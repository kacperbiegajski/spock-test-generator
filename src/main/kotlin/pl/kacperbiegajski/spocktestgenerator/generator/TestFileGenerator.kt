package pl.kacperbiegajski.spocktestgenerator.generator

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.absolutePathString

fun generateTestFile(classPath: String, className: String): Path {
    val testDirPath = createTestDirectories(classPath)
    return createTestFile(testDirPath, className)
}

private fun createTestFile(testDirPath: Path, className: String): Path {
    val testPathString = testDirPath.absolutePathString().plus("/${className}Spec.groovy")
    return Files.createFile(Paths.get(testPathString))
}

private fun createTestDirectories(classPath: String): Path {
    val testPath = classPath.replace("src/main/java", "src/test/groovy")
    return Files.createDirectories(Paths.get(testPath))
}
