package webapp.core

import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import webapp.util.Color
import kotlin.browser.document
import webapp.view.TopBar
import kotlin.dom.clear

/**
 * Main page class. Page provide window for application,
 * top-level controls, like [TopBar], functionality for base app functions
 *
 * Override some open methods to create you own page
 */
open class Page(protected val app: Application, title: String = NOTHING) : CoreFeatures {

    /**
     * Current state of [Page]
     */
    private var state = State.NOT_INITIALIZED

    /**
     * True if [Page] is in focus now
     */
    private var isFocused = false

    /**
     * [TopBar] of [Page]
     */
    protected val topBar = TopBar()

    init {
        topBar.title = if (title.isEmpty()) this::class.simpleName!! else title
    }

    /**
     * Link to root view of content of [Page]
     * null-value means no content
     */
    protected var contentView: View? = null
    set(value) {
        field = value
        if (state == State.LOADED) {
            redraw()
        }
    }

    /**
     * Link to root [Element] of content of [Page]
     */
    private var contentElement: HTMLElement = document.createDiv() as HTMLElement

    /**
     * Root [Element] of [Page]
     */
    private val rootElement: HTMLElement = document.createDiv() as HTMLElement

    /**
     * Internal function to load page
     * NOTE: Method should be called by system
     */
    fun load() {
        if (state != State.NOT_INITIALIZED || !app.isWaitingForPageLifecycle) {
            throw IllegalStateException("load() method must be called by system")
        }
        state = State.LOADING
        onLoad()

        render(app.rootElement)
        onLoaded()
        if (isFocused) onGetFocus()
        state = State.LOADED
    }

    /**
     * Internal function to destroy page
     * NOTE: Method should be called by system
     */
    fun destroy() {
        if (state != State.LOADED || !app.isWaitingForPageLifecycle) {
            throw IllegalStateException("destroy() method must be called by system")
        }
        state = State.DESTROYING
        if (isFocused) onLostFocus()
        onDestroy()
        state = State.DESTROYED

    }

    /**
     * Called by system to notify app about focus changing events
     * NOTE: Method should be called by system
     *
     * @param newState new state of focus
     */
    fun notifyFocusChanged(newState: Boolean) {
        if (state == State.LOADED && newState != isFocused) {
            if (isFocused) {
                onGetFocus()
            } else {
                onLostFocus()
            }
        }
        isFocused = newState
    }

    /**
     * Renders [Page] into [parent]
     * should just add view to parent, and cause drawing content
     *
     * @param parent - parent element, in which View should be rendered to
     */
    private fun render(parent: Element) {
        parent.appendChild(rootElement)
        topBar.render(rootElement)
        rootElement.appendChild(contentElement)
        renderContent()
    }

    /**
     * Renders [contentView] into [contentElement]
     */
    private fun renderContent() = contentView?.render(contentElement)

    /**
     * Causes redrawing Page
     */
    fun redraw() {
        contentElement.clear()
        renderContent()
    }

    /**
     * Called when user resize window
     * NOTE: Method should be called by system
     */
    open fun onWindowSizeChanged() {}

    /**
     * Called before [Page] starts loading
     * UI still uninitialized
     * NOTE: Method should be called by system
     */
    open protected fun onLoad() {
    }

    /**
     * Called when [Page] finish loading
     * UI is initialized
     * NOTE: Method should be called by system
     */
    open protected fun onLoaded() {}

    /**
     * Called when [Page] get focus
     * NOTE: Method should be called by system
     */
    open protected fun onGetFocus() {}

    /**
     * Called when [Page] lost focus
     * NOTE: Method should be called by system
     */
    open protected fun onLostFocus() {}

    /**
     * Called when [Page] starts destroying
     * UI still initialized
     * NOTE: Method should be called by system
     */
    open protected fun onDestroy() {}

    /**
     * Shows a new [Page] instead of this
     */
    override fun showPage(page: Page) {
        app.showPage(page)
    }

    /**
     * Init block
     */
    init {
        rootElement.style.backgroundColor = Color.GREY_50
        rootElement.style.height = "100%"
    }

    /**
     * Internal class to handle state of page
     */
    private enum class State {
        NOT_INITIALIZED,
        LOADING,
        LOADED,
        DESTROYING,
        DESTROYED
    }

    companion object {

        private const val NOTHING = ""
    }

}