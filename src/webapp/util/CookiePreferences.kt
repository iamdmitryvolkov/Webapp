package webapp.util

import kotlin.browser.document
import kotlin.browser.window

/**
 * Preferences implementation that save data to cookie.
 * This data can be reached after page reloading
 *
 * @param keyPrefix key prefix, that will be automatically add to all keys in cookies
 */
class CookiePreferences(private val keyPrefix: String = "") : BasePreferences() {

    private fun getKey(key: String): String {
        return encode(keyPrefix + key)
    }

    override fun getString(key: String, defaultValue: String?): String? {
        return cookieMap[getKey(key)]?.let { decode(it) } ?: defaultValue
    }

    override fun putString(key: String, value: String) {
        cookieMap.put(getKey(key), encode(value))
        save()
    }

    companion object {

        private const val OBJECTS_SEPARATOR = "; "
        private const val KEY_VALUE_SEPARATOR = "="
        private const val ENCODED_SEPARATOR = "*"

        private val cookieMap = mutableMapOf<String, String>()

        init {
            document.cookie
                    .split(OBJECTS_SEPARATOR)
                    .filter { it.isNotEmpty() }
                    .map { it.split(KEY_VALUE_SEPARATOR) }
                    .forEach { checkDataAndPutToMap(it) }
        }

        private fun encode(data: String) = window.btoa(data).replace(KEY_VALUE_SEPARATOR, ENCODED_SEPARATOR)

        private fun decode(data: String) = window.atob(data.replace(ENCODED_SEPARATOR, KEY_VALUE_SEPARATOR))

        private fun checkDataAndPutToMap(data: List<String>) {
            if (data.size == 2) {
                cookieMap.put(data[0], data[1])
            }
        }

        private fun save() {
            cookieMap.forEach { document.cookie = it.key + KEY_VALUE_SEPARATOR + it.value }
        }
    }
}