package com.dony.dramatest.util

import com.google.gson.*
import java.lang.reflect.Type
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateDeserializer : JsonDeserializer<Date>, JsonSerializer<Date> {
    private val format = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";

    override fun serialize(src: Date?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement? {
        return if (src == null) null else JsonPrimitive(SimpleDateFormat(format).format(src))
    }

    override fun deserialize(element: JsonElement, arg1: Type, arg2: JsonDeserializationContext): Date? {
        val date = element.asString
        val format = SimpleDateFormat(format)
        return try {
            format.parse(date)
        } catch (exp: ParseException) {
            null
        }
    }
}