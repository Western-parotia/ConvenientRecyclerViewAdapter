package com.kst.template

import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * create by zhusw on 6/23/21 10:46
 */
class WeatherBean(val temperature: Int, val state: Int) : MultiItemEntity {

    override fun getItemType(): Int = state
}

