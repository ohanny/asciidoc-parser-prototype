package fr.icodem.asciidoc.parser.peg.example.asciidoc.renderer.html;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.renderer.DocumentWriter;

import java.io.StringReader;

public class Main {
    public static void main(String[] args) {
        String text =
                "= Hello\n" +
                        "John Doe; Roger Rabbit <roger@mail.com>; François Pignon <fp@mail.com[@françois]>; Alice <http://www.gutenberg.org/cache/epub/11/pg11.txt[@alice]>\n" +
                        //":fruit: kiwi\n" +
                        //":fruit2!:\n" +
                        //":!fruit3:\n" +
                        ":toc: right\n" +
                        "\n" +
                        "include::file1.adoc[]\n"  +
                        "\n" +
                        "The sun, *the earth* and _the_ sea.\n" +
                        "\n" +
                        "The sun, ~the earth~ and ^the^ sea.\n" +
                        "\n" +
                        "The sun, `the earth` and [big]#the# sea.\n" +
                        "\n" +
                        "'''\n" +
                        "\n" +
                        "= About fruits\n" +
                        "\n" +
                        "== Orange\n" +
                        "\n" +
                        "== Apple\n" +
                        "\n" +
                        "== Orange\n" +
                        "\n" +
                        "Block before rule \n" +
                        "\n" +
                        "'''\n" +
                        "\n" +
                        "Block below rule\n" +
                        "\n" +
                        ". Pomme\n" +
                        ". Poire\n" +
                        ".. Cerise\n" +
                        ".. Kiwi\n" +
                        "* Mangue\n" +
                        "* Kiwai\n" +
                        "\n" +
                        ":fruit: banana\n" +
                        ":fruit2!:\n" +
                        ":!fruit3:\n" +
                        "\n" +
                        "== Other fruits\n" +
                        "\n" +
                        "I love fruits\n" +
                        "\n" +
                        "* One\n" +
                        "+\n" +
                        "Un paragraphe\n" +
                        "\n" +
                        "* Two \n" +
                        "* Three\n" +
                        "\n" +
                        "\n" +
                        "** Un\n" +
                        "** Deux\n" +
                        "*** Trois\n" +
                        "*** Quatre\n" +
                        "* Zéro\n" +
                        "\n" +
                        "[lowerroman.summary.incremental%header%footer,xxx=yyy,xxx=zzz]\n" +
                        "** Apple\n" +
                        //". Apple\n" +
                        "** Kiwi\n" +
                        "** Cherry\n" +
                        "* Banana\n" +
                        "[lowergreek]\n" +
                        ". Lemon\n" +
                        ". Strawberry\n" +
                        ".. Kaki\n" +
                        ".. Kiwai\n" +
                        "\n" +
                        "Titre 1:: contenu 1\n" +
                        "Titre 2:: contenu 2\n" +
                        "Titre 3::\n" +
                        "+\n" +
                        "contenu 3\n" +
                        "\n" +
                        "Titre 4::\n" +
                        "+\n" +
                        "* Item 1\n" +
                        "* Item 2\n" +
                        "\n" +
                        "NOTE: this is a note\n" +
                        "\n" +
                        "= About vegetables\n" +
                        "\n" +
                        "== Cabbage\n" +
                        "\n" +
                        "== Carrot\n" +
                        "\n" +
                        "=== Red Carrot\n" +
                        "\n" +
                        "Paragraph #2\n" +
                        "\n" +
                        "include::file1.adoc[]\n" +
                        "\n" +
                        "image::sunset.jpg[Sunset]\n" +
                        "\n" +
                        "[source,java]\n" +
                        "----\n" +
                        "public class Produit {\n" +
                        "  private int id;\n" +
                        "  private String nom;\n" +
                        "\n" +
                        "  public void afficher() {\n" +
                        "    System.out.println(\"nom : \" + nom)\n" +
                        "  }\n" +
                        "\n" +
                        "}\n" +
                        "----\n" +
                        "\n";

        if (false) text =  "Block above\n" +
                "\n" +
                "include::file.adoc[]\n" +
                "\n" +
                "Block below";

        if (false) text =  "* A\n" +
                "\n" +
                "include::f[]\n" +
                "\n" +
                "* B\n";


        //String includedText = "\n\nLe *ciel* est bleu.\n\n";
//        String includedText = "\n* Le *ciel* est bleu.";
        String includedText = "\nX\n";

        //List<AttributeEntry> attributes = new ArrayList<>();

//        System.out.println("\r\nWITH PEG\r\n");
//        StringWriter writer = new StringWriter();
//        new AsciidocPegProcessor(new HtmlBackend(writer), attributes).parse(text);
//        System.out.println(writer);

        //System.out.println("\r\nWITH NEW PEG\r\n");
        //StringWriter writer = new StringWriter();
        DocumentWriter writer = DocumentWriter.bufferedWriter();
//        DocumentWriter writer = DocumentWriter.fileWriter("test.html");
        DefaultHtmlRenderer.withWriter(writer)
                .withSourceResolver(name -> new StringReader(includedText))
                .render(text);
        System.out.println(writer);
        //writer.flush();

    }
}
