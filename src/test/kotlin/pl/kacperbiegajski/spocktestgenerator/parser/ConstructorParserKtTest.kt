package pl.kacperbiegajski.spocktestgenerator.parser

import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class ConstructorParserKtTest {
    @Test
    fun shouldParseConstructor() {
        // given
        val constructorText = """
            >Foo(
            >   @JsonIgonre("asd")
            >   @Min(1)
            >   DepBoo boo,
            >   
            >   @Qualified("aaa") DepZoo zoo, DepNoo noo
            >) {
        """.trimMargin(">")
        val expectedParsedConstructor = ParsedConstructor("Foo", listOf(
            ParsedDependency("DepBoo", "boo"),
            ParsedDependency("DepZoo", "zoo"),
            ParsedDependency("DepNoo", "noo")
        ))

        // when
        val result = parseConstructor(constructorText)

        // then
        assertEquals(expectedParsedConstructor, result)
    }
}
