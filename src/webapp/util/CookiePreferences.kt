package webapp.util

import kotlin.browser.document
import kotlin.browser.window
import kotlin.js.Date

/**
 * Preferences implementation that save data to cookie.
 * This data can be reached after page reloading
 *
 * Cookies additionally protected from collision by using BASE64 algorithm
 * After initializing updates expire date of all matching cookies to target
 *
 * @param keyPrefix key prefix, that will be automatically add to all keys in cookies
 * @param cookiesExpirePolicy policy of selection cookie expire date
 *
 * @see CookiesExpirePolicy
 * @see BrowserSessionExpirePolicy
 * @see MinutesExpirePolicy
 * @see HoursExpirePolicy
 * @see DaysExpirePolicy
 */
class CookiePreferences(private val keyPrefix: String = "",
                        private val cookiesExpirePolicy : CookiesExpirePolicy = BrowserSessionExpirePolicy()
) : BasePreferences() {

    init {
        updateCookieExpirationDate()
    }

    private fun getKey(key: String): String {
        return encode(keyPrefix + key)
    }

    override fun getString(key: String, defaultValue: String?): String? {
        return cookieMap[getKey(key)]?.let { decode(it) } ?: defaultValue
    }

    override fun putString(key: String, value: String) {
        val targetKey = getKey(key)
        cookieMap.put(targetKey, encode(value))
        save(targetKey, cookiesExpirePolicy.getCookieExpireDate())
    }

    /**
     * Update expiration date of all cookies to target
     */
    fun updateCookieExpirationDate() {
        saveAll(keyPrefix, cookiesExpirePolicy.getCookieExpireDate())
    }

    /**
     * Defines cookie expire date
     */
    interface CookiesExpirePolicy {

        /**
         * Returns [Date] of cookies expiration
         * null means default cookies expiration
         */
        fun getCookieExpireDate(): Date?
    }

    /**
     * Defines default expire policy (Until browser will be closed)
     */
    class BrowserSessionExpirePolicy : CookiesExpirePolicy {

        override fun getCookieExpireDate(): Date? = null
    }

    /**
     * Defines expire policy in minutes
     */
    open class MinutesExpirePolicy(private val minutes: Long) : CookiesExpirePolicy {

        override fun getCookieExpireDate(): Date? = Date().also {
            val currentTime = it.getTime()
            val targetTime = currentTime + minutes * MILLIS_IN_MINUTE
            it.setTime(targetTime)
        }
    }

    /**
     * Defines expire policy in hours
     */
    open class HoursExpirePolicy(hours: Long) : MinutesExpirePolicy(hours * MINUTES_IN_HOUR)

    /**
     * Defines expire policy in days
     */
    class DaysExpirePolicy(days: Long) : HoursExpirePolicy(days * HOURS_IN_DAY)

    companion object {

        private const val HOURS_IN_DAY = 24
        private const val MINUTES_IN_HOUR = 60
        private const val MILLIS_IN_MINUTE = 1000 * 60
        private const val OBJECTS_SEPARATOR = "; "
        private const val KEY_VALUE_SEPARATOR = "="
        private const val ENCODED_SEPARATOR = "*"
        private const val EXPIRES = "expires"

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

        private fun saveAll(prefix: String, date: Date? = null) {
            cookieMap.keys.filter { decode(it).startsWith(prefix) }
                    .forEach { save(it, date) }
        }

        private fun save(key: String, date: Date? = null) {
            val value = cookieMap[key]
            val cookie = key + KEY_VALUE_SEPARATOR + value
            document.cookie = appendExpirationDate(cookie, date)
        }

        private fun appendExpirationDate(value: String, date: Date?) : String {
            var result = value
            if (date != null) {
                result += OBJECTS_SEPARATOR + EXPIRES + KEY_VALUE_SEPARATOR + date.toUTCString()
            }
            return result
        }
    }
}