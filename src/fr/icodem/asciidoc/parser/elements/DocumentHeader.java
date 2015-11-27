package fr.icodem.asciidoc.parser.elements;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DocumentHeader {
    private Title title;
    private List<Author> authors;
    private Map<String, AttributeEntry> nameToAttributeMap;

    private boolean headerPresent;

    public DocumentHeader(Title title, List<Author> authors,
                    Map<String, AttributeEntry> nameToAttributeMap, boolean headerPresent) {
        this.title = title;
        this.authors = authors;
        this.nameToAttributeMap = nameToAttributeMap;
        this.headerPresent = headerPresent;

        if (this.title == null) {
            this.title = new Title(getAttributeValue("doctitle"));
        }
    }

    public Title getTitle() {
        return title;
    }

    public List<Author> getAuthors() {
        return Collections.unmodifiableList(authors);
    }

    public String getAttributeValue(String name) {
        AttributeEntry att = nameToAttributeMap.get(name);
        if (att != null) {
            return att.getValue();
        }
        return null;
    }

    public boolean isHeaderPresent() {
        return headerPresent;
    }

    public boolean isAuthorsPresent() {
        return authors.size() > 0;
    }

}
