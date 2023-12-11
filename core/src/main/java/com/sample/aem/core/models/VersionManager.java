package com.sample.aem.core.models;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.*;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.inject.Inject;
import javax.jcr.*;


@Model
    (
        adaptables   = {SlingHttpServletRequest.class, Resource.class}
    )
@SuppressWarnings("squid:S00115")
public class VersionManager
{
    private static final String RESOURCE_PATH = "root/responsivegrid/hospital-hub";
    private static final String SLING_RESOURCE_TYPE = "sling:resourceType";
    private static final String VERSION_NUMBER = "versionNumber";
    private static final String VERSION_1_VALUE = "v1";
    private static final String VERSION_2_VALUE = "v2";
    private static final String V1_RESOURCE_TYPE = "hospital-management-hub/components/content/old-hospital-management";
    private static final String V2_RESOURCE_TYPE = "hospital-management-hub/components/content/new-hospital-management";

    @Inject
    private Resource resource;

    @Inject
    private ResourceResolver resolver;

    @Self
    private SlingHttpServletRequest m_request;


    public String getVersionNumber() {
        Node node = getPage().getContentResource(RESOURCE_PATH).adaptTo(Node.class);
        if (node == null) {
            return null;
        }
        return readText(node, VERSION_NUMBER);
    }

    public boolean isVersion2() {
        return getVersionNumber().equals(VERSION_2_VALUE);
    }


    public void changeResourceType() {
        try {
            ModifiableValueMap properties = resource.adaptTo(ModifiableValueMap.class);

            if (properties != null) {
                String currentResourceType = properties.get(SLING_RESOURCE_TYPE, String.class);

                if (VERSION_1_VALUE.equals(getVersionNumber()) && V2_RESOURCE_TYPE.equals(currentResourceType)) {
                    properties.put(SLING_RESOURCE_TYPE, V1_RESOURCE_TYPE);
                    resolver.commit();
                } else if (VERSION_2_VALUE.equals(getVersionNumber()) && V1_RESOURCE_TYPE.equals(currentResourceType)) {
                    properties.put(SLING_RESOURCE_TYPE, V2_RESOURCE_TYPE);
                    resolver.commit();
                }
            }
        } catch (PersistenceException e) {
            throw new RuntimeException(SLING_RESOURCE_TYPE + " error: Failed to persist", e);
        }
    }


    private Page getPage() {
        PageManager pm = resolver.adaptTo(PageManager.class);
        return pm.getContainingPage(getResource());
    }


    private Resource getResource() {
        return m_request.getResource();
    }

    private String readText(Node node, String name) {
        Property property = readNodeProperty(node, name);
        if (property == null) return null;

        return readText(property);
    }


    private String readText(Property property) {
        try
        {
            return property.getString();
        }
        catch (ValueFormatException e)
        {
            String error = " error: Failed to format";
            throw new RuntimeException(error, e);
        }
        catch (RepositoryException e)
        {
            String error = " error: Failed to access";
            throw new RuntimeException(error, e);
        }
    }


    private Property readNodeProperty(Node node, String name) {
        if (node == null) return null;

        try
        {
            if (!node.hasProperty(name)) return null;

            return node.getProperty(name);
        }
        catch (ValueFormatException e)
        {
            String error = "Failed to format property " + '.' + name;
            throw new RuntimeException(error, e);
        }
        catch (PathNotFoundException e)
        {
            String error = " error: No path for ";
            throw new RuntimeException(error + node, e);
        }
        catch (RepositoryException e)
        {
            String error = " error: Failed to access ";
            throw new RuntimeException(error + node, e);
        }
    }

}



