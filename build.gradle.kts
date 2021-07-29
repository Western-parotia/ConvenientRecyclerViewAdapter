//import com.buildsrc.kts.*//根目录文件不允许使用 包导入，只能写全路径
// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {

    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath(group = "com.android.tools.build", name = "gradle", version = "4.1.0")
        classpath(
            group = "org.jetbrains.kotlin",
            name = "kotlin-gradle-plugin",
            version = com.buildsrc.kts.Dependencies.Kotlin.version
        )
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle-backup-backup files
    }
}

allprojects {
    repositories {
        google()
        jcenter()
        maven { setUrl("https://jitpack.io") }
        maven {
            setUrl(com.buildsrc.kts.Publish.Maven.codingArtifactsRepoUrl)
            credentials {
                username =
                    com.buildsrc.kts.Publish.Maven.codingArtifactsGradleUsername
                password =
                    com.buildsrc.kts.Publish.Maven.codingArtifactsGradlePassword
            }
        }
    }
}

tasks.register("clean", Delete::class.java) {
    delete(rootProject.buildDir)
}
