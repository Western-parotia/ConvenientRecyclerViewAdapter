# ConvenientRecyclerViewAdapter

在BaseQuickAdapter的基础上，增加支持ViewBinding自动初始化。

### 更新日志：

* 2021-7-27：
    * 优化库名称，优化语义

### 引用
```kotlin
implementation("com.foundation.widget:convenient-recyclerview-adapter:看最新tag")
```

### 一、单类型支持ViewBinding

```kotlin

class BookListAdapter : ViewBindingQuickAdapter<ItemBookBinding, BookBean>() {

  override fun convert(helper: ViewBindingViewHolder<ItemBookBinding>, item: BookBean) {
    val vb = helper.viewBinding
    vb.tvName.text = item.name
    vb.ivPrice.text = "¥${item.price}"
  }
}
```

### 二、多类型通用版本，无需单独创建Adapter类

```kotlin
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
// build 模式
adapter.addMultipleItemBuild<ItemWeatherCloudyBinding>()
  .setIsThisTypeCallback { _, _, item ->
    item.weather == "阴天"
  }
  .setOnBindListViewHolderCallback { _, _, vb, item ->
    vb.tvName.text = "type:2 ${item.weather}"
  }
  .build()
```

### 三、多类型自实现Adapter）

```kotlin
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

```
