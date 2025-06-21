package ru.practicum.shareit.config;

import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            builder.simpleDateFormat("yyyy-MM-dd");
            builder.serializers(LOCAL_DATE_SERIALIZER);
            builder.deserializers(LOCAL_DATE_DESERIALIZER);
        };
    }

    private static final JsonSerializer<LocalDate> LOCAL_DATE_SERIALIZER =
        new LocalDateSerializer(DateTimeFormatter.ISO_DATE);

    private static final JsonDeserializer<LocalDate> LOCAL_DATE_DESERIALIZER =
        new LocalDateDeserializer(DateTimeFormatter.ISO_DATE);
}