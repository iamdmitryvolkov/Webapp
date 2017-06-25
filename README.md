# Webapp
Webapp is Kotlin frontend framework 

Alpha version. There is will be something. Soon.

You can try it by yourself

    class MainPage(app : Application) : Page(app) {
    
        init {
            contentView = Label("Hello, world!")
        }
    
    }
    
    val app = SimpleApp({ MainPage(it) }).start()`

You will need div to show app (you can use src/index.html)

    <div id="application_root"></div>

Beta must be available at the end of 2017

Release version must be available in 2018

Interface may change at beta and release build