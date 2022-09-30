package ru.sadv1r.idea.plugin.plugin.repository.auth.provider

import com.intellij.execution.util.setEmptyState
import com.intellij.openapi.components.service
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.externalSystem.service.ui.properties.PropertiesTable
import com.intellij.openapi.observable.properties.AtomicProperty
import com.intellij.openapi.observable.util.transform
import com.intellij.openapi.options.BoundConfigurable
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.ui.DialogPanel
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.bindText
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.dsl.gridLayout.HorizontalAlign
import com.intellij.ui.layout.ValidationInfoBuilder
import com.intellij.util.io.URLUtil

class MarketplacePluginRepositoryHeadersConfigurable : BoundConfigurable("Marketplace Plugin Repository Headers"), SearchableConfigurable {

    companion object {
        val logger = logger<MarketplacePluginRepositoryHeadersConfigurable>()
    }

    override fun createPanel(): DialogPanel {
        val marketplacePluginRepositoryHeadersSettings = service<MarketplacePluginRepositoryHeadersSettings>()

        return panel {
            row("Host:") {
                textField()
                        .horizontalAlign(HorizontalAlign.FILL)
                        .bindText(
                                getter = { marketplacePluginRepositoryHeadersSettings.getHost() },
                                setter = { marketplacePluginRepositoryHeadersSettings.setHost(it) }
                        )
                        .validationOnInput {
                            validateHost(it)
                        }
                        .validationOnApply { //FIXME Not working
                            validateHost(it)
                        }
                        .enabled(false) //TODO Find a way to set host and enable
            }

            row {
                label("Headers:")
            }
            row {
                marketplacePluginRepositoryHeadersSettings.getHeaders().let { headers ->
                    val atomicProperty = AtomicProperty(headers)

                    atomicProperty.afterChange {
                        marketplacePluginRepositoryHeadersSettings.setHeaders(it) //FIXME Not working without this even with bindProperties
                    }

                    val headersTable = PropertiesTable()
                            .setEmptyState("No headers")
                            .bindProperties(atomicProperty.transform(
                                    { it.map { (k, v) -> PropertiesTable.Property(k, v) } },
                                    { it.associate { (n, v) -> n to v } }
                            ))

                    cell(headersTable.component)
                            .horizontalAlign(HorizontalAlign.FILL)
                    //                        .enabled(marketplacePluginRepositoryAuthSettings.isCustomHostSet()) //FIXME Not working

                }
            }
        }
    }

    override fun getId() = "marketplace.plugin.repository.headers.provider"

    private fun ValidationInfoBuilder.validateHost(it: JBTextField) =
            when {
                it.text.isBlank() -> null
                URLUtil.URL_PATTERN.matcher(it.text).matches() -> null
                else -> error("Invalid URL")
            }
}