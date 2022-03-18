package com.foundation.widget.crvadapter.viewbinding

/**
 * @作者 王能
 * @时间 2022/3/1 17:05
 */

/**
 * 倒序遍历
 */
internal inline fun <T> List<T>.forEachReverseSequence(action: (index: Int, T) -> Unit) {
    for (index in size - 1 downTo 0) {
        action(index, this[index])
    }
}