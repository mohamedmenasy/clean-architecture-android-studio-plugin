package clean.action

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.components.JBCheckBox
import java.awt.Dimension
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

/**
 * Created by Mohamed Nabil on 28/12/18.
 */
class SceneDialog(val project: Project) : DialogWrapper(project) {

    private val myPanel = JPanel(GridBagLayout())
    private val scenarioText = JLabel("Insert Feature name:")
    private val scenarioName = JTextField(30)
    private val isMainActivityCheckBox = JBCheckBox("Is main activity?", false)
    private val layoutRequired = JBCheckBox("Do you want a layout file?", false)


    init {
        title = "New Feature Plugin"
        myPanel.preferredSize = Dimension(350, -1)
        myPanel.add(
            scenarioText,
            GridBagConstraints().apply {
                gridx = 0; gridy = 0; insets = Insets(3, 3, 3, 0); fill = GridBagConstraints.HORIZONTAL
            })
        scenarioName.preferredSize = Dimension(250, -1)



        myPanel.add(
            scenarioName,
            GridBagConstraints().apply {
                gridx = 1; gridy = 0; insets = Insets(3, 0, 3, 10); weightx = 2.0; fill = GridBagConstraints.HORIZONTAL
            })
        myPanel.add(
            isMainActivityCheckBox,
            GridBagConstraints().apply {
                gridx = 0; gridy = 1; insets = Insets(3, 3, 3, 10); fill = GridBagConstraints.HORIZONTAL
            })


        myPanel.add(
            layoutRequired,
            GridBagConstraints().apply {
                gridx = 0; gridy = 1; insets = Insets(3, 3, 3, 10); fill = GridBagConstraints.HORIZONTAL
            })

        init()
    }

    override fun createCenterPanel() = myPanel


}