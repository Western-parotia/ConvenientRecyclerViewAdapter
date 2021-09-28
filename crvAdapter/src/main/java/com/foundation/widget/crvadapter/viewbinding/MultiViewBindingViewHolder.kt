package com.foundation.widget.crvadapter.viewbinding

import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * 基于 bravh.viewholder.BaseViewHolder 拓展提供 viewBinding 获取
 * 支持multi 类型
 * create by zhusw on 6/23/21 10:37
 */
class MultiViewBindingViewHolder(viewBinding: ViewBinding, val type: Int) :
    ViewBindingViewHolder<ViewBinding>(viewBinding) {
    /**
     * 强制转换类型
     */
    fun <VB : ViewBinding> forceBinding(itemType: Int): VB {
        if (itemType == type) {
            return viewBinding as VB
        }
        throw IllegalArgumentException("itemType not matched,check addItemType() ,make sure it match")
    }

    fun <VB : ViewBinding> forceBinding(item: MultiItemEntity): VB {
        return forceBinding(item.itemType)
    }
}