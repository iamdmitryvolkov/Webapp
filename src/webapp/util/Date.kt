package webapp.util

import kotlin.js.Date

@Suppress("UNUSED_PARAMETER", "UNUSED_VARIABLE")
fun Date.setTime(time: Double) {
    val date = this
    js("date.setTime(time)")
}

@Suppress("UNUSED_VARIABLE")
fun Date.toUTCString(): String {
    val date = this
    return js("date.toUTCString()").toString()
}