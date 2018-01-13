package webapp.util

/**
 * Preferences implementation, based on wrapping data to string
 */
abstract class BasePreferences : Preferences {

    override fun putInt(key: String, value: Int) {
        putString(key, value.toString())
    }

    override fun putLong(key: String, value: Long) {
        putString(key, value.toString())
    }

    override fun putFloat(key: String, value: Float) {
        putString(key, value.toString())
    }

    override fun putDouble(key: String, value: Double) {
        putString(key, value.toString())
    }

    override fun getInt(key: String, defaultValue: Int?): Int? {
        return getString(key)?.toInt() ?: defaultValue
    }

    override fun getLong(key: String, defaultValue: Long?): Long? {
        return getString(key)?.toLong() ?: defaultValue
    }

    override fun getFloat(key: String, defaultValue: Float?): Float? {
        return getString(key)?.toFloat() ?: defaultValue
    }

    override fun getDouble(key: String, defaultValue: Double?): Double? {
        return getString(key)?.toDouble() ?: defaultValue
    }
}