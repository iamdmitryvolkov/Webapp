package webapp.view

import webapp.core.View


/**
 * Class to implement scrollable content into view
 *
 * You should set [childView], that will scrolls into ScrollView
 */
class ScrollView(child: View? = null) : ComplexView() {

    /**
     * Child view, visible inside ScrollView
     */
    var childView: View?
    get() = subView
    set(value) {
        subView = value
    }

    /**
     * Describes, can content scroll horizontally
     */
    var canScrollHorizontally = true
    set(value) {
        field = value
        applyScrollX()
    }

    /**
     * Describes, can content scroll vertically
     */
    var canScrollVertically = true
        set(value) {
            field = value
            applyScrollY()
        }

    /**
     * Applies horizontal scrolling properties
     */
    private fun applyScrollX() {
        element.style.overflowX = if (canScrollHorizontally) SCROLLABLE_OVERFLOW else HIDDEN_OVERFLOW
    }

    /**
     * Applies vertical scrolling properties
     */
    private fun applyScrollY() {
        element.style.overflowY = if (canScrollVertically) SCROLLABLE_OVERFLOW else HIDDEN_OVERFLOW
    }

    /**
     * Init block
     */
    init {
        applyScrollX()
        applyScrollY()
        childView = child
    }

    companion object {

        private const val HIDDEN_OVERFLOW = "hidden"
        private const val SCROLLABLE_OVERFLOW = "scroll"
    }
}
