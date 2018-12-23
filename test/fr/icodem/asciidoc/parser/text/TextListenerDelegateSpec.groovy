package fr.icodem.asciidoc.parser.text

import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.AttributeEntries
import fr.icodem.asciidoc.parser.peg.example.asciidoc.listener.TextListenerDelegate2
import spock.lang.Specification


class TextListenerDelegateSpec extends Specification {
    def delegate = new TextListenerDelegate2(null, new AttributeEntries())

    def "normal text" () {
        given:
        def text = "The sky is blue and the grass is green"

        when:
        delegate.text(text)
        def result = delegate.getFormattedText()

        then:
        result != null
        result.root != null
        result.root instanceof TextListenerDelegate2.TextChunk
        def chunk = (TextListenerDelegate2.TextChunk)result.root
        chunk.type == TextListenerDelegate2.ChunkType.Normal
        chunk.text == text
    }


    def "bold text" () {
        given:
        def text = "The sky is blue and the grass is green"

        when:
        delegate.enterBold()
        delegate.text(text)
        delegate.exitBold()
        def result = delegate.getFormattedText()

        then:
        result != null
        result.root != null
        result.root instanceof TextListenerDelegate2.TextChunk
        def chunk = (TextListenerDelegate2.TextChunk)result.root
        chunk.type == TextListenerDelegate2.ChunkType.Bold
        chunk.text == text
    }

    def "italic text" () {
        given:
        def text = "The sky is blue and the grass is green"

        when:
        delegate.enterItalic()
        delegate.text(text)
        delegate.exitItalic()
        def result = delegate.getFormattedText()

        then:
        result != null
        result.root != null
        result.root instanceof TextListenerDelegate2.TextChunk
        def chunk = (TextListenerDelegate2.TextChunk)result.root
        chunk.type == TextListenerDelegate2.ChunkType.Italic
        chunk.text == text
    }

    def "subscript text" () {
        given:
        def text = "The sky is blue and the grass is green"

        when:
        delegate.enterSubscript()
        delegate.text(text)
        delegate.exitSubscript()
        def result = delegate.getFormattedText()

        then:
        result != null
        result.root != null
        result.root instanceof TextListenerDelegate2.TextChunk
        def chunk = (TextListenerDelegate2.TextChunk)result.root
        chunk.type == TextListenerDelegate2.ChunkType.Subscript
        chunk.text == text
    }

    def "supercript text" () {
        given:
        def text = "The sky is blue and the grass is green"

        when:
        delegate.enterSuperscript()
        delegate.text(text)
        delegate.exitSuperscript()
        def result = delegate.getFormattedText()

        then:
        result != null
        result.root != null
        result.root instanceof TextListenerDelegate2.TextChunk
        def chunk = (TextListenerDelegate2.TextChunk)result.root
        chunk.type == TextListenerDelegate2.ChunkType.Superscript
        chunk.mark == null
        chunk.text == text
    }

    def "monospace text" () {
        given:
        def text = "The sky is blue and the grass is green"

        when:
        delegate.enterMonospace()
        delegate.text(text)
        delegate.exitMonospace()
        def result = delegate.getFormattedText()

        then:
        result != null
        result.root != null
        result.root instanceof TextListenerDelegate2.TextChunk
        def chunk = (TextListenerDelegate2.TextChunk)result.root
        chunk.type == TextListenerDelegate2.ChunkType.Monospace
        chunk.mark == null
        chunk.text == text
    }

    def "marked text" () {
        given:
        def text = "The sky is blue and the grass is green"

        when:
        delegate.enterMark()
        delegate.text(text)
        delegate.exitMark()
        def result = delegate.getFormattedText()

        then:
        result != null
        result.root != null
        result.root instanceof TextListenerDelegate2.TextChunk
        def chunk = (TextListenerDelegate2.TextChunk)result.root
        chunk.type == TextListenerDelegate2.ChunkType.Mark
        chunk.mark != null
        chunk.text == text
    }

    def "xref text" () {
        given:
        def label = "The sky is blue and the grass is green"
        def value = "colors"

        when:
        delegate.enterXRef()
        delegate.xrefValue(value)
        delegate.xrefLabel(label)
        delegate.exitXRef()
        def result = delegate.getFormattedText()

        then:
        result != null
        result.root != null
        result.root instanceof TextListenerDelegate2.XRefChunk
        def chunk = (TextListenerDelegate2.XRefChunk)result.root
        chunk.type == TextListenerDelegate2.ChunkType.Normal
        chunk.mark == null
        chunk.xref != null
        chunk.xref.value == "colors"
        chunk.xref.label == "The sky is blue and the grass is green"
    }

