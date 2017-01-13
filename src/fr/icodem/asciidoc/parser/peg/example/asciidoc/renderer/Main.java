package fr.icodem.asciidoc.parser.peg.example.asciidoc.renderer;

import fr.icodem.asciidoc.parser.peg.example.asciidoc.renderer.html.DefaultHtmlRenderer;
import fr.icodem.asciidoc.parser.peg.example.asciidoc.renderer.shower.ShowerRenderer;

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
                        ":icons: font\n" +
                        ":source-highlighter: highlightjs\n" +
                        ":highlight-selective: \n" +
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
                        "[.orange]\n" +
                        "Orange is a citrus fruit\n" +
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
//                        ". Pomme\n" +
//                        ". Poire\n" +
//                        ".. Cerise\n" +
//                        ".. Kiwi\n" +
//                        "* Mangue\n" +
//                        "* Kiwai\n" +
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
                        "  private int id;      <1> <2>\n" +
                        "  private String nom;  <3>\n" +
                        "\n" +
                        "  public void afficher() {\n" +
                        "    System.out.println(\"nom : \" + nom)\n" +
                        "  }\n" +
                        "\n" +
                        "}\n" +
                        "----\n" +
                        "<1> instance field\n" +
                        "<2> type int\n" +
                        "<3> type String\n" +
                        "\n" +
                        "[source,java,options=highlight]\n" +
                        "----\n" +
                        "public class Famille {\n" +
                        "  private int id;\n" +
                        "  private String nom;\n" +
                        "\n" +
                        "}\n" +
                        "----\n" +
                        "\n" +
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

        if (true)
        text = "= Shower Presentation Engine\n" +
               "iodoc\n" +
               ":highlight-selective:\n" +
               "\n" +
               "\n" +
               "A shower presentation generated by iodoc\n" +
               "\n" +
               "= Shower Presentation Engine\n" +
               "\n" +
               "Brought to you by http://pepelsbey.net[Vadim Makeev]\n" +
               "\n" +
               "image::pictures/cover.jpg[cover]\n" +
               "\n" +
               "== Shower key features\n" +
               "\n" +
               ". Built on HTML, CSS and vanilla JavaScript\n" +
               ". Works in all modern browsers\n" +
               ". Themes are separated from engine\n" +
               ". Modular and extensible\n" +
               ". Fully keyboard accessible\n" +
               ". Printable to PDF\n" +
               "\n" +
               "NOTE: Shower ['ʃəuə] noun. A person or thing that shows.\n" +
               "\n" +
               "== Plain text on your slides\n" +
               "\n" +
               "Lorem ipsum dolor sit amet, consectetur <<4,adipisicing>> elit, sed do eiusmod tempor " +
               "incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, _quis nostrud_ exercitation " +
               "ullamco laboris *nisi ut aliquip* ex ea commodo consequat. Duis aute irure _dolor_ " +
               "in reprehenderit in voluptate velit esse cillum *dolore* eu fugiat nulla pariatur. Excepteur sint " +
               "occaecat cupidatat non proident, sunt in `&lt;culpa&gt;` qui officia deserunt mollit anim id " +
               "est laborum.\n" +
               "\n" +
               "== Two columns if you like\n" +
               "\n" +
               "[.double]\n" +
               "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore " +
                "et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut " +
                "aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum " +
                "dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia.\n" +
               "\n" +
               "== All kind of lists\n" +
               "\n" +
               ". Simple lists are marked with bullets\n" +
               ". Ordered lists begin with a number\n" +
               ". You can even nest lists one inside another\n" +
               "* Or mix their types\n" +
               "* But do not go too far\n" +
               "* Otherwise audience will be bored\n" +
               ". Look, seven rows exactly!\n" +
               "\n" +
               "== Serious citations\n" +
               "\n" +
               "[quote,Marcus Tullius Cicero]\n" +
               "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et " +
               "dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip " +
               "ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu " +
               "fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia.\n" +
               "\n" +
               "== Code samples\n" +
               "\n" +
               "[source,html,linenums,options=\"highlight\"]\n" +
               "----\n" +
               "\t\t\t<!DOCTYPE html>    <1> <2>\n" +
               "\t\t\t<html lang=\"en\">\n" +
               "\t\t\t<head> <!--Comment--> <3>\n" +
               "\t\t\t    <title>Shower</title>\n" +
               "\t\t\t    <meta charset=\"UTF-8\">\n" +
               "\t\t\t    <link rel=\"stylesheet\" href=\"screen.css\">\n" +
               "\t\t\t</head>\n" +
               "----\n" +
               "\n" +
               "== Even tables\n" +
               "\n" +
               "[%autowidth]\n" +
               "|===\n" +
               "|Locavore|Umami|Helvetica|Vegan\n" +
               "\n" +
               "|Fingerstache|Kale|Chips|Keytar\n" +
               "|Sriracha|Gluten-free|Ennui|Keffiyeh\n" +
               "|Thundercats|Jean|Shorts|Biodiesel\n" +
               "|Terry|Richardson|Swag|Blog\n" +
               "|===\n" +
               "\n" +
               "It’s good to have information organized.\n" +
               "\n" +
               "== Pictures\n" +
               "\n" +
               "[.cover]\n" +
               "image::pictures/picture.jpg[]\n" +
               "\n" +
               "[%shout%shrink]\n" +
               "== You can even shout this way\n" +
               "\n" +
               "== Inner navigation\n" +
               "\n" +
               "[%step]\n" +
               ". Lets you reveal list items one by one\n" +
               ". To keep some key points\n" +
               ". In secret from audience\n" +
               ". But it will work only once\n" +
               ". Nobody wants to see the same joke twice\n" +
               "\n" +
               "[.grid]\n" +
               "== All nicely aligned to grid\n" +
               "\n" +
               "\n" +
               "\n" +
               "\n";

        //String includedText = "\n\nLe *ciel* est bleu.\n\n";
//        String includedText = "\n* Le *ciel* est bleu.";
        String includedText = "\nX\n";

        DocumentWriter writer = DocumentWriter.bufferedWriter();
//        DocumentWriter writer = DocumentWriter.fileWriter("test.html");

        boolean shower = true;
        if (!shower) {
            DefaultHtmlRenderer.withWriter(writer)
                    .withSourceResolver(name -> new StringReader(includedText))
                    .render(text);
        } else {
            ShowerRenderer.withWriter(writer)
                    .withSourceResolver(name -> new StringReader(includedText))
                    .render(text);
        }

        System.out.println(writer);

    }
}
