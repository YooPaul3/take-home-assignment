package org.game.services;

import org.game.model.Config;
import org.game.testdata.ConfigTestData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigDeserializerTest {

    private ConfigDeserializer configDeserializer;

    @BeforeEach
    void setUp() {
        configDeserializer = new ConfigDeserializer();
    }

    @Test
    public void shouldDeserializeConfigFromGivenFile() {
        Config config = configDeserializer.deserialize("src/test/resources/config.json");

        assertEquals(ConfigTestData.config, config);
    }

}