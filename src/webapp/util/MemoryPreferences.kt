package webapp.util

/**
 * Preferences implementation that saves data in memory.
 * All saved data to this preferences will be lost after page reload
 */
class MemoryPreferences : BasePreferences() {

    private val dataMap = mutableMapOf<String, String>()

    override fun putString(key: String, value: String) {
        dataMap.put(key, value)
    }

    override fun getString(key: String, defaultValue: String?): String? {
        return dataMap[key] ?: defaultValue
    }
}