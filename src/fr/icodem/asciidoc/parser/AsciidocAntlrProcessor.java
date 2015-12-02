package fr.icodem.asciidoc.parser;

import fr.icodem.asciidoc.parser.antlr.AsciidocLexer;
import fr.icodem.asciidoc.parser.antlr.AsciidocParser;
import fr.icodem.asciidoc.parser.elements.*;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static fr.icodem.asciidoc.parser.ActionRequest.ActionRequestType.*;
import static java.lang.Math.max;
import static java.lang.Math.min;

public class AsciidocAntlrProcessor extends AsciidocProcessor {

    private class ModifiableDocument {
        Title title;
        List<Author> authors;
        Map<String, AttributeEntry> nameToAttributeMap;
        boolean headerPresent;

        ModifiableDocument() {
            authors = new ArrayList<>();
            nameToAttributeMap = AttributeDefaults.Instance.getAttributes();
        }

        void setTitle(Title title) {
            this.title = title;
        }

        void addAttribute(AttributeEntry att) {
            this.nameToAttributeMap.put(att.getName(), att);
        }

        void addAuthor(Author author) {
            this.authors.add(author);
        }

        int getNextAuthorPosition() {
            int position = (authors == null)?1:authors.size() + 1;
            return position;
        }

        void setHeaderPresent(boolean headerPresent) {
            this.headerPresent = headerPresent;
        }

        DocumentHeader getHeader() {
            Title title = (this.title == null)?null:ef.title(this.title.getText());

            return ef.documentHeader(title,
                               Collections.unmodifiableList(authors),
                               Collections.unmodifiableMap(nameToAttributeMap),
                               headerPresent);
        }

    }

    private ModifiableDocument document;
    private boolean documentNotified;

    private String currentTitle;

    private List<Attribute> rawAttList;

    public AsciidocAntlrProcessor(AsciidocParserHandler handler, List<AttributeEntry> attributes) {
        super(handler, attributes);

        ///// REFACTORING
        Runnable actionTask = () -> {
            try {
                // process ready tasks
                while (!parsingFinished.get()) {
                    semaphore.acquire();
                    processReadyRequests();
                }

                // parsing is finished, do a last check
                processReadyRequests();

                // notify arrival to barrier
                barrier.await();
            } catch (BrokenBarrierException | InterruptedException e) {
                e.printStackTrace();
            }

        };
        executorService.execute(actionTask);
        /////
    }

    ///// REFACTORING
    private BlockingQueue<ActionRequest> tasks = new ArrayBlockingQueue<>(1024);
    private Semaphore semaphore = new Semaphore(0);
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private AtomicBoolean parsingFinished = new AtomicBoolean();
    private CyclicBarrier barrier = new CyclicBarrier(2);

    //private HtmlBackendDelegate delegate;
    private boolean documentTitleUndefined = true;

    private void processReadyRequests() throws InterruptedException {
        ActionRequest ar = tasks.peek();
        while (ar != null && ar.isReady()) {
            tasks.take().getAction().run();
            ar = tasks.peek();
        }
    }

