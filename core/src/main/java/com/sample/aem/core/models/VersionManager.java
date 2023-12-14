package com.sample.aem.core.models;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.*;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.inject.Inject;
import javax.jcr.*;
import java.util.Objects;


//--------------------------------------------------------------------------------
/**
 * A Sling Model representing a Version Manager for handling version-specific
 * logic in AEM components.
 */
//--------------------------------------------------------------------------------
@Model
    (
        adaptables   = {SlingHttpServletRequest.class, Resource.class}
    )
@SuppressWarnings("squid:S00115")
public class VersionManager
{
    private static final String COMPONENT_NAME      = "hospital-management-hub";
    private static final String RESOURCE_PATH       = "root/responsivegrid/hospital-hub";
    private static final String COMPONENT_PATH      = "aem-component-versioning/components/content/" + COMPONENT_NAME +"/";
    private static final String SLING_RESOURCE_TYPE = "sling:resourceType";
    private static final String VERSION_NUMBER      = "versionNumber";
    private static final String VERSION_1_VALUE     = "v1";
    private static final String VERSION_2_VALUE     = "v2";
    private static final String V1_RESOURCE_TYPE    = COMPONENT_PATH + VERSION_1_VALUE + "/" + COMPONENT_NAME;
    private static final String V2_RESOURCE_TYPE    = COMPONENT_PATH + VERSION_2_VALUE + "/" + COMPONENT_NAME;

    @Inject
    private Resource resource;

    @Inject
    private ResourceResolver resolver;

    @Self
    private SlingHttpServletRequest request;


    //--------------------------------------------------------------------------------
    /**
     * Gets the version number from the content node.
     *
     * @return The version number or null if not found.
     */
    //--------------------------------------------------------------------------------
    public String getVersionNumber()
    {
        Node node = Objects
                   .requireNonNull(getPage()
                   .getContentResource(RESOURCE_PATH))
                   .adaptTo(Node.class);
        if (node == null) {
            return null;
        }

        return readText(node);
    } // getVersionNumber


    //--------------------------------------------------------------------------------
    /**
     * Checks if the version number is v2.
     *
     * @return True if the version number is v2, false otherwise.
     */
    //--------------------------------------------------------------------------------
    public boolean isVersion2()
    {
        return getVersionNumber().equals(VERSION_2_VALUE);
    } // isVersion2


    //--------------------------------------------------------------------------------
    /**
     * Changes the resource type based on the version number.
     *
     * @return An empty string.
     */
    //--------------------------------------------------------------------------------
    public String changeResourceType()
    {
        try
        {
            ModifiableValueMap properties = resource.adaptTo(ModifiableValueMap.class);

            if (properties != null)
            {
                String currentResourceType = properties.get(SLING_RESOURCE_TYPE, String.class);

                if (VERSION_1_VALUE.equals(getVersionNumber()) && V2_RESOURCE_TYPE.equals(currentResourceType))
                {
                    properties.put(SLING_RESOURCE_TYPE, V1_RESOURCE_TYPE);
                    resolver.commit();
                }
                else if (VERSION_2_VALUE.equals(getVersionNumber()) && V1_RESOURCE_TYPE.equals(currentResourceType))
                {
                    properties.put(SLING_RESOURCE_TYPE, V2_RESOURCE_TYPE);
                    resolver.commit();
                }
            }
        }
        catch (PersistenceException e)
        {
            throw new RuntimeException(SLING_RESOURCE_TYPE + " error: Failed to persist", e);
        }

        return "";
    } // changeResourceType


    //--------------------------------------------------------------------------------
    /**
     * Gets the Page object associated with the current resource.
     *
     * @return The Page object.
     */
    //--------------------------------------------------------------------------------
    private Page getPage()
    {
        PageManager pm = resolver.adaptTo(PageManager.class);

        return Objects.requireNonNull(pm).getContainingPage(getResource());
    } // getPage


    //--------------------------------------------------------------------------------
    /**
     * Gets the current Sling Resource.
     *
     * @return The current Sling Resource.
     */
    //--------------------------------------------------------------------------------
    private Resource getResource()
    {
        return request.getResource();
    } // getResource


    //--------------------------------------------------------------------------------
    /**
     * Reads text from a JCR property.
     *
     * @param node The JCR Node.
     * @return The text value of the property, or null if not found.
     */
    //--------------------------------------------------------------------------------
    private String readText(Node node)
    {
        Property property = readNodeProperty(node);
        if (property == null) return null;

        return readText(property);
    } // readText


    //--------------------------------------------------------------------------------
    /**
     * Reads text from a JCR property.
     *
     * @param property The JCR Property.
     * @return The text value of the property.
     */
    //--------------------------------------------------------------------------------
    private String readText(Property property)
    {
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
    } // readText


    //--------------------------------------------------------------------------------
    /**
     * Reads a JCR property from a node.
     *
     * @param node The JCR Node.
     * @return The JCR Property or null if not found.
     */
    //--------------------------------------------------------------------------------
    private Property readNodeProperty(Node node)
    {
        if (node == null) return null;

        try
        {
            if (!node.hasProperty(VersionManager.VERSION_NUMBER)) return null;

            return node.getProperty(VersionManager.VERSION_NUMBER);
        }
        catch (ValueFormatException e)
        {
            String error = "Failed to format property " + '.' + VersionManager.VERSION_NUMBER;
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
    } // readNodeProperty

} // VersionManager



