package pl.kacperbiegajski.spocktestgenerator.parsed

data class ParsedConstructor(
        val name: String,
        val dependencies: List<ParsedDependency>
)
