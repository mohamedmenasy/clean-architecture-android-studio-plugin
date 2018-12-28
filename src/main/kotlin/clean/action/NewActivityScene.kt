package clean.action

import clean.android.manifest.ManifestActivityUtils
import clean.android.resource.ResourceUtils
import clean.extensions.NameFormats
import clean.extensions.guard
import clean.extensions.sceneNameFormat
import clean.feature.FeatureFileCreator
import clean.feature.FeatureType
import com.android.resources.ResourceFolderType
import com.intellij.ide.fileTemplates.FileTemplateManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.psi.PsiDirectory
import org.jetbrains.android.facet.AndroidFacet
import org.jetbrains.android.util.AndroidUtils
import org.jetbrains.kotlin.psi.KtFile

/**
 * Created by Mohamed Nabil on 28/12/18.
 */
class NewActivityScene : AnAction() {

    companion object {
        private const val ACTIVITY_INJECTOR_FILE_NAME = "ActivityInjector.kt"
        private const val ACTIVITY_INJECTOR_BLOCK_PROPERTY_NAME = "NAME"
    }

    override fun actionPerformed(actionEvent: AnActionEvent) {
        try {
            val dialogValues = Messages.showInputDialogWithCheckBox(
                "Enter new feature name",
                "New Feature",
                "Insert layout file ?",
                true,
                true,
                null,
                null,
                null
            )
            if (dialogValues.first.isNullOrBlank())
                return
            val featureName: String = dialogValues.first
            val needLayoutFile = dialogValues.second
            val project = actionEvent.project
            project?.let {
                WriteCommandAction.runWriteCommandAction(project) {
                    createScene(featureName, actionEvent, project, needLayoutFile)
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            Messages.showErrorDialog(e.message, "ERROR")
        }
    }

    private fun createScene(sceneName: String, actionEvent: AnActionEvent, project: Project, needLayoutFile: Boolean) {

        val facet: AndroidFacet = getAndroidFacet(project)
        val fileName = sceneName.sceneNameFormat(NameFormats.FILE)
        val layoutName = sceneName.sceneNameFormat(NameFormats.LAYOUT)

        val manifestActivityUtils = ManifestActivityUtils(facet)

        (!manifestActivityUtils.activityAlreadyExists(fileName)).guard {
            throw IllegalStateException("Activity already exists")
        }

        val directoryName = sceneName.sceneNameFormat(NameFormats.FOLDER)
        val destinationPath = actionEvent.getData(LangDataKeys.PSI_ELEMENT) as PsiDirectory
        val sceneDirectory = destinationPath.createSubdirectory(directoryName)
        val viewDirectory = sceneDirectory.createSubdirectory("view")
        val viewModelDirectory = sceneDirectory.createSubdirectory("viewmodel")
        val useCaseDirectory = sceneDirectory.createSubdirectory("usecase")
        val dataDirectory = sceneDirectory.createSubdirectory("data")
        val remoteDirectory = dataDirectory.createSubdirectory("remote")
        val localDirectory = dataDirectory.createSubdirectory("local")
        val requestDirectory = remoteDirectory.createSubdirectory("request")
        val responseDirectory = remoteDirectory.createSubdirectory("response")

        val files = HashMap<String, PsiDirectory>()
        files["sceneDirectory"] = sceneDirectory
        files["viewDirectory"] = viewDirectory
        files["viewModelDirectory"] = viewModelDirectory
        files["useCaseDirectory"] = useCaseDirectory
        files["dataDirectory"] = dataDirectory
        files["remoteDirectory"] = remoteDirectory
        files["localDirectory"] = localDirectory
        files["requestDirectory"] = requestDirectory
        files["responseDirectory"] = responseDirectory

        val sceneFileCreator = FeatureFileCreator(project)
        sceneFileCreator.createSceneFiles(fileName, files)

        if (needLayoutFile) {
            val layoutDirectories = ResourceUtils.getResourceSubDirs(ResourceFolderType.LAYOUT, facet)
            sceneFileCreator.createLayoutFiles(layoutName, layoutDirectories)
        }

        val activityFile = viewDirectory.files.find { file ->
            file.name.contains(FeatureType.ACTIVITY.getTemplate(fileName).name)
        }
        manifestActivityUtils.addActivityEntryToManifest(activityFile as KtFile)

        //val activityInjector = FilenameIndex.getFilesByName(project,
        //     ACTIVITY_INJECTOR_FILE_NAME, GlobalSearchScope.projectScope(project)).first()


        val defaultProperties = FileTemplateManager.getInstance(project).defaultProperties
        defaultProperties[ACTIVITY_INJECTOR_BLOCK_PROPERTY_NAME] = fileName

        // ImportUtils.addImport(activityInjector, project, activityFile.packageFqName)

        // DI
        //  KotlinCodeBlockUtils.insertCodeBlock(project, CodeBlockTemplate.ACTIVITY_INJECTOR.fileName, activityInjector, defaultProperties)

        FileEditorManager.getInstance(project).openFile(activityFile.virtualFile, true)

    }

    private fun getAndroidFacet(project: Project): AndroidFacet {
        val applicationFacets = AndroidUtils.getApplicationFacets(project)
        if (applicationFacets.isEmpty()) {
            throw IllegalStateException("Android Module has not found")
        }
        return applicationFacets[0]

    }
}
