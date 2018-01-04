package webapp.view

import webapp.core.View

/**
 * Class to show lists of information
 * all items are not reusable, and visible in dom-tree
 *
 * NOTE: Section headers are invisible if adapter defines only one section
 */
open class ListView<T : ListView.ViewHolder>(adapter: Adapter<T>) : ComplexView() {

    /**
     * Pool with items of ListView
     */
    private val holdersPool = mutableListOf<MutableList<T>>()

    /**
     * Pool with section headers items
     */
    private val sectionHeadersPool = mutableListOf<View>()

    /**
     * LinearBlock, which contain all items
     */
    private val block = VerticalBlock()

    /**
     * ScrollView to scroll items into ListView
     */
    private val scrollView = ScrollView(block).apply {
        canScrollHorizontally = false
        canScrollVertically = true
    }

    /**
     * An adapter for List view. Provides data for ListView
     */
    var adapter : Adapter<T> = adapter
    set(value) {
        field = value
        notifyDatasetChanged()
    }

    /**
     * Describes visibility of section headers
     *
     * To set value from please use [invalidateSectionHeadersVisibility] to set new value
     *
     * @see handleSectionHeadersVisibilityChange
     */
    protected var isSectionHeadersVisible = false
    private set(value) {
        val old = field
        field = value
        if (old != value) handleSectionHeadersVisibilityChange(value)
    }

    /**
     * Insert item into ListView
     *
     * @param section index of section with inserted element
     * @param number index of inserted element
     */
    open fun notifyItemInserted(section: Int, number: Int) {
        notifyItemRangeInserted(section, number, 1)
    }

    /**
     * Insert items into ListView
     *
     * @param section index of section with inserted element
     * @param number index of first inserted element
     * @param count count of inserted elements
     */
    open fun notifyItemRangeInserted(section: Int, number: Int, count : Int) {
        for (i in 0 until count) {
            val holder = adapter.createViewHolder()
            adapter.bindViewHolder(holder, section, number + i)
            block.add(holder.item, calculateNewElementIndex(section, number + i) )
            holdersPool[section].add(number + i, holder)
        }
    }

    /**
     * Removes item from ListView
     *
     * @param section index of section with removed element
     * @param number index of removed element
     */
    open fun notifyItemRemoved(section: Int, number: Int) {
        notifyItemRangeRemoved(section, number, 1)
    }

    /**
     * Removes item from ListView
     *
     * @param section index of section with removed element
     * @param number index of removed element
     * @param count count of removed elements
     */
    open fun notifyItemRangeRemoved(section: Int, number: Int, count : Int) {
        for (i in 0 until count) {
            block.removeChild(holdersPool[section].removeAt(number).item)
        }
    }

    /**
     * Insert section into ListView
     *
     * @param section index of inserted section
     */
    open fun notifySectionInserted(section: Int) {
        notifySectionRangeInserted(section, 1)
    }

    /**
     * Insert sections into ListView
     *
     * @param startIndex index of inserted section
     * @param count count of new sections
     */
    open fun notifySectionRangeInserted(startIndex: Int, count : Int) {
        for (i in 0 until count) {
            val sectionList = mutableListOf<T>()
            holdersPool.add(startIndex + i, sectionList)
            val itemsCount = adapter.getItemsInSectionCount(startIndex + i)
            if (isSectionHeadersVisible) {
                val name = adapter.getSectionHeader(startIndex + i)!!
                block.add(name, calculateNewSectionNameIndex(startIndex + i))
                sectionHeadersPool.add(startIndex + i, name)
            }
            for (j in 0 until itemsCount) {
                val holder = adapter.createViewHolder()
                adapter.bindViewHolder(holder, startIndex + i, j)
                block.add(holder.item, calculateNewElementIndex(startIndex + i, j))
                sectionList.add(holder)
            }
        }
        invalidateSectionHeadersVisibility()
    }

    /**
     * Removes section from ListView
     *
     * @param section index of removed section
     */
    open fun notifySectionRemoved(section: Int) {
        notifySectionRangeRemoved(section, 1)
    }

    /**
     * Removes sections into ListView
     *
     * @param startIndex index of first removed section
     * @param count count of removed sections
     */
    open fun notifySectionRangeRemoved(startIndex: Int, count : Int) {
        for (i in 0 until count) {
            if (isSectionHeadersVisible) {
                block.removeChild(sectionHeadersPool[startIndex])
            }
            val sectionHolders = holdersPool.removeAt(startIndex)
            for (holder in sectionHolders) {
                block.removeChild(holder.item)
            }
        }
        invalidateSectionHeadersVisibility()
    }

