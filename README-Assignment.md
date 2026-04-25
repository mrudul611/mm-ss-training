#  Vehicle Feature Tile — AEM Component

##  Overview
This project implements a reusable Vehicle Feature Tile component for a multi-brand automotive platform in AEM as a Cloud Service.

The component displays:
- Icon
- Title
- Description
- Optional CTA (Call-To-Action)

It supports multiple brands (A, B, C, D) using a single implementation without duplication.

---

##  Reusability Approach

### 1. Multi-Brand Rendering (No Component Forking)
A single component is reused across all brands by deriving the brand from the current page (via page path or properties in the Sling Model).

**Sling Model Path:**
core/src/main/java/com/ps/ss/client/core/models/VehicleFeatureTile.java

**Component Path:**
/apps/ssvehicle/components/vehicle-feature-tile

A dynamic CSS modifier class is applied:
feature-tile--{brand}

Example:
feature-tile--brand-a  
feature-tile--brand-b  

This approach:
- Eliminates per-brand component duplication  
- Keeps logic centralized  
- Ensures consistent behavior across brands  

---

### 2. Brand-Specific CSS (ClientLib Strategy)
Brand-specific styling is handled via client libraries, not separate components.

Structure:
/apps/ssvehicle/components/vehicle-feature-tile/clientlibs/

  ├── feature-tile (shared styles)  
  ├── brand-a (overrides)  
  ├── brand-b (overrides)  
  ├── brand-c (overrides)  
  ├── brand-d (overrides)  

Base styles live in feature-tile. Brand overrides apply using CSS modifiers:
.feature-tile--brand-a { background: #000; color: #fff; }

This ensures:
- Clean separation of styling and logic  
- Easy extensibility for new brands  
- No duplication of components  

---

### 3. Core Module vs. ui.apps
- core module: Sling Model, business logic, validation  
- ui.apps: HTL, dialogs, clientlibs, component structure  
- brand clientlibs: Visual theming only  

This separation:
- Keeps business logic reusable  
- Allows independent styling changes  
- Aligns with AEM best practices  

---

##  Sample Authored Content

sling:resourceType: ssvehicle/content/vehicle-feature-tile  
title: "All-Wheel Drive"  
description: "Dynamic torque control across all four wheels."  
iconPath: "/content/dam/ssvehicle/brand-a/icons/awd.svg"  
ctaLabel: "Learn More"  
ctaUrl: "/content/dam/ssvehicle/brand-a/en/technology/awd.html"  
ctaNewWindow: false  
iconAltOverride: ""  

Notes:
- Empty iconAltOverride means icon is decorative  
- CTA renders only if both label and URL are present  

---

## Accessibility Considerations
- Semantic wrapper: <article> (represents standalone content)  
- Decorative icons: alt="" and aria-hidden="true"  
- Informational icons: author-provided alt text via dialog  
- External links: rel="noopener" when opening in new tab  

---

## Client-Side Enhancement (TypeScript)
- Uses IntersectionObserver  
- Emits custom event: tile:viewed  
- Triggered when 50% of tile is visible  
- Gracefully degrades if unsupported  

---

##  Testing
Unit tests implemented using wcm.io AEM Mocks:
- Field mapping validation  
- CTA rendering logic  
- Brand derivation  
- Edge cases (missing values)  
- core/src/test/java/com/ps/ss/client/core/models/VehicleFeatureTileTest.java
---



### Headless Variant

To support omnichannel delivery (SPA, mobile apps, APIs), the Vehicle Feature Tile can be modeled as a **Content Fragment Model (CFM)** and exposed via AEM GraphQL.

#### Content Fragment Model: Vehicle Feature

**Model Path:**
/conf/ssvehicle/settings/dam/cfm/models/vehicle-feature

**Fields:**
- title (Single line text)
- description (Multi-line text)
- icon (Content Reference → DAM asset)
- ctaLabel (Single line text)
- ctaUrl (Single line text)
- ctaNewWindow (Boolean)
- iconAltOverride (Single line text, optional)

#### Variants

Content Fragments support multiple variants for different channels or use cases:

- **master** → Default full content (used for web rendering)
- **compact** → Shortened description for mobile/cards
- **accessibility** → Includes explicit alt text for non-decorative icons

This allows the same content to be reused across:
- AEM Sites (HTL component)
- Headless APIs (GraphQL)
- Mobile / SPA applications

#### Example GraphQL Query

Fetch all features for a given vehicle model:

```graphql

query GetVehicleFeatures($modelPath: String!) {
  vehicleFeatureList(
    filter: {
      _path: {
        _expressions: [
          { value: $modelPath, _operator: STARTS_WITH }
        ]
      }
    }
  ) {
    items {
      title
      description
      icon {
        _path
      }
      ctaLabel
      ctaUrl
      ctaNewWindow
    }
  }
}
```
### Example Response:

```
{
  "data": {
    "vehicleFeatureList": {
      "items": [
        {
          "title": "All-Wheel Drive",
          "description": "Dynamic torque control across all wheels.",
          "icon": {
            "_path": "/content/dam/brand-a/icons/awd.svg"
          },
          "ctaLabel": "Learn More",
          "ctaUrl": "/content/brand-a/en/technology/awd.html",
          "ctaNewWindow": false
        }
      ]
    }
  }
}

```

### Dispatcher Caching
- Moderate TTL (5–15 minutes)  
- Cache invalidation triggered on content activation  
- Ensures updated content is reflected quickly  

---

### Cloud Manager Quality Gates
Potential failures:
1. Low test coverage → addressed with Sling Model unit tests  
2. SonarQube issues → handled with null-safe logic and clean structure  

---

##  Summary
This implementation focuses on:
- Reusability across multiple brands  
- Clean separation of concerns  
- Accessibility-first development  
- Scalable theming via clientlibs  
