package webapp.view

import webapp.core.View
import webapp.core.ViewContainer

/**
 * Base class for linear layout. Uses Flexbox to control elements inside
 * Uses view's [weight] value to set size of view
 * Uses view's [gravity] value to set position of view
 * Uses [contentGravity] value to set position of all views
 * Uses [alignGravity] value to set position of all views
 *
 * [View]s will split free space (block size - all view's total size) in relation to [weight] ratio
 */
abstract class LinearBlock : ViewContainer() {

    /**
     * True means that subviews will be rendered in reversed order
     */
    var reversed : Boolean = false
        set(value) {
            field = value
            applyFlexDirection()
            applyJustifyContent()
        }

    /**
     * Content gravity of child views
     */
    var contentGravity = ContentGravity.START
        set(value) {
            field = value
            applyJustifyContent()
        }

    /**
     * Align gravity of child views
     */
    var alignGravity = AlignGravity.START
        set(value) {
            field = value
            applyAlignGravityValue()
        }

    /**
     * Gets direction of primary axis
     *
     * @return String name of primary axis
     */
    abstract fun getFlexDirectionAxis() : String

    /**
     * Applies align items value to [element]
     */
    private fun applyAlignGravityValue() {
        element.style.alignItems = alignGravity.flexValue
    }

    /**
     * Applies flex direction value to [element]
     */
    private fun applyFlexDirection() {
        var direction = getFlexDirectionAxis()
        if (reversed) direction += REVERSE_SUFFIX
        element.style.flexDirection = direction
    }

    /**
     * Applies justify content to [element]
     */
    private fun applyJustifyContent() {
        element.style.justifyContent = contentGravity.getJustifyContentValue(reversed)
    }

    /**
     * Applies style values to child views
     *
     * @param view View to apply style
     */
    override fun applyStyle(view: View) {
        // Weight
        view.getStyle().flexGrow = view.weight.toString()
        // Gravity
        view.getStyle().alignSelf = AlignGravity.getByGravity(view.gravity).flexValue
    }

    init {
        element.style.display = FLEX
        element.style.flexWrap= NO_WRAP
        applyJustifyContent()
        applyFlexDirection()
        applyAlignGravityValue()
    }

    /**
     * Gravity for content in primary axis
     *
     * @param flexValue string value to set into justify-content style for this gravity
     */
    enum class ContentGravity(internal val flexValue : String) {
        START(FLEX_START),
        END(FLEX_END),
        CENTER(LinearBlock.CENTER),
        SPACE_BETWEEN(LinearBlock.SPACE_BETWEEN),
        SPACE_AROUND(LinearBlock.SPACE_AROUND);

        /**
         * Gets value, that should be set to justify-content style
         *
         * @param isReversed true if reverse action is active
         * @return string value to set into style
         */
        internal fun getJustifyContentValue(isReversed : Boolean) : String {
            var value = this
            if (isReversed) {
                when (this) {
                    START -> value = END
                    END -> value = START
                    else -> {}
                }
            }

            return value.flexValue
        }
    }

    /**
     * Gravity for content in secondary axis
     *
     * @param flexValue string value to set into align style for this gravity
     */
    enum class AlignGravity(internal val flexValue : String) {
        START(FLEX_START),
        END(FLEX_END),
        CENTER(LinearBlock.CENTER),
        NOT_SET(NOTHING);

        companion object {

            /**
             * Returns [AlignGravity] for [View.Gravity] value in View
             *
             * @param gravity [View.Gravity] value for View
             * @return equal value of [AlignGravity]
             */
            fun getByGravity(gravity : Gravity) = when (gravity) {
                Gravity.START -> START
                Gravity.END -> END
                Gravity.CENTER -> CENTER
                Gravity.NOT_SET -> NOT_SET
            }
        }

    }

    companion object {

        private const val REVERSE_SUFFIX = "-reverse"
        private const val FLEX = "flex"
        private const val NO_WRAP = "nowrap"
        private const val FLEX_START = "flex-start"
        private const val FLEX_END = "flex-end"
        private const val CENTER = "center"
        private const val SPACE_BETWEEN = "space-between"
        private const val SPACE_AROUND = "space-around"
        private const val NOTHING = ""

    }

}