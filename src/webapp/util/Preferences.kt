package webapp.util

/**
 * Defines a key-value store for data primitives
 */
interface Preferences {

    fun putString(key: String, value: String)

    fun putInt(key: String, value: Int)

    fun putLong(key: String, value: Long)

    fun putFloat(key: String, value: Float)

    fun putDouble(key: String, value: Double)

    fun getString(key: String, defaultValue: String? = null) : String?

    fun getInt(key: String, defaultValue: Int? = null) : Int?

    fun getLong(key: String, defaultValue: Long? = null) : Long?

    fun getFloat(key: String, defaultValue: Float? = null) : Float?

    fun getDouble(key: String, defaultValue: Double? = null) : Double?
}