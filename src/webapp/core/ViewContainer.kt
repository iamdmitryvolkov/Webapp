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
        for (view in viewsList) {
            applyStyle(view)
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
            applyStyle(view)
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
            viewsList.add(index, view)
            if (isContentRendered) view.render(element, index)
        }
    }

    /**
     * Returns index of view on container. returns -1 if view is not found
     */
    fun indexOf(view : View) : Int {
        return viewsList.indexOf(view)
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
     */
    abstract fun applyStyle(view : View)

    companion object {

        private const val HIDDEN_OVERFLOW = "hidden"
    }
}