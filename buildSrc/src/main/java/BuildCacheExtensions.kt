import org.gradle.caching.local.DirectoryBuildCache
import org.gradle.kotlin.dsl.KotlinSettingsScript

fun KotlinSettingsScript.setupBuildCache() {
    buildCache {
        val localBuildCacheUrl = Properties.gradleLocalBuildCacheUrl
        if (!localBuildCacheUrl.isNullOrEmpty()) {
            println("Using local build cache located at: $localBuildCacheUrl")
            local(DirectoryBuildCache::class.java) {
                uri(localBuildCacheUrl)
            }
        }
    }
}
