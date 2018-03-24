package webapp.core

import org.w3c.dom.Element
import org.w3c.dom.HTMLElement
import kotlin.browser.document
import kotlin.browser.window
import kotlin.dom.clear

/**
 * Main application class. Created by Dmitry Volkov on 27.02.17.
 * [Application] class control [Page] objects, provide access to resources and preferences.
 *
 * Any app should consist of [Application] and [Page].
 * You must override [getDefaultPage] method to let app know which page it should show.
 * To start your app just call [start]
 *
 * NOTE: in normal case [Application] must have one and only one extended subclass
 */
abstract class Application : CoreFeatures {

    /**
     * The main div, in which app will render
     */
    val rootElement: Element = document.getElementById(ID_ROOT_ELEMENT)!!

    /**
     * True if app is waiting for page lifecycle. In this case callback will not be called
     */
    var isWaitingForPageLifecycle = false
        private set(value) { field = value }

    private var currentPage: Page? = null
    private var isStarted = false

    /**
     * Returns the startup [Page] for [Application]
     *
     * @return default page
     */
    abstract fun getDefaultPage(): Page

    /**
     * Loads default [Page] and starts the application
     *
     * Note: Application can be started only once
     *
     * @see [getDefaultPage]
     */
    fun start() {
        setupDocumentBody(document.body!!)
        if (isStarted) throw IllegalStateException("Application can be stared only once")
        isStarted = true
        window.onfocus = { onGetFocus() }
        window.onblur = { onLostFocus() }
        window.onresize = { onResize() }
        showPage(getDefaultPage())
    }

    /**
     * Applies initial values to document.body
     */
    open protected fun setupDocumentBody(body: HTMLElement) {
        body.style.margin = "0px"
        body.style.overflowX = "hidden"
    }

    /**
     * Clears and Destroys [currentPage] and show new [page] instead
     *
     * @param page the page to be shown
     */
    override fun showPage(page: Page) {
        if (!isStarted) throw IllegalStateException("Application is not started")
        if (isWaitingForPageLifecycle) return
        isWaitingForPageLifecycle = true
        currentPage?.destroy()
        rootElement.clear() // destroying old page
        page.load()
        currentPage = page
        isWaitingForPageLifecycle = false
    }

    /**
     * Onfocus main callback.
     */
    private fun onGetFocus() {
        if (!isWaitingForPageLifecycle) currentPage?.notifyFocusChanged(true)
    }

    /**
     * Onblur main callback.
     */
    private fun onLostFocus() {
        if (!isWaitingForPageLifecycle) currentPage?.notifyFocusChanged(false)
    }

    /**
     * OnResize main callback.
     */
    private fun onResize() {
        if (!isWaitingForPageLifecycle) currentPage?.onWindowSizeChanged()
    }

    /**
     * Additional features and constants
     */
    companion object {

        private const val ID_ROOT_ELEMENT = "application_root"
    }
}