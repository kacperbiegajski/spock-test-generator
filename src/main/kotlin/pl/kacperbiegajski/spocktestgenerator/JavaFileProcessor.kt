package pl.kacperbiegajski.spocktestgenerator

import com.intellij.psi.PsiClass
import com.intellij.psi.PsiJavaFile
import com.intellij.psi.PsiParameter
import pl.kacperbiegajski.spocktestgenerator.parsed.ParsedConstructor
import pl.kacperbiegajski.spocktestgenerator.parsed.ParsedDependency


fun isMoreThanOneConstructor(file: PsiJavaFile): Boolean {
    val psiClass = extractClass(file)
    return psiClass.constructors.size > 1
}

private fun extractClass(file: PsiJavaFile): PsiClass {
    if (file.classes.size != 1) throw RuntimeException("Expected one class, but got ${file.classes.size}")
    return file.classes[0]
}

fun extractConstructor(file: PsiJavaFile): ParsedConstructor {
    val psiClass = extractClass(file)
    if (psiClass.constructors.size > 1) throw RuntimeException("Expected max 1 constructor, but got ${psiClass.constructors.size}")
    if (psiClass.constructors.isEmpty()) {
        return ParsedConstructor(psiClass.name ?: "", listOf())
    }
    val constructor = psiClass.constructors[0]
    val dependencies = constructor.parameterList.parameters.map { extractDependency(it) }
    return ParsedConstructor(constructor.name, dependencies)
}

private fun extractDependency(parameter: PsiParameter): ParsedDependency {
    val type = parameter.type.canonicalText
    val name = parameter.name
    return ParsedDependency(type, name)
}
