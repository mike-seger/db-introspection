package com.net128.test.entitysort.data.dbml

import com.fasterxml.jackson.annotation.JsonInclude

object Dbml {
    private val typeMap = mapOf(
        "CHARACTER VARYING" to "VARCHAR",
        "DOUBLE PRECISION" to "DOUBLE"
    )

    fun dbmlTablesToString(tables: List<DbmlTable>): String {
        val builder = StringBuilder()
        for (table in tables) {
            builder.append("Table ${table.name.uppercase()} {\n")
            for (column in table.columns) {
                column.isPrimaryKey
                builder.append("  ${column.name} ${column.type.lowercase()}${
                    if (column.isPrimaryKey == true) " [pk]" else ""}\n")
            }
            builder.append("}\n")
            for (reference in table.references.sortedBy { it.toString() }) {
                builder.append("Ref: ${table.name.uppercase()}.${reference.fromColumn} ${reference.refType} ${reference.toTable.uppercase()}.${reference.toColumn}\n")
            }
            if(table.references.isNotEmpty()) builder.append("\n")
        }

        return builder.toString()
    }

    fun mapType(type: String) : String {
        return if(typeMap[type]!=null) typeMap[type]!! else type
    }
}

enum class RefType(private val symbol: String) {
    OneToOne("-"),
    OneToMany("<"),
    ManyToOne(">"),
    ManyToMany("<>");

    override fun toString(): String { return symbol }
}

data class DbmlReference(
    val fromTable: String,
    val fromColumn: String,
    val toTable: String,
    val toColumn: String,
    val refType: RefType
)

data class DbmlTable(
    val name: String,
    val columns: List<DbmlColumn>,
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val references: List<DbmlReference> = listOf()
)

data class DbmlColumn(
    val name: String,
    val type: String,
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val isPrimaryKey: Boolean? = null
)

