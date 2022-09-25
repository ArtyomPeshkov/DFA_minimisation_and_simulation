import java.io.File
import kotlin.streams.asSequence
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class TestConvert{
    @Test
    fun sample() {
        assertEquals("5\n" +
                "2\n" +
                "0\n" +
                "1 4\n" +
                "0 0 1\n" +
                "0 1 2\n" +
                "1 0 3\n" +
                "1 1 2\n" +
                "2 0 0\n" +
                "2 1 2\n" +
                "3 0 4\n" +
                "3 1 2\n" +
                "4 0 4\n" +
                "4 1 2\n",convertInfo("src/test/resources/convert/sample.txt"))
    }

    @Test
    fun `ab simple`() {
        assertEquals("8\n" +
                "2\n" +
                "0\n" +
                "4 5 6 7\n" +
                "0 0 0\n" +
                "0 1 1\n" +
                "1 0 2\n" +
                "1 1 3\n" +
                "2 0 4\n" +
                "2 1 5\n" +
                "3 0 6\n" +
                "3 1 7\n" +
                "4 0 0\n" +
                "4 1 1\n" +
                "5 0 2\n" +
                "5 1 3\n" +
                "6 0 4\n" +
                "6 1 5\n" +
                "7 0 6\n" +
                "7 1 7\n",convertInfo("src/test/resources/convert/ab_simple.txt"))
    }

    @Test
    fun `ab result compare`() {
        for (i in 0..100) {
            val source = "01"
            val str = java.util.Random().ints((1..10).random().toLong(), 0, source.length)
                .asSequence()
                .map(source::get)
                .joinToString(" ")
            println(str)
            val sourceFile = File("src/test/resources/convert/ab_simple.txt")
            val first = File("src/test/resources/convert/compfirst.txt")
            val second = File("src/test/resources/convert/compsec.txt")
            first.writeText(str + "\n")
            second.writeText(str + "\n")
            first.appendText(sourceFile.readText())
            second.appendText(convertInfo(sourceFile.path))
            assertEquals(simulator("src/test/resources/convert/compfirst.txt"), simulator("src/test/resources/convert/compsec.txt"))
        }

    }
}