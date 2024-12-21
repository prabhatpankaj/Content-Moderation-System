package com.techbellys.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MapperUtil {

    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }

    public static <T> T mapper(Object object, Class<T> objectType) {
        if (object == null) {
            return null;
        }
        return OBJECT_MAPPER.convertValue(object, objectType);
    }

    public static <T> T getObjectFromJsonType(String json, TypeReference<T> typeReference) {
        T response = null;
        try {
            if (json != null) {
                response = OBJECT_MAPPER.readValue(json, typeReference);
            }
        } catch (Exception e) {
            log.warn(
                    "JsonProcessingException exception occurred while paring Json  :{} ",
                    e.getMessage());
        }
        return response;
    }

    public static <T> T getObjectFromObject(Object json, TypeReference<T> typeReference) {
        try {
            return OBJECT_MAPPER.convertValue(json, typeReference);
        } catch (Exception e) {
            log.warn(
                    "JsonProcessingException exception occurred while paring Json  :{} ",
                    e.getMessage());
            return null;
        }
    }

    public static <T> T getObjectFromObjectV2(Object json, Class<T> toValueType) {
        try {
            return OBJECT_MAPPER.convertValue(json, toValueType);
        } catch (Exception e) {
            log.warn(
                    "JsonProcessingException exception occurred while paring Json  :{} ",
                    e.getMessage());
            return null;
        }
    }

    public static String getStringFromObject(Object map) {
        try {
            return OBJECT_MAPPER.writeValueAsString(map);
        } catch (Exception e) {
            log.warn(
                    "JsonProcessingException exception occurred while parsing map to string  :{} ",
                    e.getMessage());
            return null;
        }
    }
}