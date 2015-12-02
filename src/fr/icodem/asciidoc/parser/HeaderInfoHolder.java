package fr.icodem.asciidoc.parser;

import fr.icodem.asciidoc.parser.elements.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

class HeaderInfoHolder {
    Title title;
    List<Author> authors;
    Map<String, AttributeEntry> nameToAttributeMap;
    boolean headerPresent;
    boolean documentTitleUndefined = true;
    boolean documentHeaderNotified;

    HeaderInfoHolder() {
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
        return (authors == null)?1:authors.size() + 1;
    }

    void setHeaderPresent(boolean headerPresent) {
        this.headerPresent = headerPresent;
    }

    DocumentHeader getHeader() {
        ElementFactory ef = new ElementFactory();
        Title title = (this.title == null)?null:ef.title(this.title.getText());

        return ef.documentHeader(title,
                Collections.unmodifiableList(authors),
                Collections.unmodifiableMap(nameToAttributeMap),
                headerPresent);
    }

}
