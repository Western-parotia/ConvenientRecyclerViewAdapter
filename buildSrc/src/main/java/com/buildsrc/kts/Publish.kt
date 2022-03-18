package com.buildsrc.kts

import java.io.File
import java.util.*


/**
 *-
 *-
 *create by zhusw on 5/6/21 16:43
 */

private const val VERSION = "1.0.6"
private const val SNAPSHOT = false

object Publish {
    object Version {
        var versionName = VERSION
            private set
            get() = when (SNAPSHOT) {
                true -> "$field-SNAPSHOT"
                false -> field
            }
        const val versionCode = 1

        private fun getTimestamp(): String {
            return java.text.SimpleDateFormat(
                "yyyy-MM-dd-hh-mm-ss",
                java.util.Locale.CHINA
            ).format(java.util.Date(System.currentTimeMillis()))
        }

        fun getVersionTimestamp(): String {
            return "$versionName-${getTimestamp()}"
        }
    }

    object Maven {
        const val groupId = "com.foundation.widget"
        const val artifactId = "convenient-recyclerview-adapter"
        val codingArtifactsGradleUsername: String
        val codingArtifactsGradlePassword: String
        val codingArtifactsRepoUrl: String

        init {
            //读取local的腾讯云用户名和密码
            val localProperties = Properties()
            var lp = File("local.properties")
            if (!lp.exists()) lp = File("../local.properties")//“/”win和mac都支持
            if (!lp.exists()) throw RuntimeException("没有找到local.properties")
            localProperties.load(lp.inputStream())
            val name = localProperties.getProperty("codingArtifactsGradleUsername")
            val password = localProperties.getProperty("codingArtifactsGradlePassword")
            val url = localProperties.getProperty("codingArtifactsRepoUrl")
            if (name == null || password == null || url == null) {
                throw RuntimeException(
                    "请在local.properties添加私有仓库的用户名（codingArtifactsGradleUsername）" +
                            "、密码（codingArtifactsGradlePassword）、url（codingArtifactsRepoUrl）"
                )
            }
            codingArtifactsGradleUsername = name
            codingArtifactsGradlePassword = password
            codingArtifactsRepoUrl = url
        }
    }

}