    /**
     * Causes updating of view form adapter
     *
     * Note: item update causes rebinding it, not reloading
     * if you want to reload items, you must use [notifyDatasetChanged]
     *
     * @param section index of section with changed element
     * @param number index of changed element
     */
    open fun notifyItemChanged(section: Int, number: Int) {
        notifyItemRangeChanged(section, number, 1)
    }

    /**
     * Causes updating of target views
     *
     * Note: item update causes rebinding it, not reloading
     * if you want to reload items, you must use [notifyDatasetChanged]
     *
     * @param section index of section with changed elements
     * @param startIndex index of first changed element
     * @param count count of changed items
     */
    open fun notifyItemRangeChanged(section: Int, startIndex: Int, count : Int) {
        for (i in 0 until count) {
            val index = startIndex + i
            adapter.bindViewHolder(holdersPool[section][index], section, index)
        }
    }

    /**
     * Causes updating of all items in section
     *
     * Note: item update causes rebinding it, not reloading
     * if you want to reload items, you must use [notifyDatasetChanged]
     *
     * @param section index of changed section
     */
    open fun notifySectionChanged(section: Int) {
        notifySectionRangeChanged(section, 1)
    }

    /**
     * Causes updating of target views
     *
     * Note: item update causes rebinding it, not reloading
     * if you want to reload items, you must use [notifyDatasetChanged]
     *
     * @param startNumber index of first changed section, inclusive
     * @param count count of changed sections
     */
    open fun notifySectionRangeChanged(startNumber : Int, count : Int) {
        for (i in 0 until count) {
            val section = startNumber + i
            val sectionHolders = holdersPool[section]

            val sectionLength = sectionHolders.size
            val newLength = adapter.getItemsInSectionCount(section)

            if (isSectionHeadersVisible) {
                val oldSectionName = sectionHeadersPool.removeAt(section)
                val newSectionName = adapter.getSectionHeader(section)!!
                sectionHeadersPool.add(section, newSectionName)
                val index = block.indexOf(oldSectionName)
                block.removeChild(oldSectionName)
                block.add(newSectionName, index)
            }

            var index = calculateNewElementIndex(section, 0)

            // processing section
            for (j in 0 until maxOf(sectionLength, newLength)) {
                if (j < sectionLength) { // view exist
                    if (j < newLength) { // view should exits
                        adapter.bindViewHolder(sectionHolders[j], section, j)
                        index++
                    } else {
                        block.removeChild(sectionHolders.removeAt(newLength).item)
                    }
                } else {
                    val holder = adapter.createViewHolder()
                    sectionHolders.add(j, holder)
                    adapter.bindViewHolder(holder, section, j)
                    block.add(holder.item, index++)
                }
            }
        }
        invalidateSectionHeadersVisibility()
    }

    /**
     * Returns item's section number
     *
     * @param item target item
     * @return section of item. -1 if not found
     */
    protected fun getSectionNumber(item : T) : Int {
        return (0 until holdersPool.size).firstOrNull { holdersPool[it].contains(item) } ?: -1
    }

    /**
     * Internal method to get last element in [block]
     *
     * @param list list with holders
     * @return last element in list. null if list is empty
     */
    private fun getLastItem(list : List<List<T>>) : T? {
        val filtredList = list.filter { it.isNotEmpty() }
        if (filtredList.isEmpty()) {
            return null
        } else {
            return filtredList.last().last()
        }
    }

    /**
     * Returns index for new creating element
     *
     * @param section index of section
     * @param number index of element in section
     * @return index of element in [block]
     */
    protected fun calculateNewSectionNameIndex(section : Int) : Int {
        return calculateNewElementIndex(section, 0) - 1
    }

    /**
     * Returns index for new creating element
     * If section name is visible, it's must be added before calling this
     *
     * @param section index of section
     * @param number index of element in section
     * @return index of element in [block]
     */
    protected fun calculateNewElementIndex(section : Int, number: Int) : Int {
        var sectionIsNotExist = false
        var sectionIsEmpty = false
        var lastItem : T? = null

        if (holdersPool.size <= section) {
            lastItem = getLastItem(holdersPool)
            sectionIsNotExist = true
        }

        if (!sectionIsNotExist && holdersPool[section].isEmpty()) {
            lastItem = getLastItem(holdersPool.slice(0 until  section))
            sectionIsEmpty = true
        }

        if (sectionIsEmpty || sectionIsNotExist) {
            val lastSection = lastItem?.let { getSectionNumber(it) } ?: -1
            val sectionHeaders = section - lastSection
            val headersCorrection = if (isSectionHeadersVisible) sectionHeaders else 0
            val lastIndex = lastItem?.let { block.indexOf(it.item) } ?: -1

            return lastIndex + 1 + headersCorrection
        }

        if (holdersPool[section].size <= number) {
            return block.indexOf(holdersPool[section].last().item) + 1
        }
        return getExistingElementIndex(section, number)
    }

