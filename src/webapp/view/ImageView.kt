package webapp.view

import webapp.core.View
import webapp.core.isColor
import webapp.core.isUrl
import webapp.core.wrapUrl

/**
 * Class to show images
 *
 * Can show [src] (Image or color), [backgroundImage] and [backgroundColor]
 *
 * Provide additional methods to set image parameters
 *
 * [background] property will automatically set [backgroundColor] or [backgroundImage]
 */
open class ImageView(src : String = ImageView.NOTHING) : ComplexView() {

    /**
     * Image source
     */
    var src : String
        get() {
            return subView?.background ?: NOTHING
        }
        set(value) {
            setImageSource(value)
        }

    /**
     * Background color
     */
    var backgroundColor : String
        get() = element.style.backgroundColor
        set(value) {
            setBackgroundColor(value)
            element.style.backgroundPosition = CENTER
        }

    /**
     * Background image
     */
    var backgroundImage : String
        get() = element.style.backgroundImage
        set(value) {
            setBackgroundImage(value)
            element.style.backgroundPosition = CENTER
        }

    /**
     * Scale mode of image on background
     */
    var imageScale : ImageScale = ImageScale.STRETCH
        set(value) {
            field = value
            applyImageScale()
        }

    /**
     * Sets background image or color property by value
     */
    override fun setBackground(value: String) {
        if (value.isEmpty()) {
            super.setBackground(value)
        }
        if (value.isColor()) {
            setBackgroundColor(value)
        } else {
            setBackgroundImage(value)
        }
    }

    /**
     * Sets background color
     */
    open fun setBackgroundColor(value: String) {
        if (value.isUrl()) return
        element.style.backgroundColor = value
    }

    /**
     * Sets background image
     */
    open fun setBackgroundImage(value: String) {
        val pValue = if (value.isUrl()) value.wrapUrl() else value
        element.style.backgroundImage = pValue
    }

    /**
     * Applies scale mode to image
     */
    open fun applyImageScale() {
        subView?.let { applyImageScale(it) }
    }

    /**
     * Sets imageScale for view
     */
    private fun applyImageScale(view : View) {
        view.backgroundImageScale = imageScale
    }

    /**
     * Sets source image to view
     */
    open fun setImageSource(value: String) {
        if (value == NOTHING) {
            subView = null
        } else {
            val imageView = View()
            imageView.width = MATCH_PARENT
            imageView.height = MATCH_PARENT
            imageView.background = value
            applyImageScale(imageView)
            subView = imageView
        }
    }

    /**
     * Init block
     */
    init {
        this.src = src
    }

    companion object {

        private const val NOTHING = ""
        private const val CENTER = "center"

    }

}