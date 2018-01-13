package webapp.util

import kotlin.browser.document
import kotlin.browser.window

/**
 * Preferences implementation that save data to cookie.
 * This data can be reached after page reloading
 *
 * @param keyPrefix key prefix, that will be automatically add to all keys in cookies
 */
class CookiePreferences(private val keyPrefix : String = "") : BasePreferences() {

    private fun getKey(key: String) : String {
        return window.btoa(keyPrefix + key)
    }

    override fun getString(key: String, defaultValue: String?): String? {
        return cookieMap[getKey(key)]?.let { window.atob(it) } ?: defaultValue
    }

    override fun putString(key: String, value: String) {
        cookieMap.put(getKey(key), window.btoa(value))
        save()
    }

    companion object {

        private const val OBJECTS_SEPARATOR = "; "

        private const val KEY_VALUE_SEPARATOR = "="

        private val cookieMap = mutableMapOf<String, String>()

        init {
            document.cookie
                    .split(OBJECTS_SEPARATOR)
                    .filter { it.isNotEmpty() }
                    .map { it.split(KEY_VALUE_SEPARATOR) }
                    .forEach { cookieMap.put(it[0], it[1]) }
        }

        private fun save() {
            cookieMap.forEach { document.cookie = it.key + KEY_VALUE_SEPARATOR + it.value }
        }
    }

}