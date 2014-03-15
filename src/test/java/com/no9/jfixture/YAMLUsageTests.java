package com.no9.jfixture;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class YAMLUsageTests {
    private Yaml yaml = new Yaml();

    @Test
    public void should_be_able_to_parse_a_yaml_file_with_the_correct_data_types() {
        String yamlInput = "TopLevel:\n" +
                "   string: Hello\n" +
                "   int: 123\n" +
                "   float: 3.1415926\n" +
                "   boolean: true\n";

        Map map = (Map) yaml.load(yamlInput);

        assertNotNull(map.get("TopLevel"));
        assertEquals("Hello", ((Map) map.get("TopLevel")).get("string"));
        assertEquals(123, ((Map) map.get("TopLevel")).get("int"));
        assertEquals(3.1415926, ((Map) map.get("TopLevel")).get("float"));
        assertEquals(true, ((Map) map.get("TopLevel")).get("boolean"));
    }

    @Test
    public void should_be_able_parse_a_yaml_file_with_a_list_of_content() {
        String yamlInput = "John:\n" +
                "   surname: Smith\n" +
                "   age: 34\n" +
                "Mary:\n" +
                "   surname: Lamb\n" +
                "   age: 32";

        Map map = (Map) yaml.load(yamlInput);

        assertNotNull(map.get("John"));
        assertNotNull(map.get("Mary"));

        assertEquals("Smith", ((Map) map.get("John")).get("surname"));
        assertEquals(34, ((Map) map.get("John")).get("age"));
        assertEquals("Lamb", ((Map) map.get("Mary")).get("surname"));
        assertEquals(32, ((Map) map.get("Mary")).get("age"));
    }
}
