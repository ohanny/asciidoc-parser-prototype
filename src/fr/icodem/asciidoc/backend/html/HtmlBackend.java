package fr.icodem.asciidoc.backend.html;

import fr.icodem.asciidoc.parser.AsciidocParserBaseHandler;
import fr.icodem.asciidoc.parser.elements.*;

import java.io.Writer;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static fr.icodem.asciidoc.backend.html.ActionRequest.ActionRequestType.*;

public class HtmlBackend extends AsciidocParserBaseHandler {

    private BlockingQueue<ActionRequest> tasks = new ArrayBlockingQueue<>(1024);
    private Semaphore semaphore = new Semaphore(0);
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private AtomicBoolean parsingFinished = new AtomicBoolean();
    private CyclicBarrier barrier = new CyclicBarrier(2);

    private HtmlBackendDelegate delegate;

    private boolean documentTitleUndefined = true;

    public HtmlBackend(Writer writer) {

        delegate = new HtmlBackendDelegate(writer);

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

    }

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

    @Override
    public void startDocument(Document doc) {
        boolean ready = doc.getTitle() != null;
        addActionRequest(StartDocument, () -> delegate.startDocument(doc), ready);
    }


    @Override
    public void startPreamble() {
        addActionRequest(StartPreamble, () -> delegate.startPreamble(), true);
    }

    @Override
    public void endPreamble() {
        addActionRequest(EndPreamble, () -> delegate.startPreamble(), true);
    }

    @Override
    public void startContent() {

    }

    @Override
    public void endContent() {
    }

    @Override
    public void endDocument(Document doc) {
        addActionRequest(EndDocument, () -> delegate.endDocument(doc), true);

        // mark end of parsing
        parsingFinished.set(true);

        // unlock outputter thread if locked
        if (semaphore.getQueueLength() == 1) {
            ActionRequest ar = tasks.peek();
            if (ar != null && ar.isReady()) {
                semaphore.release();
            }
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
    public void startDocumentTitle(DocumentTitle docTitle) {
        addActionRequest(StartDocumentTitle, () -> delegate.startDocumentTitle(docTitle), true);
    }

    @Override
    public void endDocumentTitle(DocumentTitle docTitle) {
        addActionRequest(EndDocumentTitle, () -> delegate.endDocumentTitle(docTitle), true);
    }

    @Override
    public void startTitle(Title title) {
        addActionRequest(StartTitle, () -> delegate.startTitle(title), true);
    }

    @Override
    public void startParagraph(Paragraph p) {
        addActionRequest(StartParagraph, () -> delegate.startParagraph(p), true);
    }

    @Override
    public void startSection(Section section) {
        addActionRequest(StartSection, () -> delegate.startSection(section), true);
    }

    @Override
    public void endSection(Section section) {
        addActionRequest(StartSection, () -> delegate.endSection(section), true);
    }

    @Override
    public void startSectionTitle(SectionTitle sectionTitle) {
        addActionRequest(StartSectionTitle, () -> delegate.startSectionTitle(sectionTitle), true);
    }

    @Override
    public void endSectionTitle(SectionTitle sectionTitle) {
        if (documentTitleUndefined) {
            delegate.fallbackDocumentTitle = "First Section title";
            documentTitleUndefined = false;
            tasks.stream().filter(ar -> ar.getType().equals(StartDocument)).findFirst().ifPresent(ar -> ar.ready());
            // unlock outputter thread if locked
            if (semaphore.getQueueLength() == 1) {
                ActionRequest ar = tasks.peek();
                if (ar != null && ar.isReady()) {
                    semaphore.release();
                }
            }

        }
        addActionRequest(EndSectionTitle, () -> delegate.endSectionTitle(sectionTitle), true);
    }

    @Override
    public void startAttributeEntry(AttributeEntry att) {
    }

}

