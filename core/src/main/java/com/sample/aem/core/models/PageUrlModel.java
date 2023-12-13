package com.sample.aem.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;

@Model(adaptables = SlingHttpServletRequest.class)
public class PageUrlModel {

    @SlingObject
    private SlingHttpServletRequest request;

    public String getCurrentPageUrl() {
        String url = request.getRequestURL().toString();

        if (!"true".equals(request.getParameter("wcmmode"))) {
            url += "?wcmmode=disabled";
        }

        return url;
    }
}

