package moe.kurenai.multidbdemo

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource

class RoutingDataSource: AbstractRoutingDataSource() {

    companion object {
        private val CONTEXT = ThreadLocal<String>()

        fun setCurrentCatalog(catalog: String) {
            CONTEXT.set(catalog)
        }

        fun clear() {
            CONTEXT.remove()
        }
    }

    override fun determineCurrentLookupKey(): String {
        return CONTEXT.get()?:"NULL"
    }
}