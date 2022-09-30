package ru.sadv1r.idea.plugin.marketplace.repository.header.provider

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

class MarketplaceRepositoryHeaderConfigurable : BoundConfigurable("Marketplace Repository"), SearchableConfigurable {

    companion object {
        val logger = logger<MarketplaceRepositoryHeaderConfigurable>()
    }

    override fun createPanel(): DialogPanel {
        val marketplaceRepositoryHeaderSettings = service<MarketplaceRepositoryHeaderSettings>()

        return panel {
            row("Host:") {
                textField()
                        .horizontalAlign(HorizontalAlign.FILL)
                        .bindText(
                                getter = { marketplaceRepositoryHeaderSettings.getHost() },
                                setter = { marketplaceRepositoryHeaderSettings.setHost(it) }
                        )
                        .validationOnInput {
                            validateHost(it)
                        }
                        .validationOnApply { //FIXME Not working
                            validateHost(it)
                        }
                        .enabled(false) //TODO Find a way to set host and enable
                        .comment("Please use idea.plugins.host property")

            }

            row {
                label("Headers:")
            }
            row {
                marketplaceRepositoryHeaderSettings.getHeaders().let { headers ->
                    val atomicProperty = AtomicProperty(headers)

                    atomicProperty.afterChange {
                        marketplaceRepositoryHeaderSettings.setHeaders(it) //FIXME Not working without this even with bindProperties
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

    override fun getId() = "ru.sadv1r.idea.plugin.marketplace.repository.header.provider.MarketplaceRepositoryHeaderConfigurable"

    private fun ValidationInfoBuilder.validateHost(it: JBTextField) =
            when {
                it.text.isBlank() -> null
                URLUtil.URL_PATTERN.matcher(it.text).matches() -> null
                else -> error("Invalid URL")
            }
}