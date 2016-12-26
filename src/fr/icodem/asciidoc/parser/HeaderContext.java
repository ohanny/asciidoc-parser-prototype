package fr.icodem.asciidoc.parser;

import fr.icodem.asciidoc.parser.elements.*;

import java.util.*;
import java.util.stream.Collectors;

@Deprecated
class HeaderContext {
    Title title;
    List<Author> authors; // ANTLR
    Deque<AuthorContext> authors2; // PEG
    Map<String, AttributeEntry> nameToAttributeMap;
    boolean headerPresent;
    boolean documentTitleUndefined = true;
    boolean documentHeaderNotified;

    //static boolean peg;// remove when ANTLR is removed

    HeaderContext() {
        authors = new ArrayList<>();
        authors2 = new LinkedList<>();
        nameToAttributeMap = AttributeDefaults.Instance.getAttributes();
    }

    void setTitle(Title title) {
        this.title = title;
    }

    void addAttribute(AttributeEntry att) {
        this.nameToAttributeMap.put(att.getName(), att);
    }

    void addAuthor(Author author) { // ANTLR
        this.authors.add(author);
    }

    void addAuthor2(AuthorContext author) { // PEG
        author.position = authors.size() + 1;
        this.authors2.add(author);
    }

    AuthorContext getLastAuthor() {
        return authors2.peekLast();
    }

    int getNextAuthorPosition() {
        return (authors == null) ? 1 : authors.size() + 1;
    }

    void setHeaderPresent(boolean headerPresent) {
        this.headerPresent = headerPresent;
    }

    DocumentHeader getHeader() {
        //if (!peg) { // remove when antlr is removed
        ElementFactory ef = new ElementFactory();
        Title title = (this.title == null) ? null : ef.title(this.title.getText());

        authors2.stream().forEach(a -> System.out.println("@@"+a.name+"@@"));

        if (authors.isEmpty()) {
            authors = authors2.stream()
                    .map(ac -> new Author(null, ac.name, ac.address, ac.position))
                    .collect(Collectors.toList());
        }

        return ef.documentHeader(title,
                Collections.unmodifiableList(authors),
                Collections.unmodifiableMap(nameToAttributeMap),
                headerPresent);
//        }
//        else {
//            //Author author = ef.author(null, ctx.authorName().getText().trim(),
////                address, headerContext.getNextAuthorPosition());
//            return null;
//        }
    }

}
