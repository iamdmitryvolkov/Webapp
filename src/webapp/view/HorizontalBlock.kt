package webapp.view

import webapp.core.View

/**
 * Class to provide horizontal linear layout
 * Uses view's [weight] value to set size of view
 * Uses view's [gravity] value to set position of view
 * Uses [contentGravity] value to set position of all views
 * Uses [alignGravity] value to set position of all views
 *
 * [View]s will split free space (Block width - all view's total width) in relation to [weight] ratio
 */
open class HorizontalBlock : LinearBlock() {

    /**
     * Returns row value as main axis
     */
    override fun getFlexDirectionAxis() = "row"

}