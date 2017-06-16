package webapp.core

/**
 * Base class for implementing layouts with views
 * override [applyStyle] function to specify layout
 */
abstract class ViewContainer : View() {

    /**
     * Shows is content rendered yet
     */
    private var isContentRendered = false

    /**
     * List of Views in container
     */
    var viewsList = mutableListOf<View>()

    /**
     * Render all views to target view
     */
    override fun renderContent() {
        super.renderContent()
        for ((i, view) in viewsList.withIndex()) {
            applyStyle(view, i)
            view.render(element)
        }
        isContentRendered = true
    }

    /**
     * Add view to container
     *
     * @param view View to add
     */
    fun append(view : View) {
        viewsList.add(view)
        if (isContentRendered) {
            applyStyle(view, viewsList.size)
        }
    }

    /**
     * Add view to container with index
     *
     * @param view View to add
     * @param index number of view in list
     */
    fun add(view : View, index : Int = viewsList.size) {
        if (index == viewsList.size) {
            append(view)
        } else {
            viewsList.add(index, view)
            redraw()
        }
    }

    /**
     * Init block
     */
    init {
        element.style.overflowX = HIDDEN_OVERFLOW
        element.style.overflowY = HIDDEN_OVERFLOW
    }

    /**
     * Applies specific for layout style to view
     *
     * @param view View to apply style
     * @param i number of view in list
     */
    abstract fun applyStyle(view : View, i : Int)

    companion object {

        private const val HIDDEN_OVERFLOW = "hidden"
    }
}