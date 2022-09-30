package ru.sadv1r.idea.plugin.plugin.repository.auth.provider

import com.intellij.ide.plugins.auth.PluginRepositoryAuthProvider
import com.intellij.openapi.components.service

class MarketplacePluginRepositoryHeadersProvider : PluginRepositoryAuthProvider {

    private val marketplaceRepositoryAuthSettings = service<MarketplacePluginRepositoryHeadersSettings>()

    override fun getAuthHeaders(url: String?) = marketplaceRepositoryAuthSettings.getHeaders()

    override fun canHandle(url: String?) = marketplaceRepositoryAuthSettings.isCustomHostSet()

}