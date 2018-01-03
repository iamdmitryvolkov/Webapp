package webapp.view

import webapp.core.View
import webapp.core.ViewContainer

/**
 * Simple ViewContainer to provide Views' overlapping
 * Use View's [gravity] as [primaryGravity]
 */
open class Frame : ViewContainer() {

    /**
     * Direction of primary axis
     */
    var primaryGravityDirection = PrimaryGravityDirection.HORIZONTAL
        set(value) {
            field = value
            applyFlexDirection()
        }

    /**
     * Primary axis gravity
     */
    var primaryGravity = PrimaryGravity.START
        set(value) {
            field = value
            applyAlignItemsValue()
        }

    /**
     * Content gravity of child views
     */
    var secondaryGravity = SecondaryGravity.START
        set(value) {
            field = value
            applyJustifyContent()
        }

    /**
     * Applies specific for layout style to view
     *
     * @param view View to apply style
     */
    override fun applyStyle(view: View) {
        view.getStyle().position = ABSOLUTE
        view.getStyle().alignSelf = PrimaryGravity.getByGravity(view.gravity).flexValue
    }

    /**
     * Applies align items value to [element]
     */
    private fun applyAlignItemsValue() {
        element.style.alignItems = primaryGravity.flexValue
    }

    /**
     * Applies justify content to [element]
     */
    private fun applyJustifyContent() {
        element.style.justifyContent = secondaryGravity.flexValue
    }

    /**
     * Applies flex direction value to [element]
     */
    private fun applyFlexDirection() {
        element.style.flexDirection = primaryGravityDirection.flexValue
    }

    /**
     * Init block
     */
    init {
        element.style.display = FLEX
        element.style.flexDirection = COLUMN
        element.style.flexWrap= NO_WRAP
        element.style.position = RELATIVE
    }

    /**
     * Gravity for content in secondary axis
     *
     * @param flexValue string value to set into align style for this gravity
     */
    enum class PrimaryGravityDirection(internal val flexValue : String) {
        HORIZONTAL(COLUMN),
        VERTICAL(ROW);
    }

    /**
     * Gravity for content in secondary axis
     *
     * @param flexValue string value to set into align style for this gravity
     */
    enum class PrimaryGravity(internal val flexValue : String) {
        START(FLEX_START),
        END(FLEX_END),
        CENTER(Frame.CENTER),
        NOT_SET(NOTHING);

        companion object {

            /**
             * Returns [PrimaryGravity] for [View.Gravity] value in View
             *
             * @param gravity [View.Gravity] value for View
             * @return equal value of [PrimaryGravity]
             */
            fun getByGravity(gravity : Gravity) = when (gravity) {
                Gravity.START -> START
                Gravity.END -> END
                Gravity.CENTER -> CENTER
                Gravity.NOT_SET -> NOT_SET
            }
        }

    }

    /**
     * Gravity for content in primary axis
     *
     * @param flexValue string value to set into justify-content style for this gravity
     */
    enum class SecondaryGravity(internal val flexValue : String) {
        START(FLEX_START),
        END(FLEX_END),
        CENTER(Frame.CENTER);
    }

    companion object {
        private const val RELATIVE = "relative"
        private const val ABSOLUTE = "absolute"
        private const val FLEX = "flex"
        private const val ROW  = "row"
        private const val COLUMN  = "column"
        private const val NO_WRAP = "nowrap"
        private const val FLEX_START = "flex-start"
        private const val FLEX_END = "flex-end"
        private const val CENTER = "center"
        private const val NOTHING = ""
    }

}