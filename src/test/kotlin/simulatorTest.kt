import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertFalse

class TestStatement {

    //NFA из примера--------------------------------------------------------------------------------------

    @Test
    fun `sample zero`() {
        assertTrue(simulator("src/test/resources/sample/zero.txt"))
    }

    @Test
    fun `sample one`() {
        assertFalse(simulator("src/test/resources/sample/one.txt"))
    }

    @Test
    fun `sample one zero`() {
        assertFalse(simulator("src/test/resources/sample/one-zero.txt"))
    }

    @Test
    fun `sample simple false`() {
        assertFalse(simulator("src/test/resources/sample/simple_false.txt"))
    }

    @Test
    fun `sample simple true`() {
        assertTrue(simulator("src/test/resources/sample/simple_true.txt"))
    }

    @Test
    fun `sample basic false`() {
        assertFalse(simulator("src/test/resources/sample/basic_false.txt"))
    }

    @Test
    fun `sample basic true`() {
        assertTrue(simulator("src/test/resources/sample/basic_true.txt"))
    }

    //NFA которая проверяет что в строке только буквы a и их количество кратно 3 или 5--------------------------------------------------------------------------------------

    @Test
    fun `a_3_5 zero`() {
        assertTrue(simulator("src/test/resources/only_a_3_or_5/zero.txt"))
    }

    @Test
    fun `a_3_5 one`() {
        assertFalse(simulator("src/test/resources/only_a_3_or_5/one.txt"))
    }

    @Test
    fun `a_3_5 one zero`() {
        assertFalse(simulator("src/test/resources/only_a_3_or_5/one-zero.txt"))
    }

    @Test
    fun `a_3_5 simple false`() {
        assertFalse(simulator("src/test/resources/only_a_3_or_5/simple_false.txt"))
    }

    @Test
    fun `a_3_5 simple true`() {
        assertTrue(simulator("src/test/resources/only_a_3_or_5/simple_true.txt"))
    }

    @Test
    fun `a_3_5 basic false`() {
        assertFalse(simulator("src/test/resources/only_a_3_or_5/basic_false.txt"))
    }

    @Test
    fun `a_3_5 basic true`() {
        assertTrue(simulator("src/test/resources/only_a_3_or_5/basic_true.txt"))
    }

    //DFA проверяющее наличие в строке последовательности a b a b a + некоторые допустимые вставки между ними--------------------------------------------------------------------------------------

    @Test
    fun `ababa zero`() {
        assertFalse(simulator("src/test/resources/ababa/zero.txt"))
    }

    @Test
    fun `ababa one`() {
        assertFalse(simulator("src/test/resources/ababa/one.txt"))
    }

    @Test
    fun `ababa one zero`() {
        assertTrue(simulator("src/test/resources/ababa/one-zero.txt"))
    }

    @Test
    fun `ababa simple false`() {
        assertFalse(simulator("src/test/resources/ababa/simple_false.txt"))
    }

    @Test
    fun `ababa simple true`() {
        assertTrue(simulator("src/test/resources/ababa/simple_true.txt"))
    }

    @Test
    fun `ababa basic false`() {
        assertFalse(simulator("src/test/resources/ababa/basic_false.txt"))
    }

    @Test
    fun `ababa basic true`() {
        assertTrue(simulator("src/test/resources/ababa/basic_true.txt"))
    }
}