    private void addActionRequest(ActionRequest.ActionRequestType type,
                                  Runnable runnable, boolean ready) {
        try {
            ActionRequest req = new ActionRequest(type, runnable, ready);
            tasks.put(req);
            // unlock outputter thread if locked
            if (semaphore.getQueueLength() == 1) {
                ActionRequest ar = tasks.peek();
                if (ar != null && ar.isReady()) {
                    semaphore.release();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /////


    private AttributeList consumeAttList() {
        AttributeList attList = ef.attributeList(rawAttList);
        rawAttList = null;// consumed

        return attList;
    }

    @Override
    public void parse(String text) {

        // create a parser for Asciidoc grammar
        ANTLRInputStream input = new ANTLRInputStream(text);
        AsciidocLexer lexer = new AsciidocLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        AsciidocParser parser = new AsciidocParser(tokens);

        // start parsing
        ParseTree tree = parser.document();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(this, tree);
    }

    @Override
    public void enterDocument(AsciidocParser.DocumentContext ctx) {
        document = new ModifiableDocument();
        //handler.startDocument();
        addActionRequest(StartDocument, () -> handler.startDocument(), true);
    }

    @Override
    public void exitDocument(AsciidocParser.DocumentContext ctx) {
        //notifyDocumentIfNotDone();// TODO

        //handler.endDocument();
        addActionRequest(EndDocument, () -> handler.endDocument(), true);

        // mark end of parsing
        parsingFinished.set(true);

        // end of parsing, mark every action ready
        tasks.stream().filter(ar -> !ar.isReady()).forEach(ar -> ar.ready());

        // unlock outputter thread if locked
        if (semaphore.getQueueLength() == 1) {
            semaphore.release();
        }


        // notify arrival to barrier (wait for writer thread to finish)
        try {
            barrier.await();
        } catch (BrokenBarrierException  | InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();

    }

    @Override
    public void enterDocumentTitle(AsciidocParser.DocumentTitleContext ctx) {
        currentTitle = null;
    }

    @Override
    public void exitDocumentTitle(AsciidocParser.DocumentTitleContext ctx) {
        if (currentTitle != null) {
            document.setTitle(ef.title(currentTitle));
            currentTitle = null;
        }
    }

    private void notifyDocumentIfNotDone() {// TODO rename method
        if (!documentNotified) {
            //handler.documentHeader(document.getHeader());
            //DocumentHeader header = document.getHeader();//TODO remove
            //boolean ready = header.getTitle() != null;// TODO find another way to compute ready

            DocumentHeader header = document.getHeader();
            boolean ready = document.headerPresent;

            addActionRequest(DocumentHeader, () -> handler.documentHeader(header), ready);
            documentNotified = true;
        }
    }

    @Override
    public void exitHeader(AsciidocParser.HeaderContext ctx) {
        document.setHeaderPresent(true);
        notifyDocumentIfNotDone();
    }

    @Override
    public void enterPreamble(AsciidocParser.PreambleContext ctx) {
        //handler.startPreamble();
        addActionRequest(StartPreamble, () -> handler.startPreamble(), true);
    }

    @Override
    public void exitPreamble(AsciidocParser.PreambleContext ctx) {
        //handler.endPreamble();
        addActionRequest(EndPreamble, () -> handler.endPreamble(), true);
    }

    @Override
    public void enterContent(AsciidocParser.ContentContext ctx) {
        notifyDocumentIfNotDone();// TODO
    }

    @Override
    public void enterTitle(AsciidocParser.TitleContext ctx) {
        currentTitle = ctx.getText();
    }


    @Override
    public void enterParagraph(AsciidocParser.ParagraphContext ctx) {
        String text = ctx.getText().trim();
        Paragraph p = ef.paragraph(consumeAttList(), text);
        //handler.startParagraph(ef.paragraph(consumeAttList(), text));
        addActionRequest(StartParagraph, () -> handler.startParagraph(p), true);
    }

    @Override
    public void enterSection(AsciidocParser.SectionContext ctx) {
        //handler.startSection(ef.section());
        Section section = ef.section();
        addActionRequest(StartSection, () -> handler.startSection(section), true);
    }

    @Override
    public void exitSection(AsciidocParser.SectionContext ctx) {
        //handler.endSection(ef.section());
        Section section = ef.section();
        addActionRequest(StartSection, () -> handler.endSection(section), true);
    }

    @Override
    public void enterSectionTitle(AsciidocParser.SectionTitleContext ctx) {
        currentTitle = null;
    }

    @Override
    public void exitSectionTitle(AsciidocParser.SectionTitleContext ctx) {
        int level = min(ctx.EQ().size(), 6);
        SectionTitle sectionTitle = ef.sectionTitle(level, currentTitle);
        //handler.startSectionTitle(ef.sectionTitle(level, currentTitle));

        if (documentTitleUndefined) {
            //delegate.fallbackDocumentTitle = sectionTitle.getText();
            documentTitleUndefined = false;                 // TODO
            document.setTitle(ef.title(currentTitle));
            //notifyDocumentIfNotDone();    // TODO
            //tasks.stream().filter(ar -> ar.getType().equals(DocumentHeader)).findFirst().ifPresent(ar -> ar.ready());
            DocumentHeader header = document.getHeader();
            tasks.stream()
                 .filter(ar -> ar.getType().equals(DocumentHeader))
                 .findFirst()
                 .ifPresent(ar -> ar.ready(() -> handler.documentHeader(header)));
            // unlock outputter thread if locked
            if (semaphore.getQueueLength() == 1) {
                ActionRequest ar = tasks.peek();
                if (ar != null && ar.isReady()) {
                    semaphore.release();
                }
            }

        }
        addActionRequest(StartSectionTitle, () -> handler.startSectionTitle(sectionTitle), true);

        currentTitle = null;

    }

    @Override
    public void enterAuthor(AsciidocParser.AuthorContext ctx) {
        final String address = (ctx.authorAddress() == null)?
                null:ctx.authorAddress().getText();
        Author author = ef.author(null, ctx.authorName().getText().trim(),
                address, document.getNextAuthorPosition());
        document.addAuthor(author);
    }

    @Override
    public void enterAttributeEntry(AsciidocParser.AttributeEntryContext ctx) {
        String value = null;
        if (ctx.attributeValueParts() != null) {
            value = ctx.attributeValueParts().getText();
        }

        boolean enabled = ctx.BANG().size() > 0;

        AttributeEntry att = ef.attributeEntry(ctx.attributeName().getText(), value, enabled);

        if (!documentNotified) {
            document.addAttribute(att);
        } else {
            //handler.startAttributeEntry(att);
            addActionRequest(StartAttributeEntry, () -> handler.startAttributeEntry(att), true);
        }
    }

    @Override
    public void enterAttributeList(AsciidocParser.AttributeListContext ctx) {
        if (rawAttList == null) {
            rawAttList = new ArrayList<>();
        }
    }

    @Override
    public void enterPositionalAttribute(AsciidocParser.PositionalAttributeContext ctx) {
        String value = ctx.attributeValue().getText();
        rawAttList.add(ef.attribute(null, value));
    }

    @Override
    public void enterIdName(AsciidocParser.IdNameContext ctx) {
        rawAttList.add(ef.attribute("id", ctx.getText()));
    }

    @Override
    public void enterRoleName(AsciidocParser.RoleNameContext ctx) {
        rawAttList.add(ef.attribute("role", ctx.getText()));
    }

    @Override
    public void enterOptionName(AsciidocParser.OptionNameContext ctx) {
        rawAttList.add(ef.attribute("options", ctx.getText()));
    }

    @Override
    public void enterNamedAttribute(AsciidocParser.NamedAttributeContext ctx) {
        String name = ctx.attributeName().getText();
        String value = ctx.attributeValue().getText();
        rawAttList.add(ef.attribute(name, value));
    }
}
