package webapp.core

import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import org.w3c.dom.css.CSSStyleDeclaration
import org.w3c.dom.events.Event
import kotlin.browser.document
import kotlin.dom.clear

/**
 * Element with which user can works
 * You need to override [renderContent] method if you extends it
 * also you can override [render] method, but in normal case you shouldn't do it
 */
open class View {

    /**
     * Link to root [HTMLElement] for this [View]
     */
    protected val element = createRootElement()

    /**
     * Root element's factory method.
     */
    open protected fun createRootElement() = document.createDiv() as HTMLElement

    /**
     * Background of this View
     *
     * Can be url on image
     * if you try to set value like "http:/example.org/image.jpg"
     * then url(http:/example.org/image.jpg) will be set automatically
     */
    var background : String
        get() {
            var result = element.style.background
            if (result.isNotEmpty()) return result
            result = element.style.backgroundImage
            if (result.isNotEmpty()) return result
            // else
            return element.style.backgroundColor
        }
        set(value) {
            setBackground(value)
            element.style.backgroundPosition = CENTER
        }

    /**
     * Alpha of this View
     */
    var alpha : String
        get() = element.style.opacity
        set(value) { setAlpha(value) }

    /**
     * Width of this View
     */
    var width : String
        get() = element.style.width
        set(value) { setWidth(value) }

    /**
     * Height of this View
     */
    var height : String
        get() = element.style.height
        set(value) { setHeight(value) }

    /**
     * Gravity value holder
     */
    private var gravityHolder = Gravity.NOT_SET

    /**
     * Gravity of image
     * Used by some [ViewContainer]
     */
    var gravity : Gravity
        get() = gravityHolder
        set(value) { setGravity(value)}

    /**
     * Weight value holder
     */
    private var weightHolder = 0

    /**
     * Gravity of image
     * Used by some [ViewContainer]
     */
    var weight : Int
        get() = weightHolder
        set(value) { setWeight(value)}

    /**
     * Scale mode of image on background
     */
    var backgroundImageScale : ImageScale = ImageScale.STRETCH
        set(value) {
            field = value
            applyBackgroundImageScale()
        }

    /**
     * Padding of this View
     */
    var padding : String
        get() = element.style.padding
        set(value) { setPadding(value) }

    /**
     * Margin of this View
     */
    var margin : String
        get() = element.style.margin
        set(value) { setMargin(value) }

    /**
     * Click listener for View
     */
    var onClickListener : ((Event) -> dynamic)?
        get() = element.onclick
        set(value) { setOnClickListener(value)}

    /**
     * Render function of [View] to target [Element]
     * should just add view to parent, and cause redrawing of it
     *
     * @see redraw
     *
     * @param parent - parent element, in which View should be rendered to
     */
    open fun render(parent : Element) {
        parent.appendChild(element)
        renderContent()
    }

    /**
     * Renders content in [element]
     * Called when element should be redrawed
     */
    open fun renderContent() {
        applyBackgroundImageScale()
    }

    /**
     * Causes redrawing view
     */
    fun redraw() {
        element.clear()
        renderContent()
    }

    /**
     * Sets property to style of this view
     *
     * Note wrong value may be filtered by system
     *
     * @param property - property name
     * @param value - property value to set
     */
    fun setStyleProperty(property : String, value : String) {
        element.style.setProperty(property, value)
    }

    /**
     * Gets value of style property
     *
     * @param property property name
     */
    fun getStyleProperty(property: String) : String {
        return element.style.getPropertyValue(property)
    }

    /**
     * Gets CSS style
     */
    fun getStyle() : CSSStyleDeclaration = element.style

    /**
     * Sets background value
     *
     * Can be url on image
     * if you try to set value like "http:/example.org/image.jpg"
     * then url(http:/example.org/image.jpg) will be set automatically
     *
     * @param value value of background
     */
    open fun setBackground(value : String) {
        if (value.isUrl()) {
            element.style.background = value.wrapUrl()
        } else {
            element.style.background = value
        }
    }

    /**
     * Sets width
     *
     * @param value new width value
     */
    fun setWidth(value : Int) {
        setWidth(value.px)
    }

    /**
     * Sets width
     *
     * @param value new width value
     */
    open fun setWidth(value : String) {
        element.style.width = value
    }

