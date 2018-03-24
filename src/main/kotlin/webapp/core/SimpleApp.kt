package webapp.core

/**
 * Simple application, used in most cases
 * just write SimpleApp({ <PageName>(it) }).start and enjoy it
 */
class SimpleApp(defaultPageCreator: (Application) -> Page) : Application() {

    /**
     * Default page holder
     */
    private var defaultPage: Page = defaultPageCreator(this)

    /**
     * Returns the startup [Page] for [Application]
     *
     * @return default page
     */
    override fun getDefaultPage(): Page = defaultPage
}