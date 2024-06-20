package pl.kacperbiegajski.spocktestgenerator.generator

import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import pl.kacperbiegajski.spocktestgenerator.parsed.ParsedConstructor
import pl.kacperbiegajski.spocktestgenerator.parsed.ParsedDependency

class ContentGeneratorKtTest {
    @Test
    fun shouldGenerateCorrectTestContent() {
        // given
        val expectedTestContent = """
            >package pl.kacperbiegajski.test
            >
            >import spock.lang.Specification
            >import spock.lang.Subject
            >
            >class FooSpec extends Specification {
            >   def dep1 = Mock(DepFoo)
            >   def dep2 = Mock(DepBar)
            >
            >   @Subject
            >   def foo = new Foo(dep1, dep2)
            >   
            >   def ""() {
            >       given:
            >       
            >       when:
            >       
            >       then:
            >   }
            >}
        """.trimMargin(">")

        val dependencies = listOf(ParsedDependency("DepFoo", "dep1"), ParsedDependency("DepBar", "dep2"))
        val parsedConstructor = ParsedConstructor("Foo", dependencies)

        // when
        val result = generateTestContent("pl.kacperbiegajski.test", parsedConstructor)

        // then

        assertEquals(expectedTestContent, result)
    }
}
