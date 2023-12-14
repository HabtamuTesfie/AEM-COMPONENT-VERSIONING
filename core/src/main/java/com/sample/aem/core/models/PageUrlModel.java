package com.sample.aem.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;


//--------------------------------------------------------------------------------
/**
 * A Sling Model representing a utility for obtaining the URL of the current page.
 */
//--------------------------------------------------------------------------------
@Model(adaptables = SlingHttpServletRequest.class)
public class PageUrlModel
{

    @SlingObject
    private SlingHttpServletRequest request;


    //--------------------------------------------------------------------------------
    /**
     * Retrieves the URL of the current page.
     *
     * @return The URL of the current page, including the query parameter to disable
     * WCM mode if not already present.
     */
    //--------------------------------------------------------------------------------
    public String getCurrentPageUrl()
    {
        String url = request.getRequestURL().toString();

        if (!"true".equals(request.getParameter("wcmmode")))
        {
            url += "?wcmmode=disabled";
        }

        return url;
    } // getCurrentPageUrl

} // PageUrlModel

