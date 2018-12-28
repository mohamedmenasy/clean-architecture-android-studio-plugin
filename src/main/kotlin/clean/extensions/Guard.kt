package clean.extensions

/**
 * Created by Mohamed Nabil on 28/12/18.
 */
inline fun <T> T?.guard(block: () -> Nothing): T {
    if (this == null || this == false) block(); return this
}
