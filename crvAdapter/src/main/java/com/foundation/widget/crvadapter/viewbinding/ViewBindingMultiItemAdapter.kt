package com.foundation.widget.crvadapter.viewbinding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.Keep
import androidx.viewbinding.ViewBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.foundation.widget.binding.ViewBindingHelper

/**
 * @作者 王能
 * @时间 2022/3/1 10:46
 * api很简单，就3个：[addMultipleItem]、[addMultipleItemBuild]、[addDefaultMultipleItem]
 */
open class ViewBindingMultiItemAdapter<BEAN> :
    BaseQuickAdapter<BEAN, ViewBindingViewHolder<ViewBinding>>(0) {

    /**
     * 第0个是兜底策略，所以遍历集合都是倒序
     */
    private val idInfoList = ArrayList<OnMultipleListListener<out ViewBinding, BEAN>>(4)

    override fun getDefItemViewType(listPosition: Int): Int {
        idInfoList.forEachReverseSequence { index, listener ->
            if (listener.isThisType(this, listPosition, data[listPosition])) {
                return index
            }
        }
        throw IllegalArgumentException("第${listPosition}个没有对应的type或没有兜底处理")
    }

    override fun onCreateDefViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewBindingViewHolder<ViewBinding> {
        val clazz = idInfoList[viewType].getViewBindingClass()
        val vb = ViewBindingHelper.getViewBindingInstanceByClass<ViewBinding>(
            clazz,
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewBindingViewHolder(vb)
    }

    override fun convert(holder: ViewBindingViewHolder<ViewBinding>, item: BEAN) {
        idInfoList.forEachReverseSequence { _, listener ->
            if (listener.isThisType(this, holder.getListPosition(), item)) {
                //kotlin bug，直接调用会提示必须传Nothing？？？
                bindVB(holder, item, listener)
                return
            }
        }
    }

    private fun <VB : ViewBinding> bindVB(
        holder: ViewBindingViewHolder<ViewBinding>,
        item: BEAN,
        listener: OnMultipleListListener<VB, BEAN>
    ) {
        listener.onBindListViewHolder(
            adapter = this,
            holder = (holder as ViewBindingViewHolder<VB>),
            item = item
        )
    }

    /**
     * 方便使用inline简化效果，请忽略
     */
    fun <VB : ViewBinding> defItem(
        clazz: Class<VB>,
        onBindListViewHolderCallback: (
            adapter: ViewBindingMultiItemAdapter<BEAN>,
            holder: ViewBindingViewHolder<VB>,
            vb: VB,
            item: BEAN
        ) -> Unit
    ) {
        idInfoList.add(0, object : OnMultipleListListener<VB, BEAN>() {
            override fun isThisType(
                adapter: ViewBindingMultiItemAdapter<BEAN>,
                listPosition: Int,
                item: BEAN
            ): Boolean {
                return true
            }

            override fun getViewBindingClass(): Class<VB> {
                return clazz
            }

            override fun onBindListViewHolder(
                adapter: ViewBindingMultiItemAdapter<BEAN>,
                holder: ViewBindingViewHolder<VB>,
                vb: VB,
                item: BEAN
            ) {
                onBindListViewHolderCallback.invoke(adapter, holder, vb, item)
            }
        })
        notifyDataSetChanged()
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    // 类
    /////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * builder
     */
    inner class TypeBuilder<VB : ViewBinding>(private val clazz: Class<VB>) {
        private var isThisType: ((
            adapter: ViewBindingMultiItemAdapter<BEAN>,
            listPosition: Int,
            item: BEAN
        ) -> Boolean)? = null

        private var onBindListViewHolder: ((
            adapter: ViewBindingMultiItemAdapter<BEAN>,
            holder: ViewBindingViewHolder<VB>,
            vb: VB,
            item: BEAN
        ) -> Unit)? = null

        fun setIsThisTypeCallback(
            isThisTypeCallback: (
                adapter: ViewBindingMultiItemAdapter<BEAN>,
                listPosition: Int,
                item: BEAN
            ) -> Boolean
        ): TypeBuilder<VB> {
            isThisType = isThisTypeCallback
            return this
        }

        fun setOnBindListViewHolderCallback(
            onBindListViewHolderCallback: (
                adapter: ViewBindingMultiItemAdapter<BEAN>,
                holder: ViewBindingViewHolder<VB>,
                vb: VB,
                item: BEAN
            ) -> Unit
        ): TypeBuilder<VB> {
            onBindListViewHolder = onBindListViewHolderCallback
            return this
        }

        fun build() {
            if (isThisType == null || onBindListViewHolder == null) {
                throw IllegalArgumentException("必须调用setIsThisTypeCallback、setOnBindListViewHolderCallback")
            }

            addMultipleItem(object : OnMultipleListListener<VB, BEAN>() {
                override fun isThisType(
                    adapter: ViewBindingMultiItemAdapter<BEAN>,
                    listPosition: Int,
                    item: BEAN
                ): Boolean {
                    return isThisType!!.invoke(adapter, listPosition, item)
                }

                override fun getViewBindingClass(): Class<VB> {
                    return clazz
                }

                override fun onBindListViewHolder(
                    adapter: ViewBindingMultiItemAdapter<BEAN>,
                    holder: ViewBindingViewHolder<VB>,
                    vb: VB,
                    item: BEAN
                ) {
                    onBindListViewHolder!!.invoke(adapter, holder, vb, item)
                }
            })
        }
    }

    /**
     * 多条目实现类
     */
    @Keep
    abstract class OnMultipleListListener<VB : ViewBinding, BEANS> {
        /**
         * 这个bean是否是当前类型
         */
        abstract fun isThisType(
            adapter: ViewBindingMultiItemAdapter<BEANS>,
            listPosition: Int,
            item: BEANS
        ): Boolean

        /**
         * 默认走反射
         */
        open fun getViewBindingClass(): Class<VB> {
            return ViewBindingHelper.getViewBindingClass(this.javaClass)
                ?: throw IllegalArgumentException("没有找到类${this}的ViewBinding，请检查")
        }

        abstract fun onBindListViewHolder(
            adapter: ViewBindingMultiItemAdapter<BEANS>,
            holder: ViewBindingViewHolder<VB>,
            vb: VB = holder.viewBinding,
            item: BEANS
        )
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    // 公共方法
    /////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * 添加回调
     */
    fun <VB : ViewBinding> addMultipleItem(listener: OnMultipleListListener<VB, BEAN>) {
        idInfoList.add(listener)
        notifyDataSetChanged()
    }

    /**
     * build模式添加，更方便直观
     */
    inline fun <reified VB : ViewBinding> addMultipleItemBuild(): TypeBuilder<VB> {
        return TypeBuilder(VB::class.java)
    }

    /**
     * 添加兜底type（else）
     */
    inline fun <reified VB : ViewBinding> addDefaultMultipleItem(
        noinline onBindListViewHolderCallback: (
            adapter: ViewBindingMultiItemAdapter<BEAN>,
            holder: ViewBindingViewHolder<VB>,
            vb: VB,
            item: BEAN
        ) -> Unit
    ) {
        defItem(VB::class.java, onBindListViewHolderCallback)
    }
}