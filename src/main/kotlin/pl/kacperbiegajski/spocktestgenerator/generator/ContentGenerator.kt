package pl.kacperbiegajski.spocktestgenerator.generator

import pl.kacperbiegajski.spocktestgenerator.parser.ParsedConstructor
import pl.kacperbiegajski.spocktestgenerator.parser.ParsedDependency

fun generateTestContent(packageName: String, parsedConstructor: ParsedConstructor): String {
    return """
        >package $packageName
        >
        >import spock.lang.Specification
        >import spock.lang.Subject
        >
        >class ${parsedConstructor.name}Spec extends Specification {
        ${generateMocksDef(parsedConstructor)}
        >   @Subject
        ${generateConstructorCall(parsedConstructor)}
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
}

private fun generateMocksDef(parsedConstructor: ParsedConstructor): String {
    val builder = StringBuilder()
    parsedConstructor.dependencies.forEach { builder.append(mockLine(it.name, it.type)) }
    return builder.toString()
}

private fun mockLine(varName: String, dependencyName: String): String {
    return ">   def $varName = Mock($dependencyName)\n"
}

private fun generateConstructorCall(parsedConstructor: ParsedConstructor): String {
    val builder = StringBuilder()
    builder.append(">   def ${decapitalize(parsedConstructor.name)} = new ${parsedConstructor.name}(${dependenciesNamesCommaSeparated(parsedConstructor.dependencies)})")
    return builder.toString()
}

private fun dependenciesNamesCommaSeparated(dependencies: List<ParsedDependency>): String {
    val builder = StringBuilder()
    dependencies.forEach {builder.append(it.name).append(", ")}
    return builder.toString().dropLast(2)
}

private fun decapitalize(text: String): String {
    return text.replaceFirstChar { it.lowercaseChar() }
}
