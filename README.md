# PLM Migration Assistant

A standalone Java/Spring Boot web application for PLM migration consultants and architects documenting deterministic source-to-PTC Windchill mapping specifications.

This is **not** an AI tool, middleware runtime, automatic migration engine, automatic source-to-target mapping tool, or production transformation script generator. The architect remains responsible for all mapping and transformation decisions. All mappings, transformation requirements, lookup values, assumptions, exclusions, validation notes, and customer decisions must be manually entered, imported, or explicitly approved by the architect.

## MVP Capabilities

- Migration project creation and dashboard.
- Agile-to-Windchill demo seed button.
- Configurable Windchill target object tabs loaded from `TargetObjectType` records.
- Target-first mapping workspace centered on Windchill target fields.
- Contextual Help Center with question-mark links beside major UI components and a demo how-to walkthrough.
- Three-panel UI:
  - Source Schema Browser with source tables, fields, sample values, null percentage, distinct count, max length, duplicate/list indicators.
  - Windchill Staging Schema / Mapping Matrix with required field visibility and manual mapping forms.
  - Mapping Detail / Audit Panel with deterministic status legend and saved mapping detail.
- Manual mapping types and mapping statuses matching the specification.
- Lookup/crosswalk entities and demo lifecycle crosswalk values.
- Gap/customer decision log entities and demo open customer decision.
- Deterministic audit service for required unmapped fields, missing lookup/default/source assignments, unapproved mappings, missing validation notes, open customer decisions, and max-length warnings.
- Documentation/export records for Mapping Specification, Customer Review Workbook, Implementation Requirements Export, and JSON mapping definitions.
- REST endpoints for projects, source imports/profiles, target object types/attributes, mappings, lookups, gaps, audits, and exports.
- H2 for local development/testing and Docker Compose for local PostgreSQL.

## Technology Stack

- Java 17+
- Spring Boot
- Maven
- Spring Data JPA / Hibernate
- PostgreSQL for local production-like development
- H2 for default local development/testing
- Thymeleaf + Bootstrap for the initial UI
- REST APIs for core functionality
- Docker Compose for local PostgreSQL
- Clean layered architecture: `controller`, `service`, `repository`, `domain`, `dto`/helper-ready packages

## Running Locally

Default H2 mode:

```bash
mvn spring-boot:run
```

PostgreSQL mode:

```bash
docker compose up -d postgres
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/plm_workbench \
SPRING_DATASOURCE_USERNAME=plm \
SPRING_DATASOURCE_PASSWORD=plm \
mvn spring-boot:run
```

Then open <http://localhost:8080>, create a project, or click **Create Agile-to-Windchill Demo Project**.

## Architecture and Implementation Plan

See [`docs/architecture.md`](docs/architecture.md) for the architecture summary, proposed entity model, MVP workflow, UI layout, documentation/export structure, and implementation plan that preceded implementation.

## Deterministic Product Positioning

The center of the application is the Windchill target schema. The key question answered by the UI is:

> For every Windchill target field, especially required fields, where does the value come from, what rule applies, who approved it, and what is still unresolved?

The application uses deterministic rule-based validation only. It does not add AI dependencies, call LLMs, infer mappings, recommend mappings, generate production SQL, generate executable transformation code, connect directly to Windchill, connect directly to Oracle Agile, or assume proprietary Windchill database tables.
