package com.buildsrc.kts

/**
 *@Desc:
 *-
 *-依赖声明
 *create by zhusw on 5/6/21 15:45
 */
object Dependencies {
    const val kotlinVersion = "1.6.21"
    object Kotlin {
        /**
         * kotlin 语言核心库，像 let这种操作域拓展
         */
        const val kotlin_stdlib = "org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion"
    }

    object AndroidX {
        /**
         * kotlin 标准库，各种推展方法，像 foreach什么的
         */
        const val core_ktx = "androidx.core:core-ktx:1.3.2"
        const val appcompat = "androidx.appcompat:appcompat:1.2.0"
        const val constraintlayout = "androidx.constraintlayout:constraintlayout:2.0.4"

    }

    object Material {
        const val material = "com.google.android.material:material:1.3.0"
    }

    object RecyclerView {
        const val chadAdapter = "com.github.CymChad:BaseRecyclerViewAdapterHelper:2.9.30"
        const val recyclerView = "androidx.recyclerview:recyclerview:1.2.0"
    }

    object Foundation {
        const val viewBindingHelper = "com.foundation.widget:view-binding-helper:1.0.4"
    }

}