    def "three chunks : normal - bold - normal"() {
        given:
        def part1 = "The "
        def part2 = "sky"
        def part3 = " is blue and the grass is green"

        when:
        delegate.text(part1)
        delegate.enterBold()
        delegate.text(part2)
        delegate.exitBold()
        delegate.text(part3)
        def result = delegate.getFormattedText()

        then:
        result != null
        result.root != null
        result.root instanceof TextListenerDelegate2.CompositeChunk
        def chunk = (TextListenerDelegate2.CompositeChunk)result.root
        chunk.chunks != null
        chunk.chunks.size() == 3

        chunk.chunks[0] instanceof TextListenerDelegate2.TextChunk
        def chunk1 = (TextListenerDelegate2.TextChunk)chunk.chunks[0]
        chunk1.type == TextListenerDelegate2.ChunkType.Normal
        chunk1.text == "The "

        chunk.chunks[1] instanceof TextListenerDelegate2.TextChunk
        def chunk2 = (TextListenerDelegate2.TextChunk)chunk.chunks[1]
        chunk2.type == TextListenerDelegate2.ChunkType.Bold
        chunk2.text == "sky"

        chunk.chunks[2] instanceof TextListenerDelegate2.TextChunk
        def chunk3 = (TextListenerDelegate2.TextChunk)chunk.chunks[2]
        chunk3.type == TextListenerDelegate2.ChunkType.Normal
        chunk3.text == " is blue and the grass is green"

    }

    def "three chunks : normal - italic - normal"() {
        given:
        def part1 = "The "
        def part2 = "sky"
        def part3 = " is blue and the grass is green"

        when:
        delegate.text(part1)
        delegate.enterItalic()
        delegate.text(part2)
        delegate.exitItalic()
        delegate.text(part3)
        def result = delegate.getFormattedText()

        then:
        result != null
        result.root != null
        result.root instanceof TextListenerDelegate2.CompositeChunk
        def chunk = (TextListenerDelegate2.CompositeChunk)result.root
        chunk.chunks != null
        chunk.chunks.size() == 3

        chunk.chunks[0] instanceof TextListenerDelegate2.TextChunk
        def chunk1 = (TextListenerDelegate2.TextChunk)chunk.chunks[0]
        chunk1.type == TextListenerDelegate2.ChunkType.Normal
        chunk1.text == "The "

        chunk.chunks[1] instanceof TextListenerDelegate2.TextChunk
        def chunk2 = (TextListenerDelegate2.TextChunk)chunk.chunks[1]
        chunk2.type == TextListenerDelegate2.ChunkType.Italic
        chunk2.text == "sky"

        chunk.chunks[2] instanceof TextListenerDelegate2.TextChunk
        def chunk3 = (TextListenerDelegate2.TextChunk)chunk.chunks[2]
        chunk3.type == TextListenerDelegate2.ChunkType.Normal
        chunk3.text == " is blue and the grass is green"

    }


    def "three chunks : normal - xref - normal"() {
        given:
        def part1 = "The "
        def part2Label = "sky"
        def part2Value = "sky_ref"
        def part3 = " is blue and the grass is green"

        when:
        delegate.text(part1)

        delegate.enterXRef()
        delegate.xrefLabel(part2Label)
        delegate.xrefValue(part2Value)
        delegate.exitXRef()

        delegate.text(part3)
        def result = delegate.getFormattedText()

        then:
        result != null
        result.root != null
        result.root instanceof TextListenerDelegate2.CompositeChunk
        def chunk = (TextListenerDelegate2.CompositeChunk)result.root
        chunk.chunks != null
        chunk.chunks.size() == 3

        chunk.chunks[0] instanceof TextListenerDelegate2.TextChunk
        def chunk1 = (TextListenerDelegate2.TextChunk)chunk.chunks[0]
        chunk1.type == TextListenerDelegate2.ChunkType.Normal
        chunk1.text == "The "

        chunk.chunks[1] instanceof TextListenerDelegate2.XRefChunk
        def chunk2 = (TextListenerDelegate2.XRefChunk)chunk.chunks[1]
        chunk2.type == TextListenerDelegate2.ChunkType.Normal
        chunk2.mark == null
        chunk2.xref != null
        chunk2.xref.value == part2Value
        chunk2.xref.label == part2Label

        chunk.chunks[2] instanceof TextListenerDelegate2.TextChunk
        def chunk3 = (TextListenerDelegate2.TextChunk)chunk.chunks[2]
        chunk3.type == TextListenerDelegate2.ChunkType.Normal
        chunk3.text == " is blue and the grass is green"

    }

