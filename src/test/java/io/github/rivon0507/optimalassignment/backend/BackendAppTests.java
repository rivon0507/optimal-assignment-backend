package io.github.rivon0507.optimalassignment.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

@SpringBootTest
class BackendAppTests {

    @Test
    void contextLoads() {
    }

    @Test
    void applicationLocalPropertiesCheck() throws IOException {
        Properties local = new Properties();
        Properties example = new Properties();

        local.load(new ClassPathResource("application-local.properties").getInputStream());
        example.load(new ClassPathResource("application-local.example.properties").getInputStream());

        Set<String> localProperties = new HashSet<>(local.stringPropertyNames());
        Set<String> exampleProperties = new HashSet<>(example.stringPropertyNames());
        localProperties.removeAll(example.stringPropertyNames());
        exampleProperties.removeAll(local.stringPropertyNames());

        assertIterableEquals(
                local.stringPropertyNames(),
                example.stringPropertyNames(),
                "application-local.properties and application-local.example.properties should contain the same set of keys.\n" +
                "Missing keys in local: " + localProperties + "\n" +
                "Missing keys in example: " + exampleProperties + "\n"
        );
    }
}
