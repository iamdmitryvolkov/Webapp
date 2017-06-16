package webapp.view

import org.w3c.dom.Element
import webapp.core.View
import webapp.core.px
import webapp.util.Color

/**
 * Base class to provide text features in view
 *
 * Override [createElementWithText] method to provide logic to different [Element]
 */
abstract class BaseTextView() : View() {

    /**
     * Element, that has text managed by View
     */
    protected val textElement = createElementWithText()

    /**
     * Text element's factory method.
     */
    open protected fun createElementWithText() = element

    /**
     * Color of text in view
     */
    var textColor : String
        get() = textElement.style.color
        set(value) {
            setTextColor(value)
        }

    /**
     * Size of text in View
     */
    var textSize: String
        get() = element.style.fontSize
        set(value) {
            setTextSize(value)
        }

    /**
     * Sets color of text in View
     *
     * @param value new text color value
     */
    open fun setTextColor(value : String) {
        textElement.style.color = value
    }

    /**
     * Sets size of text in View
     *
     * @param value new text size value
     */
    fun setTextSize(value : Int) {
        setTextSize(value.px)
    }

    /**
     * Sets size of text in View
     *
     * @param value new text size value
     */
    open fun setTextSize(value : String) {
        element.style.fontSize = value
    }

    /**
     * Init block
     */
    init {
        textColor = Color.getTextColor(Color.GREY_50)
        textSize = DEFAULT_TEXT_SIZE
    }


    companion object {

        private const val DEFAULT_TEXT_SIZE = "16px"

    }

}