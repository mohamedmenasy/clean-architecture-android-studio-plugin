package clean.feature

import clean.extensions.NameFormats
import clean.extensions.sceneNameFormat
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.ide.fileTemplates.FileTemplateUtil
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import java.util.*

/**
 * Created by Mohamed Nabil on 28/12/18.
 */

sealed class FeatureTemplate(val sceneName: String) {

    abstract val name: String
    abstract val templateFileName: String


    fun createTemplate(project: Project, destinationDirectory: PsiDirectory): PsiElement {

        val templateName = FileTemplateManager.getInstance(project).getInternalTemplate(templateFileName)
        val templateProperties = FileTemplateManager.getInstance(project).defaultProperties
        return FileTemplateUtil.createFromTemplate(
            templateName,
            name,
            getProperties(templateProperties),
            destinationDirectory
        )

    }

    abstract fun getProperties(templateProperties: Properties): Properties?

    class Activity(sceneName: String) : FeatureTemplate(sceneName) {

        override fun getProperties(templateProperties: Properties): Properties? {
            templateProperties["FRAGMENT_NAME"] = Fragment(sceneName).name

            return templateProperties
        }

        override val name: String = "${sceneName}Activity"
        override val templateFileName: String = "CleanActivity"

    }

    class ViewModel(sceneName: String) : FeatureTemplate(sceneName) {
        override fun getProperties(templateProperties: Properties): Properties? = null
        override val name: String = "${sceneName}ViewModel"
        override val templateFileName: String = "CleanViewModel"
    }

    class Layout(sceneName: String) : FeatureTemplate(sceneName) {
        override fun getProperties(templateProperties: Properties): Properties? = null
        override val name: String = "fragment_$sceneName"
        override val templateFileName: String = "Layout"
    }


    class Fragment(sceneName: String) : FeatureTemplate(sceneName) {
        override val name = "${sceneName}Fragment"
        override val templateFileName = "CleanFragment"
        override fun getProperties(templateProperties: Properties): Properties? {
            templateProperties["LAYOUT_FILE_NAME"] = sceneName
            templateProperties["VIEW_MODEL"] = ViewModel(sceneName).name
            templateProperties["LAYOUT_FILE_NAME"] = sceneName.sceneNameFormat(NameFormats.LAYOUT)

            return templateProperties
        }

    }
}