    /**
     * Returns index of existing element
     *
     * @param section index of section
     * @param number index of element in section
     * @return index of element in [block]
     */
    protected fun getExistingElementIndex(section : Int, number: Int) : Int {
        return block.indexOf(holdersPool[section][number].item)
    }

    /**
     * Causes reloading of all items form adapter
     */
    open fun notifyDatasetChanged() {
        redraw()
    }

    /**
     * Internal function, that causes recalculating visibility of section headers
     *
     * @see handleSectionHeadersVisibilityChange
     */
    private fun invalidateSectionHeadersVisibility() {
        isSectionHeadersVisible = adapter.getSectionsCount() > 1
    }

    /**
     * Creates new section headers or removes existing when [isSectionHeadersVisible] are changing
     *
     * Please use value from [isSectionHeadersVisible] when you applies change to DOM tree
     *
     * @see invalidateSectionHeadersVisibility
     */
    open protected fun handleSectionHeadersVisibilityChange(newValue : Boolean) {
        if (newValue) {
            // create new
            for (section in 0 until adapter.getSectionsCount()) {
                val view = adapter.getSectionHeader(section)!!
                block.add(view, calculateNewSectionNameIndex(section))
                sectionHeadersPool.add(section, view)
            }
        } else {
            for (view in sectionHeadersPool) {
                block.removeChild(view)
            }
            sectionHeadersPool.clear()
        }
    }

    /**
     * Renders all items
     */
    override fun renderContent() {
        super.renderContent()
        block.removeAllViews()
        holdersPool.clear()
        invalidateSectionHeadersVisibility()
        for (section in 0 until adapter.getSectionsCount()) {
            if (isSectionHeadersVisible) {
                val view = adapter.getSectionHeader(section)!!
                sectionHeadersPool.add(section, view)
                block.add(view)
            }
            val holders = mutableListOf<T>()
            for (item in 0 until adapter.getItemsInSectionCount(section)) {
                val holder = adapter.createViewHolder()
                holders.add(holder)
                adapter.bindViewHolder(holder, section, item)
                block.add(holder.item)
            }
            holdersPool.add(holders)
        }
    }

    /**
     * Init block
     */
    init {
        subView = scrollView
    }

    /**
     * Sets width
     *
     * @param value new width value
     */
    override fun setWidth(value: String) {
        super.setWidth(value)
        subView?.setWidth(value)
    }

    /**
     * Sets height
     *
     * @param value new height value
     */
    override fun setHeight(value: String) {
        super.setHeight(value)
        subView?.setHeight(value)
    }

    /**
     * Class to hold data about Item
     */
    abstract class ViewHolder(internal val item : View) {

        /**
         * Init block
         */
        init {
            item.width = MATCH_PARENT
            item.height = WRAP_CONTENT
        }
    }

    /**
     * ListView interface to set
     */
    interface Adapter<T : ViewHolder> {

        /**
         * Defines count of sections in ListView
         * Each sections should consist of same type of elements
         *
         * @return count of sections in ListView
         */
        fun getSectionsCount() : Int

        /**
         * Defines View, must be used as header for section
         * can be null if section headers are invisible
         *
         * @param section number of section
         * @return [View] for section header
         */
        fun getSectionHeader(section: Int) : View?

        /**
         * Defines count of items in section
         *
         * @param section number of section
         * @return count of items in section
         */
        fun getItemsInSectionCount(section : Int) : Int

        /**
         * Creates a new ViewHolder
         * must only create ViewHolder object with field, that will be used in [bindViewHolder] method
         *
         * @return new holder
         */
        fun createViewHolder() : T

        /**
         * Binds ViewHolder to it number and section
         * must sets fields in ViewHolder to it target values
         *
         * @param section number of section
         * @param number number of item in section
         */
        fun bindViewHolder(holder : T, section: Int, number: Int)

    }

}