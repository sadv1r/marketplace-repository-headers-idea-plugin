package ru.sadv1r.idea.plugin.plugin.repository.auth.provider

import com.intellij.ide.plugins.auth.PluginRepositoryAuthListener
import com.intellij.openapi.application.impl.ApplicationInfoImpl.DEFAULT_PLUGINS_HOST
import com.intellij.openapi.components.*
import com.intellij.util.xmlb.annotations.OptionTag

@State(name = "Marketplace.Plugin.Repository.Headers.Settings", storages = [Storage(value = "marketplace-plugin-repository-headers-settings.xml", roamingType = RoamingType.DISABLED)])
@Service(Service.Level.APP)
internal class MarketplacePluginRepositoryHeadersSettings : SimplePersistentStateComponent<MarketplacePluginRepositoryHeadersSettings.State>(State()) {

    private val IDEA_PLUGINS_HOST_PROPERTY = "idea.plugins.host"

    class State : BaseState() {
        @get:OptionTag("PLUGIN_REPOSITORY_HEADERS")
        var headers: MutableMap<String, String> by map()
    }

    fun getHost() = System.getProperty(IDEA_PLUGINS_HOST_PROPERTY) ?: DEFAULT_PLUGINS_HOST

    fun setHost(host: String): String? = System.setProperty(IDEA_PLUGINS_HOST_PROPERTY, host)

    fun removeHost(host: String): String? = System.clearProperty(IDEA_PLUGINS_HOST_PROPERTY)

    fun isCustomHostSet() = System.getProperty(IDEA_PLUGINS_HOST_PROPERTY)?.isNotEmpty() ?: false

    fun setHeaders(headers: Map<String, String>) {
        state.headers = headers.toMutableMap()

        PluginRepositoryAuthListener.notifyAuthChanged()
    }

    fun getHeaders(): Map<String, String> = state.headers

}