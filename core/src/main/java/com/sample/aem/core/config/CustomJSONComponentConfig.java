package com.sample.aem.core.config;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(name = "Custom JSON Component Configuration", description = "Configuration for Custom JSON Component")
public @interface CustomJSONComponentConfig {

    @AttributeDefinition(name = "JSON URL", description = "URL for the JSON data")
    String jsonUrl() default "";

}
