package com.kst.template

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.foundation.widget.crvadapter.viewbinding.ViewBindingQuickAdapter
import com.foundation.widget.crvadapter.viewbinding.ViewBindingViewHolder
import com.kst.template.databinding.ActivityMainBinding
import com.kst.template.databinding.ItmeBookBinding

class MainActivity : AppCompatActivity() {

    lateinit var vb: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vb = ActivityMainBinding.inflate(layoutInflater)
        setContentView(vb.root)
        setBookData()


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
}

class BookListAdapter : ViewBindingQuickAdapter<ItmeBookBinding, BookBean>() {

    override fun convert(helper: ViewBindingViewHolder<ItmeBookBinding>, item: BookBean) {
        val vb = helper.viewBinding
        vb.tvName.text = item.name
        vb.tvPrice.text = "¥${item.price}"
    }

}

