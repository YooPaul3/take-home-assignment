package org.game.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.game.model.Config;

import java.io.FileReader;
import java.io.IOException;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS;

public class ConfigDeserializer {

    public Config deserialize(String configFilePath) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.enable(ACCEPT_CASE_INSENSITIVE_ENUMS);

        try (FileReader fileReader = new FileReader(configFilePath)) {
            return mapper.readValue(fileReader, Config.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
