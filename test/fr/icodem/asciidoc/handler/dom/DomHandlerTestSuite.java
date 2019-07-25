package fr.icodem.asciidoc.handler.dom;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
  DocumentSpec.class,
  AttributeEntrySpec.class,
  AuthorSpec.class,
  RevisionInfoSpec.class,
  SectionSpec.class,
  ParagraphSpec.class
})

public class DomHandlerTestSuite {
}