    def "three chunks : normal - mark - normal"() {
        given:
        def part1 = "The "
        def part2 = "sky"
        def part3 = " is blue and the grass is green"

        when:
        delegate.text(part1)

        delegate.enterMark()
        delegate.text(part2)
        delegate.exitMark()

        delegate.text(part3)
        def result = delegate.getFormattedText()

        then:
        result != null
        result.root != null
        result.root instanceof TextListenerDelegate2.CompositeChunk
        def chunk = (TextListenerDelegate2.CompositeChunk)result.root
        chunk.chunks != null
        chunk.chunks.size() == 3

        chunk.chunks[0] instanceof TextListenerDelegate2.TextChunk
        def chunk1 = (TextListenerDelegate2.TextChunk)chunk.chunks[0]
        chunk1.type == TextListenerDelegate2.ChunkType.Normal
        chunk1.text == "The "

        chunk.chunks[1] instanceof TextListenerDelegate2.TextChunk
        def chunk2 = (TextListenerDelegate2.TextChunk)chunk.chunks[1]
        chunk2.type == TextListenerDelegate2.ChunkType.Mark
        chunk2.mark != null
        chunk2.text == "sky"

        chunk.chunks[2] instanceof TextListenerDelegate2.TextChunk
        def chunk3 = (TextListenerDelegate2.TextChunk)chunk.chunks[2]
        chunk3.type == TextListenerDelegate2.ChunkType.Normal
        chunk3.text == " is blue and the grass is green"

    }

    def "nested chunks : bold contains italic"() {
        given:
        def part1 = "The "
        def part2 = "sky"
        def part3 = " is blue"
        def part4 = " and the grass is green"

        when:
        delegate.text(part1)
        delegate.enterBold()
        delegate.text(part2)
        delegate.enterItalic()
        delegate.text(part3)
        delegate.exitItalic()
        delegate.exitBold()
        delegate.text(part4)

        def result = delegate.getFormattedText()

        then:
        result != null
        result.root != null
        result.root instanceof TextListenerDelegate2.CompositeChunk
        def chunk = (TextListenerDelegate2.CompositeChunk)result.root
        chunk.chunks != null
        chunk.chunks.size() == 3

        chunk.chunks[0] instanceof TextListenerDelegate2.TextChunk
        def chunk1 = (TextListenerDelegate2.TextChunk)chunk.chunks[0]
        chunk1.type == TextListenerDelegate2.ChunkType.Normal
        chunk1.text == "The "


        chunk.chunks[1] instanceof TextListenerDelegate2.CompositeChunk
        def chunk2 = (TextListenerDelegate2.CompositeChunk)chunk.chunks[1]
        chunk2.type == TextListenerDelegate2.ChunkType.Bold
        chunk2.chunks != null
        chunk2.chunks.size() == 2

        chunk2.chunks[0] instanceof TextListenerDelegate2.TextChunk
        def chunk2_1 = (TextListenerDelegate2.TextChunk)chunk2.chunks[0]
        chunk2_1.type == TextListenerDelegate2.ChunkType.Normal
        chunk2_1.text == "sky"

        chunk2.chunks[1] instanceof TextListenerDelegate2.TextChunk
        def chunk2_2 = (TextListenerDelegate2.TextChunk)chunk2.chunks[1]
        chunk2_2.type == TextListenerDelegate2.ChunkType.Italic
        chunk2_2.text == " is blue"


        chunk.chunks[2] instanceof TextListenerDelegate2.TextChunk
        def chunk3 = (TextListenerDelegate2.TextChunk)chunk.chunks[2]
        chunk3.type == TextListenerDelegate2.ChunkType.Normal
        chunk3.text == " and the grass is green"

    }

    def "nested chunks : italic contains bold"() {
        given:
        def part1 = "The "
        def part2 = "sky"
        def part3 = " is blue"
        def part4 = " and the grass is green"

        when:
        delegate.text(part1)
        delegate.enterItalic()
        delegate.text(part2)
        delegate.enterBold()
        delegate.text(part3)
        delegate.exitBold()
        delegate.exitItalic()
        delegate.text(part4)

        def result = delegate.getFormattedText()

        then:
        result != null
        result.root != null
        result.root instanceof TextListenerDelegate2.CompositeChunk
        def chunk = (TextListenerDelegate2.CompositeChunk)result.root
        chunk.chunks != null
        chunk.chunks.size() == 3

        chunk.chunks[0] instanceof TextListenerDelegate2.TextChunk
        def chunk1 = (TextListenerDelegate2.TextChunk)chunk.chunks[0]
        chunk1.type == TextListenerDelegate2.ChunkType.Normal
        chunk1.text == "The "


        chunk.chunks[1] instanceof TextListenerDelegate2.CompositeChunk
        def chunk2 = (TextListenerDelegate2.CompositeChunk)chunk.chunks[1]
        chunk2.type == TextListenerDelegate2.ChunkType.Italic
        chunk2.chunks != null
        chunk2.chunks.size() == 2

        chunk2.chunks[0] instanceof TextListenerDelegate2.TextChunk
        def chunk2_1 = (TextListenerDelegate2.TextChunk)chunk2.chunks[0]
        chunk2_1.type == TextListenerDelegate2.ChunkType.Normal
        chunk2_1.text == "sky"

        chunk2.chunks[1] instanceof TextListenerDelegate2.TextChunk
        def chunk2_2 = (TextListenerDelegate2.TextChunk)chunk2.chunks[1]
        chunk2_2.type == TextListenerDelegate2.ChunkType.Bold
        chunk2_2.text == " is blue"


        chunk.chunks[2] instanceof TextListenerDelegate2.TextChunk
        def chunk3 = (TextListenerDelegate2.TextChunk)chunk.chunks[2]
        chunk3.type == TextListenerDelegate2.ChunkType.Normal
        chunk3.text == " and the grass is green"

    }

