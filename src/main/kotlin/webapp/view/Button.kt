package webapp.view

import org.w3c.dom.HTMLElement
import webapp.core.px
import kotlin.browser.document

/**
 * Button class
 */
open class Button(text: String) : TextView(text) {

    /**
     * Root element's factory method.
     */
    override fun createRootElement() =  document.createElement("button") as HTMLElement

    /**
     * Init block
     */
    init {
        padding = 8.px
    }
}