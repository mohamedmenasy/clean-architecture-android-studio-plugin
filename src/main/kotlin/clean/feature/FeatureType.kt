package clean.feature

/**
 * Created by Mohamed Nabil on 28/12/18.
 */
enum class FeatureType {

    ACTIVITY {
        override fun getTemplate(sceneName: String): FeatureTemplate =
            FeatureTemplate.Activity(sceneName)
    },
    FRAGMENT {
        override fun getTemplate(sceneName: String): FeatureTemplate =
            FeatureTemplate.Fragment(sceneName)

    },
    LAYOUT {
        override fun getTemplate(sceneName: String): FeatureTemplate =
            FeatureTemplate.Layout(sceneName)

    },
    VIEW_MODEL {
        override fun getTemplate(sceneName: String): FeatureTemplate =
            FeatureTemplate.ViewModel(sceneName)
    };


    abstract fun getTemplate(sceneName: String): FeatureTemplate


    fun isSourceFile(): Boolean = this != LAYOUT

}
