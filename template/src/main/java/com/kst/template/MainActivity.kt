package com.kst.template

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.foundation.widget.crvadapter.viewbinding.*
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
        setWeathers2()
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

    private fun setWeathers2() {
        val adapter = ViewBindingMultiItemAdapter<WeatherBean>()
        adapter.addDefaultMultipleItem<ItemWeatherCloudyBinding> { _, _, vb, _ ->
            vb.tvName.text = "兜底"
        }
        adapter.addMultipleItem(object :
            ViewBindingMultiItemAdapter.OnMultipleListListener<ItemWeatherSunnyBinding, WeatherBean>() {
            override fun isThisType(
                adapter: ViewBindingMultiItemAdapter<WeatherBean>,
                listPosition: Int,
                item: WeatherBean
            ): Boolean {
                return item.weather == "晴天"
            }

            override fun onBindListViewHolder(
                adapter: ViewBindingMultiItemAdapter<WeatherBean>,
                holder: ViewBindingViewHolder<ItemWeatherSunnyBinding>,
                vb: ItemWeatherSunnyBinding,
                item: WeatherBean
            ) {
                holder.viewBinding.tvName.text = "type:1 ${item.weather}"
            }
        })
        adapter.addMultipleItemBuild<ItemWeatherCloudyBinding>()
            .setIsThisTypeCallback { _, _, item ->
                item.weather == "阴天"
            }
            .setOnBindListViewHolderCallback { _, _, vb, item ->
                vb.tvName.text = "type:2 ${item.weather}"
            }
            .build()
        vb.rvWeather2.let {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = adapter
        }
        val data = mutableListOf<WeatherBean>()
        for (i in 0..20) {
            val bean = when (i % 3) {
                0 -> {
                    WeatherBean("晴天")
                }
                1 -> {
                    WeatherBean("阴天")
                }
                else -> {
                    WeatherBean("没有乱写的")
                }
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

