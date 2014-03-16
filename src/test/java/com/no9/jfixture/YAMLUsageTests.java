package com.no9.jfixture;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

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

    @Test
    public void should_be_able_iterate_through_all_the_yaml_entries() {
        String yamlInput = "- \n" +
                "   John: blah\n" +
                "   surname: Smith\n" +
                "   age: 34\n" +
                "- \n" +
                "   Mary: blah\n" +
                "   surname: Lamb\n" +
                "   age: 32";

        Iterator<Object> objects = yaml.loadAll(yamlInput).iterator();

        List<Object> people = (List<Object>) objects.next();

        Map johnItem = (Map) people.get(0);
        System.out.println(johnItem);
        assertEquals("Smith", johnItem.get("surname"));
        assertEquals(34, johnItem.get("age"));

        Map maryItem = (Map) people.get(1);
        assertEquals("Lamb", maryItem.get("surname"));
        assertEquals(32, maryItem.get("age"));

        assertFalse(objects.hasNext());
    }

    @Test
    public void should_be_the_blueprint_how_to_disassemble_yaml_content_for_a_fixture_control_file() {
        String yamlInput = "- echo:\n" +
                "   message: Hello World\n" +
                "- echo:\n" +
                "   to: stdout\n" +
                "   messages:\n" +
                "       - Bye bye love\n" +
                "       - Take it easy\n" +
                "---\n" +
                "- echo:\n" +
                "   - message: it is almost time\n" +
                "   - message: it is certainly time\n" +
                "   - message: let's live again";

        Iterator<Object> objects = yaml.loadAll(yamlInput).iterator();

        List<Object> firstList = (List<Object>) objects.next();
        Map firstEcho = (Map) firstList.get(0);
        assertEquals(1, firstEcho.size());
        assertEquals(1, ((Map) firstEcho.get("echo")).size());
        assertEquals("Hello World", ((Map) firstEcho.get("echo")).get("message"));

        Map secondEcho = (Map) firstList.get(1);
        assertEquals(1, secondEcho.size());

        assertEquals(2, firstList.size());

        List<Object> secondList = (List<Object>) objects.next();
        Map thirdEcho = (Map) secondList.get(0);
        assertEquals(1, thirdEcho.size());

        assertEquals(1, secondList.size());

        assertFalse(objects.hasNext());
    }
}
