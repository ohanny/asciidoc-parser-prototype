package fr.icodem.asciidoc.parser.peg.example.asciidoc.writer.html;

public enum CssElement {
    Arabic, Decimal, LowerAlpha, UpperAlpha, LowerRoman, UpperRoman, LowerGreek; // ordered list


    public static CssElement getOrderedListNumerationStyle(int level) {
        switch (level) {
            case 1: return Arabic;
            case 2: return LowerAlpha;
            case 3: return LowerRoman;
            case 4: return UpperAlpha;
            case 5:
            default: return UpperRoman;
        }
    }

    public static CssElement getOrderedListNumerationStyle(String name) {
        if (name == null) return null;
        switch (name) {
            case "arabic": return Arabic;
            case "loweralpha": return LowerAlpha;
            case "lowerroman": return LowerRoman;
            case "upperalpha": return UpperAlpha;
            case "upperroman": return UpperRoman;
            case "lowergreek": return LowerGreek;
            default: return null;
        }
    }

    public String getOrderedListNumerationType() {
        switch (this) {
            case LowerAlpha: return "a";
            case UpperAlpha: return "A";
            case LowerRoman: return "i";
            case UpperRoman: return "I";
            default: return null;
        }
    }

    public String getOrderedListNumerationStyleName() {
        switch (this) {
            case Arabic: return "arabic";
            case Decimal: return "decimal";
            case LowerAlpha: return "loweralpha";
            case UpperAlpha: return "upperalpha";
            case LowerRoman: return "lowerroman";
            case UpperRoman: return "upperroman";
            case LowerGreek: return "lowergreek";
            default: return null;
        }
    }
}
