package pl.kacperbiegajski.spocktestgenerator.parser

data class ParsedConstructor(
        val name: String,
        val dependencies: List<ParsedDependency>
)
