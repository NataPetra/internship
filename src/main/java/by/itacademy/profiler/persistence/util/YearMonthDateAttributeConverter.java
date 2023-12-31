package by.itacademy.profiler.persistence.util;

import jakarta.persistence.AttributeConverter;

import java.sql.Date;
import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneId;

import static by.itacademy.profiler.persistence.util.PersistenceConstants.EUROPE_MINSK;

public class YearMonthDateAttributeConverter implements AttributeConverter<YearMonth, Date> {

    @Override
    public Date convertToDatabaseColumn(YearMonth attribute) {
        if (attribute != null) {
            return Date.valueOf(
                    attribute.atDay(1)
            );
        }
        return null;
    }

    @Override
    public YearMonth convertToEntityAttribute(Date dbData) {
        if (dbData != null) {
            return YearMonth.from(
                    Instant
                            .ofEpochMilli(dbData.getTime())
                            .atZone(ZoneId.of(EUROPE_MINSK))
                            .toLocalDate()
            );
        }
        return null;
    }
}
