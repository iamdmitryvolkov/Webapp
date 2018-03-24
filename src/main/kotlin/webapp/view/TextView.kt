package webapp.view

import org.w3c.dom.Element
import webapp.core.View
import webapp.core.px
import webapp.util.Color

/**
 * Class to provide additional text features, using innerText parameter
 *
 * Override [createElementWithText] method to provide logic to different [Element]
 */
abstract class TextView(text: String) : BaseTextView() {

    /**
     * Horizontal alignment of text
     */
    var textAlign = TextAlign.LEFT
        set(value) {
            field = value
            applyTextAlignment()
        }

    /**
     * Text value
     */
    var text: String
        get() = textElement.innerText
        set(value) {
            setText(value)
        }

    /**
     * Sets text to View
     *
     * @param value new text value
     */
    open fun setText(value: String) {
        textElement.innerText = value
    }

    /**
     * Applies text alignment to view
     */
    open fun applyTextAlignment() {
        textElement.style.textAlign = textAlign.cssValue
    }

    /**
     * Init block
     */
    init {
        this.text = text
    }

    /**
     * Enum class to describe horizontal text alignment
     */
    enum class TextAlign(internal val cssValue: String) {
        LEFT(TextView.LEFT),
        CENTER(TextView.CENTER),
        RIGHT(TextView.RIGHT),
        START(TextView.START),
        END(TextView.END)
    }

    companion object {

        private const val LEFT = "left"
        private const val CENTER = "center"
        private const val RIGHT = "right"
        private const val START = "start"
        private const val END = "end"
    }
}