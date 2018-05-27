package fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.listing;

import fr.icodem.asciidoc.parser.peg.Chars;

/**
 * This class extracts callouts from one line
 */
public class CalloutProcessor {
    private int position;
    private char[] calloutNumber = new char[2];

    private LineContext context;

    public static CalloutProcessor newInstance() {
        return new CalloutProcessor();
    }

    public void processCallouts(LineContext context) {
        this.context = context;
        this.position = context.offset + context.length - 1;

        while (callout()) {
            context.length = position + 1 - context.offset;
            context.addCallout(0, ListingCallout.of(getCalloutNumber(), context.lineNumber));
        }
    }

    private int getCalloutNumber() {
        int value = Character.getNumericValue(calloutNumber[1]);
        if (calloutNumber[0] != Chars.NULL) {
            value += 10 * Character.getNumericValue(calloutNumber[0]);
        }
        return value;
    }

    // rules
    private boolean callout() {
        calloutNumber[0] = Chars.NULL;
        calloutNumber[1] = Chars.NULL;
        return optionalBlank() && matchGT() && matchNumber() && matchLT();
//        return optionalBlank() && matchGT() && matchNumber() && matchLT() && optionalBlank();
    }

    private boolean optionalBlank() {
        while (position > -1) {
            char c = context.data[position];
            if (c == ' ' || c == '\t') {
                position--;
                continue;
            }
            break;
        }
        return true;
    }

    private boolean matchGT() {
        if (context.data[position] != '>') {
            return false;
        }
        position--;

        return true;
    }

    private boolean matchNumber() {
        boolean result = false;
        while (position > -1) {
            if (!isDigit(context.data[position])) {
                break;
            }
            result = true;
            position--;
        }

        return result;
    }

    private boolean matchLT() {
        if (context.data[position] != '<') {
            return false;
        }
        position--;

        return true;
    }

    private boolean isDigit(char c) {
        switch (c) {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                if (calloutNumber[1] == Chars.NULL) {
                    calloutNumber[1] = c;
                }
                else if (calloutNumber[0] == Chars.NULL) {
                    calloutNumber[0] = c;
                }
                return true;
        }

        return false;
    }

}
