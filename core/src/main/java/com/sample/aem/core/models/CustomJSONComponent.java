package com.sample.aem.core.models;

import com.sample.aem.core.api.JsonData;
import com.sample.aem.core.config.CustomJSONComponentConfigImpl;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sample.aem.core.config.CustomJSONComponentConfig;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Model(adaptables = SlingHttpServletRequest.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class CustomJSONComponent {

    private static final Logger LOG = LoggerFactory.getLogger(CustomJSONComponent.class);
    private static final String CONFIG_PID = "com.sample.aem.core.config.CustomJSONComponentConfig";

    @OSGiService
    private ConfigurationAdmin configAdmin;

    @OSGiService
    private ObjectMapper objectMapper;

    @Reference
    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public String getUrl() {
        try {
            Configuration configuration = configAdmin.getConfiguration(CONFIG_PID);
            if (configuration != null) {
                CustomJSONComponentConfig config = new CustomJSONComponentConfigImpl(configuration.getProperties());
                String jsonUrl = config.jsonUrl();

                if (jsonUrl != null) return jsonUrl;
            }
        }  catch (Exception e) {
            LOG.error("An error occurred while getting the JSON URL configuration.", e);
            throw new RuntimeException("Failed to retrieve JSON URL.", e);
        }
        return null;
    }

    public JsonData getResponse() {
        HttpURLConnection connection = null;

        try {
            String jsonUrl = getUrl();
            URL url = new URL(jsonUrl);

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK) {
                try(InputStream inputStream = connection.getInputStream()) {
                    return objectMapper.readValue(inputStream, JsonData.class);
                }
            } else {
                LOG.error("HTTP response code was not 200 OK");
            }

        } catch (IOException e) {
            LOG.error("I/O exception occurred when connecting", e);

        } catch (UncheckedIOException e) {
            LOG.error("I/O exception occurred when reading response", e);

        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }

        return null;
    }
}
