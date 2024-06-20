package pl.kacperbiegajski.spocktestgenerator.parser


fun parseConstructor(text: String): ParsedConstructor {
    val name = text.substringBefore("(")
    val dependencies = text.substringAfter("(").substringBeforeLast(")").split(",")
            .map { it.trimStart() }
            .map { it.trimEnd() }
            .map { it.split(" ", "\n") }
            .map { removeEmpty(it) }
            .map { removeAnnotations(it) }
            .map { ParsedDependency(it[0], it[1]) }
    return ParsedConstructor(name, dependencies)
}

private fun removeAnnotations(argElements: List<String>): List<String> {
    return argElements.filter { !it.trimStart().startsWith("@") }
}

private fun removeEmpty(argElements: List<String>): List<String> {
    return argElements.filter { it.isNotEmpty() }
}

