package de.imedia24.shop.config

import org.springframework.http.HttpInputMessage
import org.springframework.http.HttpOutputMessage
import org.springframework.http.MediaType
import org.springframework.http.converter.AbstractHttpMessageConverter
import org.springframework.lang.Nullable
import org.springframework.stereotype.Component
import javax.json.Json
import javax.json.JsonMergePatch
import javax.json.JsonReader

@Component
class JsonMergePatchHttpMessageConverter : AbstractHttpMessageConverter<JsonMergePatch>(MediaType.parseMediaType("application/merge-patch+json")) {
    override fun supports(clazz: Class<*>): Boolean {
        return JsonMergePatch::class.java.isAssignableFrom(clazz)
    }

    override fun readInternal(clazz: Class<out JsonMergePatch>, inputMessage: HttpInputMessage): JsonMergePatch {
        val reader: JsonReader = Json.createReader(inputMessage.getBody())
        return Json.createMergePatch(reader.readValue())
    }

    override fun writeInternal(t: JsonMergePatch, outputMessage: HttpOutputMessage) {
        val writer = Json.createWriter(outputMessage.getBody())
        writer.write(t.toJsonValue())
    }
}