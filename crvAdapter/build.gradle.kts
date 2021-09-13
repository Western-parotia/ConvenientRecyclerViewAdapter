import com.buildsrc.kts.AndroidConfig
import com.buildsrc.kts.Dependencies
import com.buildsrc.kts.Publish

plugins {
    id("com.android.library")
    id("kotlin-android")
    `maven-publish`
}
val versionTimestamp = Publish.Version.getVersionTimestamp()

android {
    compileSdkVersion(AndroidConfig.compileSdkVersion)

    defaultConfig {

        minSdkVersion(AndroidConfig.minSdkVersion)
        targetSdkVersion(AndroidConfig.targetSdkVersion)
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isDebuggable = false
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

    }
    afterEvaluate {
        buildTypes.forEach {
            it.buildConfigField("Integer", "versionCode", Publish.Version.versionCode.toString())
            it.buildConfigField("String", "versionName", "\"${Publish.Version.versionName}\"")
            it.buildConfigField("String", "versionTimeStamp", "\"$versionTimestamp\"")
        }
    }
    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = AndroidConfig.Language.sourceCompatibility
        targetCompatibility = AndroidConfig.Language.targetCompatibility
    }
    kotlinOptions {
        jvmTarget = AndroidConfig.Language.jvmTarget
    }
}

configurations.all {
    resolutionStrategy {
        //版本号会改变
        cacheDynamicVersionsFor(10, TimeUnit.SECONDS)
        //版本号不变,1.0-SNAPSHOT 这种应该实时更新
        cacheChangingModulesFor(10, TimeUnit.SECONDS)
    }
}
dependencies {
    implementation(Dependencies.Kotlin.kotlin_stdlib)
    implementation(Dependencies.AndroidX.core_ktx)
    api(Dependencies.RecyclerView.chadAdapter)
    implementation(Dependencies.RecyclerView.recyclerView)
    implementation(Dependencies.Foundation.viewBindingHelper)
}


val sourceCodeTask: Jar = tasks.register("sourceCode", Jar::class.java) {
    from(android.sourceSets.getByName("main").java.srcDirs)
    classifier = "sources"
}.get()


tasks.register("createGitTagAndPush", Exec::class.java) {
    commandLine("git", "push", "origin", versionTimestamp)
}.get().dependsOn(tasks.register("createGitTag", Exec::class.java) {
    commandLine("git", "tag", versionTimestamp, "-m", "autoCreateWithMavenPublish")
})
publishing {
    val versionName = Publish.Version.versionName
    val groupId = Publish.Maven.groupId
    val artifactId = Publish.Maven.artifactId

    publications {
        create<MavenPublication>("crvAdapter") {
            setGroupId(groupId)
            setArtifactId(artifactId)
            version = versionName
            artifact(sourceCodeTask)
            afterEvaluate {//在脚本读取完成后绑定
                val bundleReleaseAarTask: Task = tasks.getByName("bundleReleaseAar")
                bundleReleaseAarTask.finalizedBy("createGitTagAndPush")
                artifact(bundleReleaseAarTask)
            }
//            artifact("$buildDir/outputs/aar/loading-release.aar")//直接指定文件
            pom.withXml {
                val dependenciesNode = asNode().appendNode("dependencies")
                configurations.implementation.get().allDependencies.forEach {
                    if (it.version != "unspecified" && it.name != "unspecified") {
                        val depNode = dependenciesNode.appendNode("dependency")
                        depNode.appendNode("groupId", it.group)
                        depNode.appendNode("artifactId", it.name)
                        depNode.appendNode("version", it.version)
                    }
                }
            }

        }
        repositories {
            maven {
                setUrl(Publish.Maven.codingArtifactsRepoUrl)
                credentials {
                    username = Publish.Maven.codingArtifactsGradleUsername
                    password = Publish.Maven.codingArtifactsGradlePassword
                }
            }
        }
    }

}