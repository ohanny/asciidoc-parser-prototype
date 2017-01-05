package fr.icodem.asciidoc.parser.peg.buffers;

import java.util.Arrays;

public class NewLinesTracker {
    private int[] newLinePositions;
    private int lastNewLinePositionIndex;

    private int lineNumber;

    public void init() {
        newLinePositions = new int[128];
        clear();
    }

    public void clear() {
        Arrays.fill(newLinePositions, -1);
        lastNewLinePositionIndex = -1;
    }

    public void addNewLine(int position) {
        ensureCapacity();

        newLinePositions[++lastNewLinePositionIndex] = position;
        lineNumber++;
    }

    private void ensureCapacity() {
        if (newLinePositions.length == lastNewLinePositionIndex + 1) {
            newLinePositions = Arrays.copyOf(newLinePositions, newLinePositions.length * 2);
        }
    }

    public void sync(int position) {
        for (int i = lastNewLinePositionIndex; i > -1 ; i--) {
            if (position >= newLinePositions[lastNewLinePositionIndex]) {
                break;
            }
            else {
                lineNumber--;
                newLinePositions[lastNewLinePositionIndex--] = -1;
            }
        }
    }

    public int getPositionInLine(int position) {
        if (lastNewLinePositionIndex > -1) {
            if (newLinePositions[lastNewLinePositionIndex] == position) {
                if (lastNewLinePositionIndex > 0) {
                    return position - newLinePositions[lastNewLinePositionIndex - 1] - 1;
                }
                return position;
            }

            return position - newLinePositions[lastNewLinePositionIndex] - 1;
        }
        return position;
//        return position + offset; TODO case when no new lines in buffer
    }

    public int getLineNumber() {
        return lineNumber;
    }
}
