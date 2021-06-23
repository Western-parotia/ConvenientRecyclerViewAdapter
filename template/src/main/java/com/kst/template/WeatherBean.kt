package com.kst.template

import com.chad.library.adapter.base.entity.MultiItemEntity
import com.foundation.widget.crvadapter.viewbinding.ViewBindingMultiItemQuickAdapter

/**
 * create by zhusw on 6/23/21 10:46
 */
class WeatherBean(val weather: String) : MultiItemEntity {

    override fun getItemType(): Int = when (weather) {
        "晴天" -> 1
        "阴天" -> 2
        else -> ViewBindingMultiItemQuickAdapter.DEFAULT_VIEW_TYPE
    }
}

