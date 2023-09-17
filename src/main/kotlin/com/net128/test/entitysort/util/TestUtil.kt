package com.net128.test.entitysort.util

object TestUtil {
    fun indent(n: Int, text: String, indentString: String = "    ") : String {
        return indentString.repeat(n)+text
    }
}