    def "multiple nested chunks"() {
        given:
        def part1 = "The "
        def part2 = "sky"
        def part3 = " is blue"
        def part4 = " and "
        def part5 = " the "
        def part6 = " grass "
        def part7 = "is"
        def part8 = " green"

        when:
        delegate.text(part1)
        delegate.enterBold()
        delegate.text(part2)
        delegate.enterSuperscript()
        delegate.text(part3)
        delegate.exitSuperscript()
        delegate.text(part4)
        delegate.exitBold()
        delegate.enterItalic()
        delegate.text(part5)
        delegate.enterSubscript()
        delegate.text(part6)
        delegate.enterMonospace()
        delegate.text(part7)
        delegate.exitMonospace()
        delegate.exitSubscript()
        delegate.text(part8)
        delegate.exitItalic()

        def result = delegate.getFormattedText()

        then:
        result != null
        result.root != null
        result.root instanceof TextListenerDelegate2.CompositeChunk
        def chunk = (TextListenerDelegate2.CompositeChunk)result.root
        chunk.chunks != null
        chunk.chunks.size() == 3

        chunk.chunks[0] instanceof TextListenerDelegate2.TextChunk
        chunk.chunks[1] instanceof TextListenerDelegate2.CompositeChunk
        chunk.chunks[2] instanceof TextListenerDelegate2.CompositeChunk

        def chunk1 = (TextListenerDelegate2.TextChunk)chunk.chunks[0]
        chunk1.type == TextListenerDelegate2.ChunkType.Normal
        chunk1.text == "The "

        def chunk2 = (TextListenerDelegate2.CompositeChunk)chunk.chunks[1]
        chunk2.type == TextListenerDelegate2.ChunkType.Bold
        chunk2.chunks != null
        chunk2.chunks.size() == 3

        def chunk3 = (TextListenerDelegate2.CompositeChunk)chunk.chunks[2]
        chunk3.type == TextListenerDelegate2.ChunkType.Italic
        chunk3.chunks != null
        chunk3.chunks.size() == 3

        def chunk2_1 = (TextListenerDelegate2.TextChunk)chunk2.chunks[0]
        chunk2_1.type == TextListenerDelegate2.ChunkType.Normal
        chunk2_1.text == "sky"

        def chunk2_2 = (TextListenerDelegate2.TextChunk)chunk2.chunks[1]
        chunk2_2.type == TextListenerDelegate2.ChunkType.Superscript
        chunk2_2.text == " is blue"

        def chunk2_3 = (TextListenerDelegate2.TextChunk)chunk2.chunks[2]
        chunk2_3.type == TextListenerDelegate2.ChunkType.Normal
        chunk2_3.text == " and "


        def chunk3_1 = (TextListenerDelegate2.TextChunk)chunk3.chunks[0]
        chunk3_1.type == TextListenerDelegate2.ChunkType.Normal
        chunk3_1.text == " the "

        def chunk3_2 = (TextListenerDelegate2.CompositeChunk)chunk3.chunks[1]
        chunk3_2.type == TextListenerDelegate2.ChunkType.Subscript
        chunk3_2.chunks != null
        chunk3_2.chunks.size() == 2

        def chunk3_3 = (TextListenerDelegate2.TextChunk)chunk3.chunks[2]
        chunk3_3.type == TextListenerDelegate2.ChunkType.Normal
        chunk3_3.text == " green"


        def chunk3_2_1 = (TextListenerDelegate2.TextChunk)chunk3_2.chunks[0]
        chunk3_2_1.type == TextListenerDelegate2.ChunkType.Normal
        chunk3_2_1.text == " grass "

        def chunk3_2_2 = (TextListenerDelegate2.TextChunk)chunk3_2.chunks[1]
        chunk3_2_2.type == TextListenerDelegate2.ChunkType.Monospace
        chunk3_2_2.text == "is"

    }

}