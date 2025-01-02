package infra.config

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Converter(autoApply = true)
class LocalDateTimeConverter : AttributeConverter<LocalDateTime, String> {
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    override fun convertToDatabaseColumn(attribute: LocalDateTime?): String? {
        return attribute?.format(formatter)
    }

    override fun convertToEntityAttribute(dbData: String?): LocalDateTime? {
        return dbData?.let { LocalDateTime.parse(it, formatter) }
    }
}