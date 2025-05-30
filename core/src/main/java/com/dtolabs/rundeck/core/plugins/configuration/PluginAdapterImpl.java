package com.dtolabs.rundeck.core.plugins.configuration;

import com.dtolabs.rundeck.core.common.PropertyRetriever;
import com.dtolabs.rundeck.core.config.FeatureInfoService;
import com.dtolabs.rundeck.core.plugins.Plugin;
import com.dtolabs.rundeck.plugins.config.Group;
import com.dtolabs.rundeck.plugins.config.PluginGroup;
import com.dtolabs.rundeck.plugins.descriptions.*;
import com.dtolabs.rundeck.plugins.util.DescriptionBuilder;
import com.dtolabs.rundeck.plugins.util.PropertyBuilder;
import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Plugin adapter implementation
 */
public class PluginAdapterImpl
        implements PluginAdapter
{

    @Getter @Setter FeatureInfoService featureInfoService;
    /**
     * @param object potential plugin object annotated with {@link com.dtolabs.rundeck.core.plugins.Plugin}
     * @return true if the object has a valid Plugin annotation
     */
    @Override
    public boolean canBuildDescription(final Object object) {
        final String annotation1 = getPluginNameAnnotation(object.getClass());
        return null != annotation1;
    }

    /**
     * @param object  the object
     * @param builder builder
     * @return Create a Description using a builder by analyzing the annotations on a plugin object, and including
     *         annotations on fields as DescriptionProperties.
     */
    @Override
    public Description buildDescription(final Object object, final DescriptionBuilder builder) {
        return buildDescription(object, builder, true);
    }

    /**
     * @param type    the type
     * @param builder builder
     * @return Create a Description using a builder by analyzing the annotations on a plugin type, and including
     *         annotations on fields as DescriptionProperties.
     */
    @Override
    public Description buildDescription(final Class<?> type, final DescriptionBuilder builder) {
        return buildDescription(type, builder, true);
    }

    /**
     * @param object                          the object
     * @param builder                         builder
     * @param includeAnnotatedFieldProperties if true, add DescriptionProperties to the Description based on annotations
     *                                        of fields in the class of the instance
     * @return Create a Description using a builder by analyzing the annotations on a plugin object.
     */
    @Override
    public Description buildDescription(
            final Object object,
            final DescriptionBuilder builder,
            final boolean includeAnnotatedFieldProperties
    )
    {
        buildDescription(object.getClass(), builder, includeAnnotatedFieldProperties);
        builder.collaborate(object);
        return builder.build();
    }

    /**
     * @param builder                         builder
     * @param includeAnnotatedFieldProperties if true, add DescriptionProperties to the Description based on annotations
     *                                        of fields in the class of the instance
     * @return Create a Description using a builder by analyzing the annotations on a plugin object.
     */
    @Override
    public Description buildDescription(
            final Class<?> type,
            final DescriptionBuilder builder,
            final boolean includeAnnotatedFieldProperties
    )
    {
        //analyze this class to determine properties
        final String pluginName = getPluginNameAnnotation(type);
        if (null != pluginName) {
            builder
                    .name(pluginName)
                    .title(pluginName)
                    .description("");
        }

        final PluginDescription descAnnotation = type.getAnnotation(PluginDescription.class);
        if (null != descAnnotation) {
            if (!"".equals(descAnnotation.title())) {
                builder.title(descAnnotation.title());
            }
            if (!"".equals(descAnnotation.description())) {
                builder.description(descAnnotation.description());
            }
            if (descAnnotation.isHighlighted()) {
                builder.isHighlighted(true);
                builder.order(descAnnotation.order());
            }
        }
        builder.metadata(loadPluginMetadata(type));

        final Group group = type.getAnnotation(Group.class);

        if (null != group && !PluginGroup.class.isAssignableFrom(type)) {
            Class<?> value = group.value();
            if (!(PluginGroup.class.isAssignableFrom(value))) {
                throw new IllegalStateException("Cannot use group type: " + value);
            }
            builder.pluginGroup(value.asSubclass(PluginGroup.class));
        }

        if (includeAnnotatedFieldProperties) {
            buildFieldProperties(type, builder);
        }
        return builder.build();
    }

    @Override
    public String getPluginNameAnnotation(final Class<?> aClass) {
        final Plugin annotation1 = aClass.getAnnotation(Plugin.class);
        return annotation1 != null ? annotation1.name() : null;
    }

    @Override
    public Map<String, String> loadPluginMetadata(final Class<?> clazz) {
        HashMap<String, String> meta = new HashMap<>();
        final PluginMetadata[] metadata = clazz.getAnnotationsByType(PluginMetadata.class);
        if (null != metadata) {
            if (metadata.length > 0) {
                for (PluginMetadata metadatum : metadata) {
                    meta.put(metadatum.key(), metadatum.value());
                }
            }
        }
        return meta;
    }

    /**
     * Return the list of properties by introspecting the annotated fields for {@link PluginProperty}
     *
     * @param object object
     * @return list of properties, may be empty
     */
    @Override
    public List<Property> buildFieldProperties(final Object object) {
        return buildFieldProperties(object.getClass());
    }

    /**
     * Return the list of properties by introspecting the annotated fields for {@link PluginProperty}
     *
     * @param aClass class
     * @return list of properties, may be empty
     */
    @Override
    public List<Property> buildFieldProperties(final Class<?> aClass) {
        DescriptionBuilder builder = DescriptionBuilder.builder();
        buildFieldProperties(aClass, builder);
        return builder.buildProperties();
    }

    /**
     * Add properties based on introspection of the object
     *
     * @param object  object
     * @param builder builder
     */
    @Override
    public void buildFieldProperties(final Object object, final DescriptionBuilder builder) {
        buildFieldProperties(object.getClass(), builder);
    }

    /**
     * Add properties based on introspection of a class
     *
     * @param aClass  class
     * @param builder builder
     */
    @Override
    public void buildFieldProperties(final Class<?> aClass, final DescriptionBuilder builder) {
        for (final Field field : collectClassFields(aClass)) {
            final PluginProperty annotation = field.getAnnotation(PluginProperty.class);
            if (null == annotation) {
                continue;
            }
            final Property pbuild = propertyFromField(field, annotation);
            if (null == pbuild) {
                continue;
            }
            builder.property(pbuild);
        }
    }

    @Override
    public Field fieldForPropertyName(final String name, final Object object) {
        for (final Field field : collectFields(object)) {
            final PluginProperty annotation = field.getAnnotation(PluginProperty.class);
            if (null == annotation) {
                continue;
            }
            if (!"".equals(annotation.name()) && name.equals(annotation.name())) {
                return field;
            } else if ("".equals(annotation.name()) && name.equals(field.getName())) {
                return field;
            }
        }
        return null;
    }

    @Override
    public Collection<Field> collectFields(final Object object) {
        return collectClassFields(object.getClass());
    }

    @Override
    public Collection<Field> collectClassFields(final Class<?> aClass) {
        ArrayList<Field> fields = new ArrayList<Field>();
        Class<?> clazz = aClass;
        do {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        while (clazz != Object.class);
        return fields;
    }

    @Override
    public Property.Type propertyTypeFromFieldType(final Class clazz) {
        if (clazz == Integer.class || clazz == int.class) {
            return Property.Type.Integer;
        } else if (clazz == Long.class || clazz == long.class) {
            return Property.Type.Long;
        } else if (clazz == Boolean.class || clazz == boolean.class) {
            return Property.Type.Boolean;
        } else if (clazz == String.class) {
            return Property.Type.String;
        } else if (clazz == String[].class || Set.class.isAssignableFrom(clazz) || List.class.isAssignableFrom(clazz)) {
            return Property.Type.Options;
        }
        return null;
    }

    @Override
    public Property propertyFromField(final Field field, final PluginProperty annotation) {
        final PropertyBuilder pbuild = PropertyBuilder.builder();
        //determine type
        final Property.Type type = propertyTypeFromFieldType(field.getType());
        if (null == type) {
            return null;
        }
        pbuild.type(type);
        if (type == Property.Type.Options) {
            final SelectValues selectAnnotation = field.getAnnotation(SelectValues.class);
            if (null != selectAnnotation) {
                String[] values = selectAnnotation.values();
                pbuild.values(values);

                extractSelectLabels(pbuild, values, field.getAnnotation(SelectLabels.class));
            }
        } else if (type == Property.Type.String) {
            StringRenderingConstants.DisplayType renderBehaviour = StringRenderingConstants.DisplayType.SINGLE_LINE;
            //set select/freeselect
            final SelectValues selectAnnotation = field.getAnnotation(SelectValues.class);
            if (null != selectAnnotation) {
                pbuild.type(
                        selectAnnotation.multiOption() ? Property.Type.Options :
                        (selectAnnotation.freeSelect() ? Property.Type.FreeSelect : Property.Type.Select)
                );
                String[] values = selectAnnotation.values();
                pbuild.values(values);
                pbuild.dynamicValues(selectAnnotation.dynamicValues());
                extractSelectLabels(pbuild, values, field.getAnnotation(SelectLabels.class));
            }

            if (field.getAnnotation(TextArea.class) != null) {
                renderBehaviour = StringRenderingConstants.DisplayType.MULTI_LINE;
            }

            if (field.getAnnotation(Password.class) != null) {
                renderBehaviour = StringRenderingConstants.DisplayType.PASSWORD;
            }

            pbuild.renderingOption(StringRenderingConstants.DISPLAY_TYPE_KEY, renderBehaviour);

            ReplaceDataVariablesWithBlanks
                    blankReplaceAnnotation =
                    field.getAnnotation(ReplaceDataVariablesWithBlanks.class);
            if (blankReplaceAnnotation != null) {
                pbuild.blankIfUnexpandable(blankReplaceAnnotation.value());
            }
        }
        for (RenderingOption renderingOption : field.getAnnotationsByType(RenderingOption.class)) {
            pbuild.renderingOption(renderingOption.key(), renderingOption.value());
        }

        String name = annotation.name();
        if (null == name || "".equals(name)) {
            name = field.getName();
        }
        pbuild.name(name);

        if (notBlank(annotation.title())) {
            pbuild.title(annotation.title());
        } else {
            pbuild.title(name);
        }

        pbuild.description(annotation.description());

        if (notBlank(annotation.defaultValue())) {
            pbuild.defaultValue(annotation.defaultValue());
        }
        pbuild.required(annotation.required());

        pbuild.scope(annotation.scope());

        if (notBlank(annotation.validatorClassName()) || !Object.class.equals(annotation.validatorClass())) {
            //attempt to create a validator
            Class<?> validatorClass = annotation.validatorClass();
            String validatorClassName = annotation.validatorClassName();
            if (notBlank(validatorClassName)) {
                try {
                    validatorClass = Class.forName(validatorClassName);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
            try {
                if (PropertyValidator.class.isAssignableFrom(validatorClass)) {
                    PropertyValidator validator = (PropertyValidator) validatorClass.getDeclaredConstructor()
                                                                                    .newInstance();
                    pbuild.validator(validator);
                }
            } catch (NoSuchMethodException | InvocationTargetException |
                     IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        }

        return pbuild.build();
    }

    @Override
    public void extractSelectLabels(
            final PropertyBuilder pbuild,
            final String[] values,
            final SelectLabels labelsAnnotation
    )
    {
        if (null != labelsAnnotation) {
            String[] labels = labelsAnnotation.values();
            HashMap<String, String> labelsMap = new HashMap<>();
            for (int i = 0; i < values.length && i < labels.length; i++) {
                labelsMap.put(values[i], labels[i]);
            }
            pbuild.labels(labelsMap);
        }
    }

    @Override
    public boolean notBlank(final String string) {
        return null != string && !"".equals(string);
    }

    private final List<PropertyScope> instanceScopes = Arrays.asList(
            PropertyScope.Instance,
            PropertyScope.InstanceOnly
    );


    /**
     * Set field values on a plugin object by using annotated field values to create a Description, and setting field
     * values to resolved property values. Any resolved properties that are not mapped to a field will  be included in
     * the return result.
     *
     * @param resolver property resolver
     * @param object   plugin object
     * @return Map of resolved properties that were not configured in the object's fields
     */
    @Override
    public Map<String, Object> configureProperties(
            final PropertyResolver resolver,
            final Object object
    )
    {
        //use a default scope of InstanceOnly if the Property doesn't specify it
        return configureProperties(resolver, buildDescription(object, DescriptionBuilder.builder()), object,
                                   PropertyScope.InstanceOnly
        );
    }

    /**
     * Set field values on a plugin object by using a Description, and setting field values to resolved property values.
     * Any resolved properties that are not mapped to a field will  be included in the return result.
     *
     * @param resolver     the property resolver
     * @param description  the property descriptions
     * @param object       the target object, which can implement {@link Configurable}, otherwise introspection will be
     *                     used
     * @param defaultScope a default property scope to assume for unspecified properties
     * @return Map of resolved properties that were not configured in the object's fields
     */
    @Override
    public Map<String, Object> configureProperties(
            final PropertyResolver resolver,
            final Description description,
            final Object object, PropertyScope defaultScope
    )
    {
        Map<String, Object> inputConfig = mapDescribedProperties(resolver, description, defaultScope);
        //todo: map feature flag properties
        if (object instanceof Configurable) {
            Configurable configObject = (Configurable) object;
            Properties configuration = new Properties();
            configuration.putAll(inputConfig);
            try {
                configObject.configure(configuration);
            } catch (ConfigurationException e) {

            }
        } else {
            inputConfig = configureObjectFieldsWithProperties(object, description.getProperties(), inputConfig);
        }
        return inputConfig;
    }

    /**
     * Set field values on an object using introspection and input values for those properties
     *
     * @param object      object
     * @param inputConfig input
     * @return Map of resolved properties that were not configured in the object's fields
     */
    @Override
    public Map<String, Object> configureObjectFieldsWithProperties(
            final Object object,
            final Map<String, Object> inputConfig
    )
    {
        return configureObjectFieldsWithProperties(object, buildFieldProperties(object), inputConfig);
    }

    /**
     * Set field values on an object given a list of properties and input values for those properties
     *
     * @param object      object
     * @param properties  properties
     * @param inputConfig input
     * @return Map of resolved properties that were not configured in the object's fields
     */
    @Override
    public Map<String, Object> configureObjectFieldsWithProperties(
            final Object object,
            final List<Property> properties,
            final Map<String, Object> inputConfig
    )
    {
        HashMap<String, Object> modified = new HashMap<>(inputConfig);
        for (final Property property : properties) {
            if (null != modified.get(property.getName())) {
                if (setValueForProperty(property, modified.get(property.getName()), object)) {
                    modified.remove(property.getName());
                }
            }
        }
        return modified;
    }

    private class PropertyDefaultValues
            implements PropertyRetriever
    {
        private Map<String, String> properties;

        private PropertyDefaultValues(final List<Property> properties1) {
            properties = new HashMap<String, String>();
            for (final Property property : properties1) {
                if (null != property.getDefaultValue()) {
                    properties.put(property.getName(), property.getDefaultValue());
                }

            }
        }

        public String getProperty(final String name) {
            return properties.get(name);
        }

    }

    /**
     * Retrieve the Description's Properties mapped to resolved values given the resolver, using InsanceOnly default
     * scope.
     *
     * @param resolver    property resolver
     * @param description plugin description
     * @return All mapped properties by name and value.
     */
    @Override
    public Map<String, Object> mapDescribedProperties(
            final PropertyResolver resolver,
            final Description description
    )
    {
        //use a default scope of InstanceOnly if the Property doesn't specify it
        //use property default value if otherwise not resolved
        return mapDescribedProperties(resolver, description, null);
    }

    /**
     * Retrieve the Description's Properties mapped to resolved values given the resolver, with a default property
     * scope
     *
     * @param resolver             property resolver
     * @param description          plugin description
     * @param defaultPropertyScope default scope for unspecified property scopes
     * @return All mapped properties by name and value.
     */
    @Override
    public Map<String, Object> mapDescribedProperties(
            final PropertyResolver resolver,
            final Description description, final PropertyScope defaultPropertyScope
    )
    {
        final List<Property> properties = description.getProperties();
        return mapProperties(resolver, properties, defaultPropertyScope);
    }

    /**
     * Retrieve the Properties mapped to resolved values given the resolver, with a default property scope
     *
     * @param resolver             property resolver
     * @param properties           properties
     * @param defaultPropertyScope default scope for unspecified property scopes
     * @return All mapped properties by name and value.
     */
    @Override
    public Map<String, Object> mapProperties(
            final PropertyResolver resolver,
            final List<Property> properties, final PropertyScope defaultPropertyScope
    )
    {
        PropertyResolver inputResolver = resolver;
        if (null != featureInfoService) {
            inputResolver = featureFlagScopeResolver(resolver, properties);
        }
        final PropertyResolver defaulted =
                PropertyResolverFactory.withDefaultValues(
                        PropertyResolverFactory.withDefaultScope(
                                null != defaultPropertyScope ? defaultPropertyScope : PropertyScope.InstanceOnly,
                                inputResolver
                        ),
                        new PluginAdapterImpl.PropertyDefaultValues(properties)
                );

        return PropertyResolverFactory.mapPropertyValues(properties, defaulted);
    }

    private PropertyResolver featureFlagScopeResolver(final PropertyResolver resolver, final List<Property> properties) {
        return (name, scope) -> {
            if (scope != PropertyScope.FeatureFlag) {
                return resolver.resolvePropertyValue(name, scope);
            }

            //flag name can be defined in rendering options, otherwise use property name
            String flagName = properties.stream()
                                        .filter(p -> p.getName().equals(name))
                                        .filter(p -> p.getRenderingOptions() != null
                                                     && p.getRenderingOptions()
                                                         .get(StringRenderingConstants.FEATURE_FLAG_NAME)
                                                        != null)
                                        .findFirst()
                                        .map(p -> p
                                                .getRenderingOptions()
                                                .get(StringRenderingConstants.FEATURE_FLAG_NAME)
                                                .toString())
                                        .orElse(name);
            return featureInfoService.featurePresent(flagName);
        };
    }

    /**
     * Set config on fields annotated with PluginConfig {@link PluginCustomConfig}
     *
     * @param object
     * @param config
     */
    @Override
    public void setConfig(final Object object, Object config) {
        for (final Field field : collectClassFields(object.getClass())) {
            final PluginCustomConfig annotation = field.getAnnotation(PluginCustomConfig.class);
            if (null == annotation) {
                continue;
            }

            try {
                setFieldValue(field, config, object);
            } catch (Exception e) {
                throw new RuntimeException("Cannot set value of PluginCustomConfig field: " + field.getName(), e);
            }
        }
    }

    @Override
    public PluginCustomConfig getCustomConfigAnnotation(final Object providerInstance) {
        PluginCustomConfig annotation = null;
        for (final Field field : collectClassFields(providerInstance.getClass())) {
            annotation = field.getAnnotation(PluginCustomConfig.class);
            if (null != annotation) {
                break;
            }
        }
        return annotation;
    }

    /**
     * Set instance field value for the given property, returns true if the field value was set, false otherwise
     */
    private boolean setValueForProperty(final Property property, final Object value, final Object object) {
        final Field field = fieldForPropertyName(property.getName(), object);
        if (null == field) {
            return false;
        }
        final Property.Type type = property.getType();
        final Property.Type ftype = propertyTypeFromFieldType(field.getType());
        if (ftype != property.getType()
            && !(
                ftype == Property.Type.String
                && (
                        property.getType() == Property.Type.Select ||
                        property.getType() == Property.Type.FreeSelect ||
                        property.getType() == Property.Type.Options
                )
        )) {

            throw new IllegalStateException(
                    String.format(
                            "cannot map property {%s type: %s} to field {%s type: %s}",
                            property.getName(),
                            property.getType(),
                            field.getName(),
                            ftype
                    ));
        }
        final Object resolvedValue;
        if (type == Property.Type.Integer) {
            final Integer intvalue;
            if (value instanceof String) {
                intvalue = Integer.parseInt((String) value);
            } else if (value instanceof Integer) {
                intvalue = (Integer) value;
            } else {
                //XXX
                return false;
            }
            resolvedValue = intvalue;
        } else if (type == Property.Type.Long) {
            final Long longvalue;
            if (value instanceof String) {
                longvalue = Long.parseLong((String) value);
            } else if (value instanceof Long) {
                longvalue = (Long) value;
            } else if (value instanceof Integer) {
                final int val = (Integer) value;
                longvalue = (long) val;
            } else {
                //XXX
                return false;
            }
            resolvedValue = longvalue;
        } else if (type == Property.Type.Boolean) {
            final Boolean boolvalue;
            if (value instanceof String) {
                boolvalue = Boolean.parseBoolean((String) value);
            } else if (value instanceof Boolean) {
                boolvalue = (Boolean) value;
            } else {
                //XXX
                return false;
            }
            resolvedValue = boolvalue;
        } else if (type == Property.Type.Options) {
            final List<String> splitList;
            if (value instanceof String) {
                String valstring = (String) value;
                splitList = Arrays.stream(valstring.split(", *"))
                                  .filter(s -> !s.isBlank())
                                  .collect(Collectors.toList());
            } else if (value instanceof List) {
                splitList = (List<String>) value;
            } else if (value instanceof Set) {
                splitList = new ArrayList<>((Set) value);
            } else if (value.getClass() == String[].class) {
                splitList = Arrays.asList((String[]) value);
            } else {
                return false;
            }
            Set<String> resolvedValueSet = null;
            //not a String field
            if (field.getType().isAssignableFrom(Set.class)) {
                HashSet<String> strings = new HashSet<>();
                strings.addAll(splitList);
                resolvedValueSet = strings;
                resolvedValue = strings;
            } else if (field.getType().isAssignableFrom(List.class)) {
                ArrayList<String> strings = new ArrayList<>();
                strings.addAll(splitList);
                resolvedValueSet = new HashSet<>(strings);
                resolvedValue = strings;
            } else if (field.getType() == String[].class) {
                ArrayList<String> strings = new ArrayList<>();
                strings.addAll(splitList);
                resolvedValueSet = new HashSet<>(strings);
                resolvedValue = strings.toArray(new String[strings.size()]);
            } else if (field.getType() == String.class) {
                resolvedValueSet = new HashSet<>();
                resolvedValueSet.addAll(splitList);
                resolvedValue = value;
            } else {
                return false;
            }
            if (property.getSelectValues() != null && !property.getSelectValues().containsAll(resolvedValueSet)) {
                throw new RuntimeException(String.format(
                        "Some options values were not allowed for property %s: %s",
                        property.getName(),
                        resolvedValue
                ));
            }

        } else if (type == Property.Type.String || type == Property.Type.FreeSelect) {
            if (value instanceof String) {
                resolvedValue = value;
            } else {
                //XXX
                return false;
            }
        } else if (type == Property.Type.Select) {
            if (value instanceof String) {
                resolvedValue = value;
                if (!field.getAnnotation(SelectValues.class).dynamicValues() && !property
                        .getSelectValues()
                        .contains((String) resolvedValue)) {
                    throw new RuntimeException(
                            "value not allowed for property " + property.getName() + ": " + resolvedValue);
                }
            } else {
                //XXX
                return false;
            }
        } else {
            //XXX
            return false;
        }
        try {
            setFieldValue(field, resolvedValue, object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to configure plugin: " + e.getMessage(), e);
        }
        return true;
    }

    private void setFieldValue(final Field field, final Object value, final Object object)
            throws IllegalAccessException
    {
        if (!field.canAccess(object)) {
            field.setAccessible(true);
        }
        field.set(object, value);
    }
}
