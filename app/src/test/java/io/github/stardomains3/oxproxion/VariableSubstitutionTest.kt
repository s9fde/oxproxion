package io.github.stardomains3.oxproxion

import org.junit.Assert.*
import org.junit.Test
import java.time.OffsetDateTime
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put

class VariableSubstitutionTest {

    @Test
    fun testFormatAndSubstitute() {
        val now = OffsetDateTime.parse("2025-12-10T15:04:00Z")
        val parts = VariableSubstitution.formatDateTime(now)
        assertEquals("2025-12-10", parts.date)
        assertEquals("15:04", parts.time)

        val template = "Today is {{DATE}} at {{time}}"
        val out = VariableSubstitution.substituteVariables(template, now)
        assertTrue(out!!.contains("2025-12-10"))
        assertTrue(out.contains("15:04"))
    }

    @Test
    fun testJsonArraySubstitution() {
        val now = OffsetDateTime.parse("2025-12-10T09:07:00Z")
        val arr = buildJsonArray {
            add(buildJsonObject {
                put("type", JsonPrimitive("text"))
                put("text", JsonPrimitive("Date: {{date}}"))
            })
            add(buildJsonObject {
                put("type", JsonPrimitive("image_url"))
                put("image_url", buildJsonObject { put("url", JsonPrimitive("data:...")) })
            })
        }
        val out = VariableSubstitution.substituteJsonContent(arr, now)
        // assert first element text replaced; second unchanged
        val firstText = out.jsonArray[0].jsonObject["text"]?.jsonPrimitive?.content
        assertEquals("Date: 2025-12-10", firstText)
        val secondType = out.jsonArray[1].jsonObject["type"]?.jsonPrimitive?.content
        assertEquals("image_url", secondType)
    }
}
