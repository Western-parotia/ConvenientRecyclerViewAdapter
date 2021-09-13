package com.foundation.widget.crvadapter.viewbinding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.entity.MultiItemEntity
import com.foundation.widget.binding.ViewBindingHelper
import com.foundation.widget.crvadapter.databinding.CrvadapterEmptyViewBindingLayoutBinding

/**
 * 支持 viewBinding
 * create by zhusw on 6/23/21 10:37
 */
abstract class ViewBindingMultiItemQuickAdapter<T : MultiItemEntity>
    : BaseQuickAdapter<T, MultiViewBindingViewHolder>(0) {

    companion object {
        const val DEFAULT_VIEW_TYPE = -1
    }

    private val bindingMap = HashMap<Int, Class<out ViewBinding>>()

    init {
        bindingMap[DEFAULT_VIEW_TYPE] = CrvadapterEmptyViewBindingLayoutBinding::class.java
    }

    override fun getDefItemViewType(position: Int): Int {
        val item: Any = mData[position]
        return if (item is MultiItemEntity) {
            item.itemType
        } else DEFAULT_VIEW_TYPE
    }

    fun setDefaultViewTypeLayout(vb: Class<out ViewBinding>) {
        bindingMap[DEFAULT_VIEW_TYPE] = vb
    }

    final override fun onCreateDefViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MultiViewBindingViewHolder {
        val clzType = bindingMap[viewType]!!
        val vb = ViewBindingHelper.getViewBindingInstanceByClass<ViewBinding>(
            clzType, LayoutInflater.from(parent.context), parent, false
        )
        return MultiViewBindingViewHolder(vb, viewType)
    }

    fun addItemType(type: Int, vb: Class<out ViewBinding>) {
        bindingMap[type] = vb
    }


}