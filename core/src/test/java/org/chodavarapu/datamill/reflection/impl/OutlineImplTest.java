package org.chodavarapu.datamill.reflection.impl;

import org.chodavarapu.datamill.reflection.Outline;
import org.chodavarapu.datamill.reflection.OutlineBuilder;
import org.chodavarapu.datamill.values.StringValue;
import org.junit.Test;

import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;

/**
 * @author Ravi Chodavarapu (rchodava@gmail.com)
 */
public class OutlineImplTest {
    private int actualBeanMethodInvocations = 0;

    public class TestBeanClass {
        private String readWriteProperty;
        public String getReadWriteProperty() {
            actualBeanMethodInvocations++;
            return readWriteProperty;
        }

        public boolean isBooleanProperty() {
            actualBeanMethodInvocations++;
            return false;
        }

        public String getReadOnlyProperty() {
            actualBeanMethodInvocations++;
            return "";
        }

        public void setReadWriteProperty(String value) {
            actualBeanMethodInvocations++;
            this.readWriteProperty = value;
        }

        public void nonPropertyMethod() {

        }
    }

    public class TestBeanClassWithVariousProperties {
        private boolean booleanProperty;
        private byte byteProperty;
        private char charProperty;
        private short shortProperty;
        private int intProperty;
        private long longProperty;
        private float floatProperty;
        private double doubleProperty;
        private String stringProperty;

        public boolean isBooleanProperty() {
            return booleanProperty;
        }

        public byte getByteProperty() {
            return byteProperty;
        }

        public char getCharProperty() {
            return charProperty;
        }

        public short getShortProperty() {
            return shortProperty;
        }

        public int getIntProperty() {
            return intProperty;
        }

        public long getLongProperty() {
            return longProperty;
        }

        public float getFloatProperty() {
            return floatProperty;
        }

        public double getDoubleProperty() {
            return doubleProperty;
        }

        public String getStringProperty() {
            return stringProperty;
        }

        public void setBooleanProperty(boolean booleanProperty) {
            this.booleanProperty = booleanProperty;
        }

        public void setByteProperty(byte byteProperty) {
            this.byteProperty = byteProperty;
        }

        public void setCharProperty(char charProperty) {
            this.charProperty = charProperty;
        }

        public void setShortProperty(short shortProperty) {
            this.shortProperty = shortProperty;
        }

        public void setIntProperty(int intProperty) {
            this.intProperty = intProperty;
        }

        public void setLongProperty(long longProperty) {
            this.longProperty = longProperty;
        }

        public void setFloatProperty(float floatProperty) {
            this.floatProperty = floatProperty;
        }

        public void setDoubleProperty(double doubleProperty) {
            this.doubleProperty = doubleProperty;
        }

        public void setStringProperty(String stringProperty) {
            this.stringProperty = stringProperty;
        }
    }

    @Test
    public void camelCasedNames() {
        OutlineBuilder<TestBeanClass> outlineBuilder = new OutlineBuilder<>(TestBeanClass.class);
        Outline<TestBeanClass> outline = outlineBuilder.defaultSnakeCased().build();

        assertEquals("readOnlyProperty", outline.camelCasedName(outline.members().getReadOnlyProperty()));
        assertEquals("booleanProperty", outline.camelCasedName(outline.members().isBooleanProperty()));
        assertEquals("readWriteProperty", outline.camelCasedName(members -> members.setReadWriteProperty("")));

        assertEquals("TestBeanClass", outline.camelCasedName());
        assertEquals("TestBeanClasses", outline.camelCasedPluralName());

        assertEquals(0, actualBeanMethodInvocations);
    }

    @Test
    public void methods() {
        OutlineBuilder<TestBeanClass> outlineBuilder = new OutlineBuilder<>(TestBeanClass.class);
        Outline<TestBeanClass> outline = outlineBuilder.defaultSnakeCased().build();

        // Test all methods in class are present
        assertEquals(5, outline.methods().stream().mapToInt(m -> {
            switch (m.getName()) {
                case "getReadOnlyProperty":
                    return 1;
                case "isBooleanProperty":
                    return 1;
                case "getReadWriteProperty":
                    return 1;
                case "setReadWriteProperty":
                    return 1;
                case "nonPropertyMethod":
                    return 1;
                default:
                    return 0;
            }
        }).sum());
    }

    @Test
    public void propertyNamesCamelCased() {
        assertThat(new OutlineBuilder<>(TestBeanClass.class).defaultCamelCased().build().propertyNames(),
                hasItems("readWriteProperty", "readOnlyProperty", "booleanProperty"));

        assertEquals(0, actualBeanMethodInvocations);
    }

    @Test
    public void propertyNamesSnakeCased() {
        assertThat(new OutlineBuilder<>(TestBeanClass.class).defaultSnakeCased().build().propertyNames(),
                hasItems("read_write_property", "read_only_property", "boolean_property"));

        assertEquals(0, actualBeanMethodInvocations);
    }

    @Test
    public void getPropertiesCamelCasedDoesNotHaveGetClass() {
        assertThat(new OutlineBuilder<>(TestBeanClass.class).defaultCamelCased().build().properties().stream()
                        .map(p -> p.getName()).collect(Collectors.toList()),
                not(hasItems("class")));

        assertEquals(0, actualBeanMethodInvocations);
    }

