# PLM Migration Assistant Architecture

## 1. Architecture summary

The PLM Migration Assistant is a standalone Spring Boot application for PLM migration architects documenting third-party PLM to PTC Windchill mapping specifications. It is target-first: mapping workspaces are organized by configurable Windchill `TargetObjectType` records, not hardcoded tabs and not source-first screens.

The application is intentionally deterministic. It does not use AI, machine learning, LLMs, automatic inference, auto-mapping, or smart recommendations. Architects manually enter, import, or explicitly approve mappings, transformations, lookup/crosswalk values, assumptions, decisions, and notes. Rule-based audits only validate configured facts and manually entered records.

### Layers

- `controller`: Thymeleaf page controllers and REST API controllers.
- `service`: deterministic business workflows, profiling, audit, exports, and demo seed creation.
- `repository`: Spring Data JPA repositories.
- `domain`: JPA entities and enums.
- `dto`: REST request/response records.
- `mapper/helper`: small conversion helpers where needed.

## 2. Proposed database/entity model

Core entities:

- `MigrationProject`: customer, source system, Windchill target, scope, architect, and lifecycle status.
- `SourceImport`, `SourceTable`, `SourceColumnProfile`, `SourceSampleValue`: imported files/tables and deterministic source profiling facts.
- `TargetModel`, `TargetObjectType`, `TargetAttribute`: configurable Windchill target/staging schema.
- `MappingSet`, `FieldMapping`, `TransformationRequirement`: architect-managed mapping matrix and transformation requirements.
- `LookupTable`, `LookupValue`: crosswalks such as Agile lifecycle phase to Windchill lifecycle state.
- `GapDecision`: unresolved customer decisions, gaps, exclusions, and accepted risks.
- `ValidationRuleSpec`: validation requirements that must be documented for implementation/testing.
- `AuditFinding`: deterministic audit results.
- `DocumentExport`, `DocumentTemplate`: generated documentation/export records and templates.
- `ChangeHistory`: auditable history notes for user-visible changes.

## 3. MVP workflow

1. Create a migration project.
2. Upload Agile CSV exports or use the demo seed.
3. Profile source files/tables using deterministic statistics.
4. Import a Windchill target model JSON or use demo metadata.
5. Open the Mapping Workspace.
6. Select a configurable Windchill target object tab.
7. Review required Windchill fields and red unmapped errors.
8. Select source fields, defaults, lookup tables, and mapping types manually.
9. Enter transformation requirements, validation notes, assumptions, implementation notes, and decisions.
10. Run deterministic audits.
11. Track gaps/customer decisions.
12. Export mapping specification, customer review workbook, validation plan, implementation requirements, and JSON mapping definitions.

## 4. UI layout description

The initial UI uses Thymeleaf and Bootstrap:

- Project List: create and open migration projects.
- Project Dashboard: per-project metrics, demo seed action, imports, target model, exports, gaps, and workspace links.
- Source Imports/Profile: uploaded CSV files, discovered tables, column profiles, sample values, null percentage, distinct count, max length, duplicates, and deterministic list-field indicators.
- Windchill Target Model: configured `TargetObjectType` records and attributes.
- Mapping Workspace: target object tabs generated from the database. For each selected target object type:
  - Left panel: searchable source schema browser.
  - Center panel: Windchill target field mapping matrix with status indicators and editable mapping fields.
  - Right panel: selected row mapping detail and audit context.
- Lookup/Crosswalk Tables.
- Gap and Decision Log.
- Validation Requirements.
- Documentation Exports.

## 5. Documentation/export structure

Exports are generated from structured data and templates only. No AI narrative, executable transformation code, production SQL, or script generation is produced.

Supported export concepts:

- Mapping Specification Document.
- Transformation Requirements Document.
- Customer Review Workbook.
- Gap and Decision Log.
- Validation Plan.
- Mapping Matrix CSV.
- Lookup/Crosswalk Workbook CSV.
- JSON export of mapping definitions.
- Implementation Requirements Export.

The Implementation Requirements Export documents target object type, target field, source table/field, source expression, mapping type, lookup requirement, transformation requirement, validation requirement, implementation notes, unresolved decisions, owner, and status.

## 6. Implementation plan

1. Scaffold Spring Boot 3 / Java 17+ / Maven with JPA, Thymeleaf, validation, H2, PostgreSQL driver, and Bootstrap webjar.
2. Create domain entities, enums, and repositories.
3. Add deterministic services for CSV profiling, JSON target model import, mapping updates, audit execution, exports, and demo seed data.
4. Add REST APIs for projects, source imports, target model, mappings, lookups, gaps, audit, and exports.
5. Add Thymeleaf pages for the MVP screens and the target-first three-panel workspace.
6. Add sample Agile CSV, crosswalk CSV, and Windchill target model JSON demo files.
7. Add Docker Compose for local PostgreSQL and H2 defaults for local/test use.
8. Add smoke tests for application context and deterministic audit behavior.
