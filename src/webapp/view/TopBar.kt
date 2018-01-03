package webapp.view

import webapp.core.Page
import webapp.core.View
import webapp.core.px
import webapp.util.Color

/**
 * Class to implement top bar of [Page]
 */
class TopBar : ComplexView() {

    private var titleLabel = createTitleLabel("App name goes here")

    // TODO: provide logic to add "UP" button, and menu

    /**
     * Title text in TopBar
     */
    var title : String
    get() = titleLabel.text
    set(value) {
        titleLabel.setText(value)
    }

    /**
     * Creates label to show [Page] title
     */
    private fun createTitleLabel(title : String) : Label {
        val label = Label(title)
        label.width = MATCH_PARENT
        label.height = MATCH_PARENT
        label.verticalAlign = Label.VerticalAlign.CENTER
        label.textAlign = TextView.TextAlign.START
        label.setPadding(0, 0, 32, 0)
        label.textColor = Color.getTextColor(background)
        label.textSize = 24.px
        return label
    }

    /**
     * Init block
     */
    init {
        height = APPBAR_HEIGHT

        // App primary color
        background = Color.TEAL_500

        subView = titleLabel
    }

    companion object {

        private const val APPBAR_HEIGHT = "70px"

    }

}