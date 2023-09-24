package com.net128.test.entitysort.util

object TextUtil {
    fun indent(n: Int, text: String, indentString: String = "    ") : String {
        return indentString.repeat(n)+text
    }
}