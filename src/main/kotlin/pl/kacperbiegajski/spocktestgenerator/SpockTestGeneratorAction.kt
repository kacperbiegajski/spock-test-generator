package pl.kacperbiegajski.spocktestgenerator

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.PsiJavaFile
import pl.kacperbiegajski.spocktestgenerator.generator.generateTestContent
import pl.kacperbiegajski.spocktestgenerator.generator.generateTestFile


class SpockTestGeneratorAction: AnAction() {

    override fun update(event: AnActionEvent) {
        event.project ?: return
        event.getData(CommonDataKeys.EDITOR) ?: return
        val fileExtension = event.getData(CommonDataKeys.VIRTUAL_FILE)?.extension ?: return
        event.presentation.isEnabledAndVisible = fileExtension == "java"
    }

    override fun actionPerformed(event: AnActionEvent) {
        try {
            val javaFile = getJavaFile(event)
            if (javaFile == null) {
                displayError("Could not map file to Java file")
                return
            }
            if (isMoreThanOneConstructor(javaFile)) {
                displayError("Found more than one constructor")
                return
            }
            val path =javaFile.parent?.virtualFile?.canonicalPath
            if (path == null) {
                displayError("Could not get file path")
                return
            }
            val packageName = javaFile.packageName
            val parsedConstructor = extractConstructor(javaFile)
            val testFilePath = generateTestFile(path, parsedConstructor.name)
            testFilePath.toFile().writeText(generateTestContent(packageName, parsedConstructor))
            event.project?.let {
                val createdFile = VfsUtil.findFile(testFilePath, true)
                if (createdFile == null) {
                    displayError("Could not find created file")
                    return
                }
                FileEditorManager.getInstance(it).openFile(createdFile, true)
            }
        } catch (e: Exception) {
            displayError(e.message ?: "No message available")
        }
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.EDT
    }

    private fun getJavaFile(event: AnActionEvent): PsiJavaFile? {
        val psiFile = event.getData(CommonDataKeys.PSI_FILE)
        if (psiFile !is PsiJavaFile) {
            return null
        }
        return psiFile
    }

    private fun displayError(message: String) {
        Messages.showErrorDialog(message, "Error")
    }
}
