package webapp.view

import org.w3c.dom.HTMLElement
import webapp.core.createDiv
import webapp.core.pc
import kotlin.browser.document

/**
 * Class to show text
 */
open class Label(text: String) : TextView(text) {

    /**
     * Text element's factory method.
     */
    override fun createElementWithText() = document.createDiv() as HTMLElement

    /**
     * vertical alignment of text in label
     */
    var verticalAlign = VerticalAlign.CENTER
        set(value) {
            field = value
            applyAlignItems()
        }

    /**
     * Set align items value to parent element
     */
    private fun applyAlignItems() {
        element.style.alignItems = verticalAlign.flexValue
    }

    /**
     * Init block
     */
    init {
        element.style.display = FLEX
        element.style.flexWrap = NO_WRAP
        element.style.flexDirection = ROW
        element.appendChild(textElement)
        textElement.style.width = 100.pc
        applyAlignItems()
        textAlign = TextAlign.CENTER
    }

    /**
     * Describes possible values of vertical text alignment
     */
    enum class VerticalAlign(internal val flexValue: String) {
        START(FLEX_START),
        CENTER(Label.CENTER),
        END(FLEX_END)
    }

    companion object {

        private const val CENTER = "center"
        private const val FLEX = "flex"
        private const val NO_WRAP = "nowrap"
        private const val ROW  = "row"
        private const val FLEX_START = "flex-start"
        private const val FLEX_END = "flex-end"
    }
}