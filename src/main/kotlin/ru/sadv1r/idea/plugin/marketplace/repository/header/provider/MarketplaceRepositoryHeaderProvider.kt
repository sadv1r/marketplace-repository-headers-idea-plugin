package ru.sadv1r.idea.plugin.marketplace.repository.header.provider

import com.intellij.ide.plugins.auth.PluginRepositoryAuthProvider
import com.intellij.openapi.components.service

class MarketplaceRepositoryHeaderProvider : PluginRepositoryAuthProvider {

    private val marketplaceRepositoryHeaderSettings = service<MarketplaceRepositoryHeaderSettings>()

    override fun getAuthHeaders(url: String?) = marketplaceRepositoryHeaderSettings.getHeaders()

    override fun canHandle(url: String?) = marketplaceRepositoryHeaderSettings.isCustomHostSet()

}