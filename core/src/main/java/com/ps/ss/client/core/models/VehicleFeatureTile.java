package com.ps.ss.client.core.models;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.*;

import javax.annotation.PostConstruct;

import com.day.cq.wcm.api.Page;

@Model(
    adaptables = Resource.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class VehicleFeatureTile {

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String description;

    @ValueMapValue
    private String iconPath;

    @ValueMapValue
    private String ctaLabel;

    @ValueMapValue
    private String ctaUrl;

    @ValueMapValue
    private boolean ctaNewWindow;

    @ValueMapValue
    private String iconAltOverride;

    @ScriptVariable
    private Page currentPage;

    private String themeClass;

    @PostConstruct
    protected void init() {
        String brand = "default";

        if (currentPage != null) {
            String path = currentPage.getPath();

            if (path.contains("brand-a")) {
                brand = "brand-a";
            } else if (path.contains("brand-b")) {
                brand = "brand-b";
            } else if (path.contains("brand-c")) {
                brand = "brand-c";
            } else if (path.contains("brand-d")) {
                brand = "brand-d";
            }
        }

        themeClass = "feature-tile--" + brand;
    }

    public String getTitle() {
        return StringUtils.defaultString(title);
    }

    public String getDescription() {
        return StringUtils.defaultString(description);
    }

    public String getIconPath() {
        return StringUtils.defaultString(iconPath);
    }

    public String getCtaLabel() {
        return StringUtils.defaultString(ctaLabel);
    }

    public String getCtaUrl() {
        return StringUtils.defaultString(ctaUrl);
    }

    public boolean isCtaNewWindow() {
        return ctaNewWindow;
    }

    public boolean hasCta() {
        return StringUtils.isNotBlank(ctaLabel) && StringUtils.isNotBlank(ctaUrl);
    }

    public String getIconAlt() {
        return StringUtils.defaultString(iconAltOverride);
    }

    public boolean isDecorativeIcon() {
        return StringUtils.isBlank(iconAltOverride);
    }

    public String getThemeClass() {
        return themeClass;
    }
}