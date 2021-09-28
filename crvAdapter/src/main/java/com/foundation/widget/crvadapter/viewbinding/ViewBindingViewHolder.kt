package com.foundation.widget.crvadapter.viewbinding

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

/**
 *
 * 基于 bravh.viewholder.BaseViewHolder 拓展提供 viewBinding 获取
 * create by zhusw on 6/23/21 10:37
 */
open class ViewBindingViewHolder<T : ViewBinding>(val viewBinding: T) :
    BaseViewHolder(viewBinding.root) {

    /**
     * 两个position有点头疼，无从选择，合并成一个
     *
     * 注意点：
     * list里注意header、footer
     * 完全就没bind过，肯定还是-1了
     */
    val adapterLayoutPosition: Int get() = if (layoutPosition < 0) bindingAdapterPosition else layoutPosition

    /**
     * 获取list的真正position
     */
    fun getListPosition(adapter: RecyclerView.Adapter<*>? = bindingAdapter): Int {
        if (adapter is BaseQuickAdapter<*, *>) {
            return adapterLayoutPosition - adapter.headerLayoutCount
        }
        return -1
    }

    val context: Context get() = itemView.context
}