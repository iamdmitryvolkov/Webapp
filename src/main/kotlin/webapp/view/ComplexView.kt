package webapp.view

import webapp.core.View

/**
 * Class to provide constructing view using another views
 */
abstract class ComplexView : View() {

    /**
     * View, visible inside ComplexView
     */
    protected var subView: View? = null
        set(value) {
            field = value
            redraw()
        }

    /**
     * Renders child into [element]
     * Called when element should be redrawed
     */
    override fun renderContent() {
        super.renderContent()
        subView?.render(element)
    }
}