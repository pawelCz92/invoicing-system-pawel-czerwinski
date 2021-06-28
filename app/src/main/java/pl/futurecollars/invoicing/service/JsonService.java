package pl.futurecollars.invoicing.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;

@Service
public class JsonService {

    private final ObjectMapper objectMapper;

    public JsonService() {
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public <T> String objectToString(T object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            System.out.println("Serialization from object to string filed!");
        }
        return "";
    }

    public <T> T stringToObject(String jsonObject, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonObject, clazz);
        } catch (JsonProcessingException e) {
            System.out.println("Serialization from string to object filed!");
        }
        return null;
    }
}
