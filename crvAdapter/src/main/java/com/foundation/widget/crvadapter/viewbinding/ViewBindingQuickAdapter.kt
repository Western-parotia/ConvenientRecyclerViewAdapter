package com.foundation.widget.crvadapter.viewbinding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.foundation.widget.binding.ViewBindingHelper

/**
 * 初始化 viewBinding
 * VB:ViewBinding
 * T : Data Class
 * create by zhusw on 6/23/21 10:37
 */
abstract class ViewBindingQuickAdapter<VB : ViewBinding, T>
    : BaseQuickAdapter<T, ViewBindingViewHolder<VB>>(0) {
    override fun onCreateDefViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewBindingViewHolder<VB> {
        val vb = ViewBindingHelper.getViewBindingInstance<VB>(
            this, LayoutInflater.from(parent.context), parent, false
        )
        return ViewBindingViewHolder(vb)
    }

    abstract override fun convert(holder: ViewBindingViewHolder<VB>, item: T)
}
