package webapp.view

import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLInputElement
import webapp.core.px
import kotlin.browser.document

/**
 * Class to provide text input from user
 */
open class Input(text : String= "") : BaseTextView() {

    /**
     * Root element's factory method.
     */
    override fun createRootElement()= document.createElement("input") as HTMLElement

    /**
     * Text in input
     */
    var value : String
    get() = (element as HTMLInputElement).value
    set(value) {
        setValue(value)
    }

    /**
     * Sets text to input
     */
    open fun setValue(value : String) {
        (element as HTMLInputElement).value = value
    }

    /**
     * Init block
     */
    init {
        value = text
        padding = DEFAULT_PADDING
        // TODO: provide logic to set border
        element.style.borderStyle = NO_BORDER
        element.style.borderWidth = 0.px
    }

    companion object {

        private const val DEFAULT_PADDING = "6px"

        private const val NO_BORDER = "none"

    }

}