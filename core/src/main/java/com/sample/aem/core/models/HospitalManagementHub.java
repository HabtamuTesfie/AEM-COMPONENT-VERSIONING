package com.sample.aem.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.*;


//--------------------------------------------------------------------------------
/**
 * A Sling Model representing the Hospital Management Hub component.
 * This model is used to retrieve information about the Hospital Management Hub.
 */
//--------------------------------------------------------------------------------
@Model
    (
      adaptables = { SlingHttpServletRequest.class, Resource.class },
      defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
   )
public class HospitalManagementHub
{

    @Self
    private SlingHttpServletRequest request;

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String versionNumber;

    public String getTitle() {
        return title;
    }

    public String getVersion() {
        return versionNumber;
    }
}
