package com.foundation.widget.crvadapter.viewbinding

import androidx.viewbinding.ViewBinding
import com.foundation.widget.binding.ViewBindingHelper

/**
 * 轻量级可展开折叠的adapter
 *
 * 点击事件等想得到position见[getPositionInfo]
 *
 * 注意：目前仅为普通的2级数据展示
 * 暂未实现展开折叠（后续需要再添加）
 * 暂不支持header、footer、empty等功能（后续需要再添加）
 */
abstract class ViewBindingExpandableAdapter<T, VB1 : ViewBinding, VB2 : ViewBinding> :
    ViewBindingMultiItemAdapter<T>() {
    private val listRange get() = 0 until data.size

    init {
        TypeBuilder(getViewBindingClassFromIndex<VB1>(1))
            .setIsThisTypeCallback { _, listPosition, _ ->
                val info = getPositionInfo(listPosition)
                return@setIsThisTypeCallback listPosition == info.itemStartPosition
            }
            .setOnBindListViewHolderCallback { _, holder, _, item ->
                convertParent(holder, item)
            }
            .build()
        addDefaultMultipleItem(getViewBindingClassFromIndex<VB2>(2)) { _, holder, _, item ->
            val listPosition = holder.getListPosition()
            val info = getPositionInfo(listPosition)
            convertChild(holder, item, listPosition - info.itemStartPosition)
        }
    }

    private fun <T : ViewBinding> getViewBindingClassFromIndex(typeIndex: Int) =
        ViewBindingHelper.getViewBindingClassFromIndex<T>(this.javaClass, typeIndex)
            ?: throw IllegalArgumentException("没有找到类${this}的ViewBinding，请检查")

    override fun getItemViewType(position: Int): Int {
        return getDefItemViewType(position)
    }

    override fun getItem(position: Int): T {
        val info = getPositionInfo(position)
        return data[info.parentPosition]
    }

    final override fun getItemCount(): Int {
        var count = data.size
        (0 until data.size).forEach { parentPosition ->
            count += getChildCount(parentPosition)
        }
        return count
    }

    /**
     * 根据holder的position获取child真正的position
     * @param listPosition holder的listPosition
     */
    fun getPositionInfo(listPosition: Int): ExpandablePositionInfo {
        var itemStartPosition = 0
        listRange.forEach { parentPosition ->
            val childCount = getChildCount(parentPosition)
            val nextStartPosition = itemStartPosition + childCount + 1
            if (nextStartPosition > listPosition) {
                return ExpandablePositionInfo(
                    parentPosition,
                    listPosition - itemStartPosition,
                    itemStartPosition
                )
            } else {
                itemStartPosition = nextStartPosition
            }
        }
        throw IllegalArgumentException("没有找到对应的child你可能没有notify")
    }

    abstract fun getChildCount(parentPosition: Int): Int

    abstract fun convertParent(holder: ViewBindingViewHolder<VB1>, item: T)
    abstract fun convertChild(holder: ViewBindingViewHolder<VB2>, parentItem: T, childPosition: Int)

    class ExpandablePositionInfo(
        val parentPosition: Int,
        val childPosition: Int,
        internal val itemStartPosition: Int
    )
}