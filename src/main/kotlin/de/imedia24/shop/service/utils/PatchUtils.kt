package de.imedia24.shop.service.utils

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr353.JSR353Module
import org.springframework.stereotype.Component
import javax.json.JsonMergePatch
import javax.json.JsonValue
import javax.validation.ConstraintViolationException
import javax.validation.Validator


@Component
class PatchUtils constructor(
    val validator: Validator, val mapper: ObjectMapper = ObjectMapper()
        .setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL)
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
) {

    fun <T> mergePatch(mergePatch: JsonMergePatch, targetBean: T, beanClass: Class<T>): T {
        mapper.registerModule(JSR353Module())
        val target: JsonValue = mapper.convertValue(targetBean, JsonValue::class.java)
        val patched: JsonValue = applyMergePatch(mergePatch, target)
        return convertAndValidate(patched, beanClass)
    }

    private fun <T> convertAndValidate(patched: JsonValue, beanClass: Class<T>): T {
        val bean: T = mapper.convertValue(patched, beanClass)
        validate(bean)
        return bean
    }

    private fun <T> validate(bean: T) {

        val violations = validator.validate(bean)

        if (!violations.isEmpty()) {
            throw ConstraintViolationException(violations)
        }
    }


    private fun applyMergePatch(mergePatch: JsonMergePatch, target: JsonValue): JsonValue {
        try {
            return mergePatch.apply(target)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }
}
