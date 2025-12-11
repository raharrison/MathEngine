package uk.co.ryanharrison.mathengine.unitconversion.units;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;
import uk.co.ryanharrison.mathengine.core.BigRational;
import uk.co.ryanharrison.mathengine.unitconversion.units.currency.Currency;
import uk.co.ryanharrison.mathengine.unitconversion.units.timezones.TimeZones;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class UnitGroupLoader {
    private static final String DEFAULT_UNITS_FILE = "units.xml";

    private UnitGroupLoader() {
    }

    public static List<UnitGroup<?>> loadFromClasspath() {
        return loadFromClasspath(DEFAULT_UNITS_FILE);
    }

    public static List<UnitGroup<?>> loadFromClasspath(String resourcePath) {
        try (InputStream inputStream = UnitGroupLoader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("Resource not found: " + resourcePath);
            }
            return parseUnitsXml(inputStream);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load unit groups from " + resourcePath, e);
        }
    }

    private static List<UnitGroup<?>> parseUnitsXml(InputStream inputStream) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        XmlUnitHandler handler = new XmlUnitHandler();
        parser.parse(inputStream, handler);
        return handler.getGroups();
    }

    private static class XmlUnitHandler extends DefaultHandler {
        private enum GroupType {SIMPLE, COMPLEX}

        private final List<UnitGroup<?>> groups = new ArrayList<>();
        private final Map<String, CustomGroupFactory> customGroupFactories = new HashMap<>();

        private GroupType currentGroupType;
        private String currentGroupName;
        private List<SimpleUnit> simpleUnits;
        private List<ComplexUnit> complexUnits;

        private SimpleUnit.Builder simpleUnitBuilder;
        private ComplexUnit.Builder complexUnitBuilder;
        private String currentConversionTarget;
        private String currentVariable;
        private StringBuilder textContent;

        public XmlUnitHandler() {
            customGroupFactories.put("currency", Currency::withUnits);
            customGroupFactories.put("time zones", TimeZones::withUnits);
        }

        @Override
        public void startDocument() {
            textContent = new StringBuilder();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            textContent.setLength(0);

            switch (qName.toLowerCase()) {
                case "group" -> {
                    String type = attributes.getValue("type");
                    currentGroupType = "complex".equalsIgnoreCase(type) ? GroupType.COMPLEX : GroupType.SIMPLE;
                    simpleUnits = new ArrayList<>();
                    complexUnits = new ArrayList<>();
                    currentGroupName = null;
                }
                case "unit" -> {
                    if (currentGroupType == GroupType.SIMPLE) {
                        simpleUnitBuilder = SimpleUnit.builder();
                    } else {
                        complexUnitBuilder = ComplexUnit.builder();
                    }
                }
                case "conversion" -> {
                    if (currentGroupType == GroupType.COMPLEX) {
                        currentConversionTarget = attributes.getValue("to");
                        currentVariable = attributes.getValue("var");
                    }
                }
            }
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            String content = textContent.toString().trim();

            switch (qName.toLowerCase()) {
                case "group" -> {
                    UnitGroup<?> group = createGroup();
                    if (group != null) {
                        groups.add(group);
                    }
                    currentGroupName = null;
                    simpleUnits = null;
                    complexUnits = null;
                }
                case "name" -> currentGroupName = content;
                case "unit" -> {
                    if (currentGroupType == GroupType.SIMPLE && simpleUnitBuilder != null) {
                        simpleUnits.add(simpleUnitBuilder.build());
                        simpleUnitBuilder = null;
                    } else if (currentGroupType == GroupType.COMPLEX && complexUnitBuilder != null) {
                        complexUnits.add(complexUnitBuilder.build());
                        complexUnitBuilder = null;
                    }
                }
                case "singular" -> {
                    if (simpleUnitBuilder != null) {
                        simpleUnitBuilder.singular(content);
                    } else if (complexUnitBuilder != null) {
                        complexUnitBuilder.singular(content);
                    }
                }
                case "plural" -> {
                    if (simpleUnitBuilder != null) {
                        simpleUnitBuilder.plural(content);
                    } else if (complexUnitBuilder != null) {
                        complexUnitBuilder.plural(content);
                    }
                }
                case "alias" -> {
                    if (simpleUnitBuilder != null) {
                        simpleUnitBuilder.alias(content);
                    } else if (complexUnitBuilder != null) {
                        complexUnitBuilder.alias(content);
                    }
                }
                case "conversion" -> {
                    if (currentGroupType == GroupType.SIMPLE && simpleUnitBuilder != null) {
                        if (!content.isEmpty()) {
                            simpleUnitBuilder.conversionFactor(BigRational.of(content));
                        }
                    } else if (currentGroupType == GroupType.COMPLEX && complexUnitBuilder != null) {
                        if (!content.isEmpty()) {
                            complexUnitBuilder.conversionEquation(currentConversionTarget, content);
                            if (currentVariable != null) {
                                complexUnitBuilder.variable(currentVariable);
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) {
            textContent.append(ch, start, length);
        }

        private UnitGroup<?> createGroup() {
            if (currentGroupName == null) {
                return null;
            }

            CustomGroupFactory customFactory = customGroupFactories.get(currentGroupName.toLowerCase());
            if (customFactory != null) {
                return customFactory.create(simpleUnits);
            }

            if (currentGroupType == GroupType.SIMPLE) {
                return SimpleUnitGroup.of(currentGroupName, simpleUnits);
            } else {
                return ComplexUnitGroup.of(currentGroupName, complexUnits);
            }
        }

        public List<UnitGroup<?>> getGroups() {
            return groups;
        }
    }

    @FunctionalInterface
    private interface CustomGroupFactory {
        UnitGroup<?> create(List<SimpleUnit> units);
    }
}
