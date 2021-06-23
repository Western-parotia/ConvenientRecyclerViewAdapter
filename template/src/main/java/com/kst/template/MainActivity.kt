package com.kst.template

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.foundation.widget.crvadapter.viewbinding.MultiViewBindingViewHolder
import com.foundation.widget.crvadapter.viewbinding.ViewBindingMultiItemQuickAdapter
import com.foundation.widget.crvadapter.viewbinding.ViewBindingQuickAdapter
import com.foundation.widget.crvadapter.viewbinding.ViewBindingViewHolder
import com.kst.template.databinding.ActivityMainBinding
import com.kst.template.databinding.ItemBookBinding
import com.kst.template.databinding.ItemWeatherCloudyBinding
import com.kst.template.databinding.ItemWeatherSunnyBinding

class MainActivity : AppCompatActivity() {

    lateinit var vb: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vb.root)
        setBookData()
        setWeathers()

    }

    private fun setBookData() {
        val adapter = BookListAdapter()
        vb.rvBook.let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = adapter
        }
        val data = mutableListOf<BookBean>()
        for (i in 0..20) {
            val bean = if (i % 2 == 0) {
                BookBean("《麦田里的守望者》", 30.00)
            } else {
                BookBean("《三体》", 80.00)
            }
            data.add(bean)
        }
        adapter.setNewData(data)
    }

    private fun setWeathers() {
        val adapter = WeatherListAdapter()
        vb.rvWeather.let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = adapter
        }
        val data = mutableListOf<WeatherBean>()
        for (i in 0..20) {
            val bean = if (i % 2 == 0) {
                WeatherBean("晴天")
            } else {
                WeatherBean("阴天")
            }
            data.add(bean)
        }
        adapter.setNewData(data)
    }
}

class BookListAdapter : ViewBindingQuickAdapter<ItemBookBinding, BookBean>() {

    override fun convert(helper: ViewBindingViewHolder<ItemBookBinding>, item: BookBean) {
        val vb = helper.viewBinding
        vb.tvName.text = item.name
        vb.ivPrice.text = "¥${item.price}"
    }
}

class WeatherListAdapter : ViewBindingMultiItemQuickAdapter<WeatherBean>() {

    init {
        addItemType(1, ItemWeatherSunnyBinding::class.java)
        addItemType(2, ItemWeatherCloudyBinding::class.java)
    }

    override fun convert(helper: MultiViewBindingViewHolder, item: WeatherBean) {
        when (val itemType = item.itemType) {
            1 -> {
                val vb = helper.forceBinding<ItemWeatherSunnyBinding>(itemType)
                convertSunny(vb, item)
            }
            2 -> {
                val vb = helper.forceBinding<ItemWeatherCloudyBinding>(itemType)
                convertCloudy(vb, item)
            }
        }
    }

    fun convertSunny(vb: ItemWeatherSunnyBinding, item: WeatherBean) {
        vb.tvName.text = "type:1 ${item.weather}"
    }

    fun convertCloudy(vb: ItemWeatherCloudyBinding, item: WeatherBean) {
        vb.tvName.text = "type:2 ${item.weather}"
    }

}

