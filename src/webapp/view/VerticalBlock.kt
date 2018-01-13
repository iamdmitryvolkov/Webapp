package webapp.view

import webapp.core.View

/**
 * Class to provide vertical linear layout
 * Uses view's [weight] value to set size of view
 * Uses view's [gravity] value to set position of view
 * Uses [contentGravity] value to set position of all views
 * Uses [alignGravity] value to set position of all views
 *
 * [View]s will split free space (Block height - all view's total height) in relation to [weight] ratio
 */
open class VerticalBlock : LinearBlock() {

    /**
     * Returns column value as main axis
     */
    override fun getFlexDirectionAxis() = "column"
}