    /**
     * Sets height
     *
     * @param value new height value
     */
    fun setHeight(value : Int) {
        setHeight(value.px)
    }

    /**
     * Sets height
     *
     * @param value new height value
     */
    open fun setHeight(value : String) {
        element.style.height = value
    }

    /**
     * Sets weight of element
     * Used by some [ViewContainer]
     *
     * @param value new weight value
     */
    open fun setWeight(value : Int) {
        weightHolder = value
    }

    /**
     * Sets gravity of element
     * Used by some [ViewContainer]
     *
     * @param value new gravity value
     */
    open fun setGravity(value : Gravity) {
        gravityHolder = value
    }

    /**
     * Sets alpha. 0 - transparent 1.0 - visible
     *
     * @param value new alpha value
     */
    fun setAlpha(value : Float) {
        setAlpha(value.toString())
    }

    /**
     * Sets alpha. 0 - transparent 1.0 - visible
     *
     * @param value new alpha value
     */
    open fun setAlpha(value : String) {
        element.style.opacity = value
    }

    /**
     * Sets padding for [View]
     *
     * @param value new padding value
     */
    fun setPadding(value : Int) {
        setPadding(value.px)
    }

    /**
     * Sets padding for [View]
     *
     * @param value new padding value
     */
    open fun setPadding(value : String) {
        element.style.padding = value
    }

    /**
     * Sets padding for each size of [View]
     *
     * @param top new top padding value
     * @param bottom new bottom padding value
     * @param left new left padding value
     * @param right new right padding value
     */
    fun setPadding(top : Int, bottom : Int, left : Int, right : Int) {
        setPadding(top.px, bottom.px, left.px, right.px)
    }

    /**
     * Sets padding for each size of [View]
     *
     * @param top new top padding value
     * @param bottom new bottom padding value
     * @param left new left padding value
     * @param right new right padding value
     */
    open fun setPadding(top : String, bottom : String, left : String, right : String) {
        element.style.paddingTop = top
        element.style.paddingBottom = bottom
        element.style.paddingLeft = left
        element.style.paddingRight = right
    }

    /**
     * Sets margin for [View]
     *
     * @param value new margin value
     */
    fun setMargin(value : Int) {
        setMargin(value.px)
    }

    /**
     * Sets margin for [View]
     *
     * @param value new margin value
     */
    open fun setMargin(value : String) {
        element.style.margin = value
    }

    /**
     * Sets margin for each size of [View]
     *
     * @param top new top margin value
     * @param bottom new bottom margin value
     * @param left new left margin value
     * @param right new right margin value
     */
    fun setMargin(top : Int, bottom : Int, left : Int, right : Int) {
        setMargin(top.px, bottom.px, left.px, right.px)
    }

    /**
     * Sets margin for each size of [View]
     *
     * @param top new top margin value
     * @param bottom new bottom margin value
     * @param left new left margin value
     * @param right new right margin value
     */
    open fun setMargin(top : String, bottom : String, left : String, right : String) {
        element.style.marginTop = top
        element.style.marginBottom = bottom
        element.style.marginLeft = left
        element.style.marginRight = right
    }

    /**
     * Applies scale mode to background image
     */
    open fun applyBackgroundImageScale() {
        element.style.backgroundSize = backgroundImageScale.backgroundSizeValue
        element.style.backgroundRepeat = backgroundImageScale.backgroundRepeatValue
    }

    /**
     * Sets onClickListener
     *
     * @param value new onClickListener value
     */
    open fun setOnClickListener(value : ((Event) -> dynamic)?) {
        element.onclick = value
    }

    /**
     * Gravity value for view. Can be used by some [ViewContainer]
     */
    enum class Gravity {
        START,
        END,
        CENTER,
        NOT_SET
    }

    /**
     * Describes scale mode for image
     */
    enum class ImageScale(internal val backgroundSizeValue : String, internal  val backgroundRepeatValue : String) {
        STRETCH(COVER, ROUND),
        FIT(CONTAIN, NO_REPEAT)
    }

    companion object {
        const val WRAP_CONTENT = ""
        const val MATCH_PARENT = "100%"

        private const val COVER = "cover"
        private const val CONTAIN = "contain"
        private const val ROUND = "round"
        private const val NO_REPEAT = "no-repeat"
        private const val CENTER = "center"
    }
}