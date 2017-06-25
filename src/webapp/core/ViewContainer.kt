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
    protected var viewsList = mutableListOf<View>()

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
            view.render(element)
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
            // TODO: use insertAfter instead of redraw
            viewsList.add(index, view)
            redraw()
        }
    }

    /**
     * Removes child from container
     */
    fun removeChild(number : Int) {
        if (number >= viewsList.size) throw Exception("View not found")
        val view = viewsList.removeAt(number)
        view.splitFromParent()
    }

    /**
     * Removes child from container
     *
     * Does nothing if child is not found
     */
    fun removeChild(child : View) {
        val index = viewsList.indexOf(child)
        if (index != -1) removeChild(index)
    }

    /**
     * Removes all views from parent
     */
    fun removeAllViews() {
        viewsList.clear()
        redraw()
    }

    /**
     * Returns list of views it container
     */
    fun getSubViewList() : List<View> {
        return viewsList.toList()
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