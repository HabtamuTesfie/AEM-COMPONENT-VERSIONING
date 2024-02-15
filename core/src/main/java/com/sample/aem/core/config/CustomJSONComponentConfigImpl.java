package com.sample.aem.core.config;

import java.lang.annotation.Annotation;

 public class CustomJSONComponentConfigImpl implements CustomJSONComponentConfig {
        private final String jsonUrl;

        public CustomJSONComponentConfigImpl(java.util.Dictionary<String, Object> properties) {
            this.jsonUrl = (String) properties.get("jsonUrl");
        }

        @Override
        public String jsonUrl() {
            return jsonUrl;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return null;
        }
    }