    @Test
    public void getPropertiesCamelCased() {
        assertThat(new OutlineBuilder<>(TestBeanClass.class).defaultCamelCased().build().properties().stream()
                        .map(p -> p.getName()).collect(Collectors.toList()),
                hasItems("readWriteProperty", "readOnlyProperty", "booleanProperty"));

        assertEquals(0, actualBeanMethodInvocations);
    }

    @Test
    public void getPropertiesSnakeCased() {
        assertThat(new OutlineBuilder<>(TestBeanClass.class).defaultSnakeCased().build().properties().stream()
                        .map(p -> p.getName()).collect(Collectors.toList()),
                hasItems("read_write_property", "read_only_property", "boolean_property"));

        assertEquals(0, actualBeanMethodInvocations);
    }

    @Test
    public void getPropertyCamelCased() {
        Outline<TestBeanClass> outline = new OutlineBuilder<>(TestBeanClass.class).defaultCamelCased().build();
        assertEquals("readWriteProperty", outline.property(outline.members().getReadWriteProperty()).getName());
        assertEquals("booleanProperty", outline.property(outline.members().isBooleanProperty()).getName());
        assertEquals("readWriteProperty", outline.property(outline.members().getReadWriteProperty()).getName());

        assertEquals(0, actualBeanMethodInvocations);
    }

    @Test
    public void getPropertySnakeCased() {
        Outline<TestBeanClass> outline = new OutlineBuilder<>(TestBeanClass.class).defaultSnakeCased().build();
        assertEquals("read_write_property", outline.property(outline.members().getReadWriteProperty()).getName());
        assertEquals("boolean_property", outline.property(outline.members().isBooleanProperty()).getName());
        assertEquals("read_write_property", outline.property(outline.members().getReadWriteProperty()).getName());

        assertEquals(0, actualBeanMethodInvocations);
    }

    @Test
    public void snakeCasedNames() {
        OutlineBuilder<TestBeanClass> outlineBuilder = new OutlineBuilder<>(TestBeanClass.class);
        Outline<TestBeanClass> outline = outlineBuilder.defaultCamelCased().build();

        assertEquals("read_only_property", outline.snakeCasedName(outline.members().getReadOnlyProperty()));
        assertEquals("boolean_property", outline.snakeCasedName(outline.members().isBooleanProperty()));
        assertEquals("read_write_property", outline.snakeCasedName(members -> members.setReadWriteProperty("")));

        assertEquals("test_bean_class", outline.snakeCasedName());
        assertEquals("test_bean_classes", outline.snakeCasedPluralName());

        assertEquals(0, actualBeanMethodInvocations);
    }

    @Test
    public void wrapAndBeanGet() {
        OutlineBuilder<TestBeanClass> outlineBuilder = new OutlineBuilder<>(TestBeanClass.class);
        Outline<TestBeanClass> outline = outlineBuilder.defaultCamelCased().build();

        TestBeanClass instance = new TestBeanClass();
        instance.setReadWriteProperty("value1");

        assertEquals("value1", outline.wrap(instance).get(outline.members().getReadWriteProperty()));
        assertEquals(2, actualBeanMethodInvocations);
    }

    @Test
    public void wrapAndBeanSet() {
        OutlineBuilder<TestBeanClass> outlineBuilder = new OutlineBuilder<>(TestBeanClass.class);
        Outline<TestBeanClass> outline = outlineBuilder.defaultCamelCased().build();

        TestBeanClass instance = new TestBeanClass();
        outline.wrap(instance).set(outline.members().getReadWriteProperty(), "value1");

        assertEquals(1, actualBeanMethodInvocations);
        assertEquals("value1", instance.getReadWriteProperty());
    }

    @Test
    public void wrapAndBeanSetCastedValue() {
        OutlineBuilder<TestBeanClassWithVariousProperties> outlineBuilder =
                new OutlineBuilder<>(TestBeanClassWithVariousProperties.class);
        Outline<TestBeanClassWithVariousProperties> outline = outlineBuilder.defaultCamelCased().build();

        TestBeanClassWithVariousProperties instance = new TestBeanClassWithVariousProperties();
        outline.wrap(instance)
                .set(outline.members().isBooleanProperty(), new StringValue("true"))
                .set(outline.members().getByteProperty(), new StringValue("10"))
                .set(outline.members().getCharProperty(), new StringValue("c"))
                .set(outline.members().getDoubleProperty(), new StringValue("1.0"))
                .set(outline.members().getFloatProperty(), new StringValue("2.0"))
                .set(outline.members().getIntProperty(), new StringValue("1"))
                .set(outline.members().getLongProperty(), new StringValue("2"))
                .set(outline.members().getShortProperty(), new StringValue("3"))
                .set(outline.members().getStringProperty(), new StringValue("string"));

        assertEquals(true, instance.isBooleanProperty());
        assertEquals(10, instance.getByteProperty());
        assertEquals('c', instance.getCharProperty());
        assertEquals(1.0d, instance.getDoubleProperty(), 0.1);
        assertEquals(2.0f, instance.getFloatProperty(), 0.1);
        assertEquals(1, instance.getIntProperty());
        assertEquals(2, instance.getLongProperty());
        assertEquals(3, instance.getShortProperty());
        assertEquals("string", instance.getStringProperty());
    }

    @Test
    public void unwrapWrappedBean() {
        OutlineBuilder<TestBeanClass> outlineBuilder = new OutlineBuilder<>(TestBeanClass.class);
        Outline<TestBeanClass> outline = outlineBuilder.defaultCamelCased().build();

        TestBeanClass instance = new TestBeanClass();

        assertEquals(instance, outline.wrap(instance).unwrap());
    }
}