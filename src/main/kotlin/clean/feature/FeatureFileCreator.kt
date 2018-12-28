package clean.feature

import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.fileTemplates.FileTemplateUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDirectory
import org.jetbrains.kotlin.idea.refactoring.toPsiDirectory

/**
 * Created by Mohamed Nabil on 28/12/18.
 */
class FeatureFileCreator(private val project: Project) {


    fun createSceneFiles(sceneName: String, destinationDirectory: HashMap<String, PsiDirectory>) {

        FeatureType.values().forEach { sceneType ->
            if (sceneType.isSourceFile()) {
                val template = sceneType.getTemplate(sceneName)
                when (sceneType) {
                    FeatureType.ACTIVITY -> createTemplateFile(template, destinationDirectory["viewDirectory"]!!)
                    FeatureType.FRAGMENT -> createTemplateFile(template, destinationDirectory["viewDirectory"]!!)
                    FeatureType.VIEW_MODEL -> createTemplateFile(template, destinationDirectory["viewModelDirectory"]!!)
                    else -> {
                    }
                }
            }
        }
    }

    fun createLayoutFiles(sceneName: String, destinationDirectories: List<VirtualFile>) {
        val template = FeatureType.LAYOUT.getTemplate(sceneName)
        destinationDirectories.forEach { directory ->
            createTemplateFile(template, directory.toPsiDirectory(project)!!)
        }
    }

    private fun createTemplateFile(template: FeatureTemplate, destinationDirectory: PsiDirectory) {

        val fileTemplate = FileTemplateManager.getInstance(project).getInternalTemplate(template.templateFileName)
        val templateProperties = FileTemplateManager.getInstance(project).defaultProperties
        FileTemplateUtil.createFromTemplate(
            fileTemplate,
            template.name,
            template.getProperties(templateProperties),
            destinationDirectory
        )
    }

}

