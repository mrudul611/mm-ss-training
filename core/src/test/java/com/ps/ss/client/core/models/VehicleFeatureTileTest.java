package com.ps.ss.client.core.models;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
class VehicleFeatureTileTest {

    private final AemContext context = new AemContext();

    private Resource resource;

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(VehicleFeatureTile.class);

        context.create().page("/content/brand-a/en");

        resource = context.create().resource(
                "/content/brand-a/en/jcr:content/feature",
                "title", "AWD",
                "description", "All wheel drive",
                "iconPath", "/content/dam/icon.svg",
                "ctaLabel", "Learn More",
                "ctaUrl", "/content/page",
                "ctaNewWindow", true
        );

        context.currentResource(resource);
        context.currentPage("/content/brand-a/en");
    }

    @Test
    void testModelBasicFields() {
        VehicleFeatureTile model = context.request().adaptTo(VehicleFeatureTile.class);

        assertNotNull(model);
        assertEquals("AWD", model.getTitle());
        assertEquals("All wheel drive", model.getDescription());
        assertEquals("/content/dam/icon.svg", model.getIconPath());
    }

    @Test
    void testCTAExists() {
        VehicleFeatureTile model = context.request().adaptTo(VehicleFeatureTile.class);

        assertTrue(model.hasCta());
        assertTrue(model.isCtaNewWindow());
    }

    @Test
    void testThemeClassDerivedFromBrand() {
        VehicleFeatureTile model = context.request().adaptTo(VehicleFeatureTile.class);

        assertEquals("feature-tile--brand-a", model.getThemeClass());
    }

    @Test
    void testNoCTAWhenMissingFields() {
        Resource noCtaResource = context.create().resource(
                "/content/test/nocta",
                "title", "No CTA"
        );

        context.currentResource(noCtaResource);

        VehicleFeatureTile model = context.request().adaptTo(VehicleFeatureTile.class);

        assertFalse(model.hasCta());
    }

    @Test
    void testDecorativeIcon() {
        VehicleFeatureTile model = context.request().adaptTo(VehicleFeatureTile.class);

        assertTrue(model.isDecorativeIcon());
    }
}