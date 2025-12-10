package io.github.stardomains3.oxproxion

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.put

object VariableSubstitution {

    private val DATE_FMT: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE // YYYY-MM-DD
    private val TIME_FMT: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm") // 24h hours:minutes

    data class DateTimeParts(val date: String, val time: String)

    fun formatDateTime(now: OffsetDateTime = OffsetDateTime.now()): DateTimeParts {
        val date = now.format(DATE_FMT)
        val time = now.format(TIME_FMT)
        return DateTimeParts(date, time)
    }

    fun substituteVariables(input: String?, now: OffsetDateTime? = null): String? {
        if (input == null) return null
        if (input.isEmpty()) return input
        val odt = now ?: OffsetDateTime.now()
        val parts = formatDateTime(odt)
        return input
            .replace(Regex("\\{\\{\\s*date\\s*}}", RegexOption.IGNORE_CASE), parts.date)
            .replace(Regex("\\{\\{\\s*time\\s*}}", RegexOption.IGNORE_CASE), parts.time)
    }

    fun substituteJsonContent(elem: JsonElement, now: OffsetDateTime? = null): JsonElement {
        val odt = now ?: OffsetDateTime.now()
        return when (elem) {
            is JsonPrimitive -> {
                if (elem.isString) {
                    val replaced = substituteVariables(elem.content, odt) ?: elem.content
                    JsonPrimitive(replaced)
                } else elem
            }
            is JsonArray -> {
                buildJsonArray {
                    elem.forEach { item ->
                        if (item is JsonObject) {
                            val type = item["type"]?.jsonPrimitive?.contentOrNull
                            if (type == "text") {
                                val originalText = item["text"]?.jsonPrimitive?.contentOrNull ?: ""
                                val newText = substituteVariables(originalText, odt) ?: originalText
                                add(
                                    buildJsonObject {
                                        item.forEach { (k, v) ->
                                            if (k == "text") put(k, JsonPrimitive(newText)) else put(k, v)
                                        }
                                    }
                                )
                            } else {
                                add(item)
                            }
                        } else {
                            add(item)
                        }
                    }
                }
            }
            else -> elem
        }
    }
}
