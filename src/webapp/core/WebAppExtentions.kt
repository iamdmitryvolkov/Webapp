package webapp.core

import org.w3c.dom.Document
import org.w3c.dom.HTMLDivElement
import webapp.util.Color

/**
 * Creates a new [HTMLDivElement]
 */
fun Document.createDiv() = this.createElement("div")

/**
 * Gets pixel value of Int
 */
val Int.px
get() = toString() + "px"

/**
 * Gets percent value of Int
 */
val Int.pc
    get() = toString() + "%"

/**
 * Gets percent  value of Int relative to viewport's width
 */
val Int.vw
    get() = toString() + "vw"


/**
 * Gets percent value of Int relative to viewport's height
*/
val Int.vh
get() = toString() + "vh"


/**
 * Gets percent value of Int relative to viewport's width's and height's minimum
 */
val Int.vmin
    get() = toString() + "vmin"


/**
 * Gets percent value of Int relative to viewport's width's and height's maximum
 */
val Int.vmax
    get() = toString() + "vmax"

/**
 * Checks string is url
 */
fun String.isUrl() = this.matches("^https?://")

/**
 * Wraps string into url()
 */
fun String.wrapUrl() = "url($this)"

/**
 * Checks string is color
 */
fun String.isColor() = Color.isColor(this)

/**
 * Wraps collection as string without spaces
 */
fun <T> Collection<T>.asString() : String = fold("", { s, i -> s + i.toString() })