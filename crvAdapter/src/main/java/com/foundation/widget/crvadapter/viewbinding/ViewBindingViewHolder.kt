package com.foundation.widget.crvadapter.viewbinding

import android.view.View
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseViewHolder

/**
 *
 * 基于 bravh.viewholder.BaseViewHolder 拓展提供 viewBinding 获取
 * create by zhusw on 6/23/21 10:37
 */
class ViewBindingViewHolder<T : ViewBinding>(val viewBinding: T, view: View) :
    BaseViewHolder(view) {
}