package clean

import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

/**
 * Created by Mohamed Nabil on 28/12/18.
 */
interface CleanPluginIcons {

    val DEMO_ACTION: Icon
        get() = IconLoader.getIcon("/action.png")
}