package ext.ucoe.plmmigrationassistant.controller;
import static ext.ucoe.plmmigrationassistant.domain.Enums.*;

import ext.ucoe.plmmigrationassistant.domain.*;
import ext.ucoe.plmmigrationassistant.repository.*;
import ext.ucoe.plmmigrationassistant.service.*;
import java.util.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@RestController
@RequestMapping("/api")
public class ApiController {
  private final CsvImportService csvImport;
  private final TargetModelImportService targetImport;
  private final MigrationProjectRepository projects;
  private final SourceImportRepository imports;
  private final SourceTableRepository tables;
  private final SourceColumnProfileRepository cols;
  private final TargetObjectTypeRepository types;
  private final TargetAttributeRepository attrs;
  private final FieldMappingRepository mappings;
  private final LookupTableRepository lookups;
  private final LookupValueRepository lookupValues;
  private final GapDecisionRepository gaps;
  private final AuditService audit;
  private final AuditFindingRepository findings;
  private final ExportService exports;
  public ApiController(CsvImportService csv,
                       TargetModelImportService targetImport,
                       MigrationProjectRepository p, SourceImportRepository i,
                       SourceTableRepository t, SourceColumnProfileRepository c,
                       TargetObjectTypeRepository ty,
                       TargetAttributeRepository a, FieldMappingRepository m,
                       LookupTableRepository l, LookupValueRepository lv,
                       GapDecisionRepository g, AuditService au,
                       AuditFindingRepository f, ExportService e) {
    csvImport = csv;
    this.targetImport = targetImport;
    projects = p;
    imports = i;
    tables = t;
    cols = c;
    types = ty;
    attrs = a;
    mappings = m;
    lookups = l;
    lookupValues = lv;
    gaps = g;
    audit = au;
    findings = f;
    exports = e;
  }
  @GetMapping("/projects")
  List<MigrationProject> projects() {
    return projects.findAll();
  }
  @PostMapping("/projects")
  MigrationProject create(@RequestBody MigrationProject p) {
    return projects.save(p);
  }
  @GetMapping("/projects/{id}")
  MigrationProject project(@PathVariable Long id) {
    return projects.findById(id).orElseThrow();
  }
  @PutMapping("/projects/{id}")
  MigrationProject update(@PathVariable Long id,
                          @RequestBody MigrationProject body) {
    body.id = id;
    return projects.save(body);
  }
  @PostMapping("/projects/{projectId}/source-imports/csv")
  SourceImport uploadCsv(@PathVariable Long projectId,
                         @RequestParam("file") MultipartFile file)
      throws java.io.IOException {
    return csvImport.importCsv(projectId, file);
  }
  @GetMapping("/projects/{projectId}/source-imports")
  List<SourceImport> sourceImports(@PathVariable Long projectId) {
    return imports.findByProjectId(projectId);
  }
  @GetMapping("/source-imports/{importId}/tables")
  List<SourceTable> importTables(@PathVariable Long importId) {
    return tables.findBySourceImportId(importId);
  }
  @GetMapping("/source-tables/{tableId}/columns")
  List<SourceColumnProfile> tableColumns(@PathVariable Long tableId) {
    return cols.findBySourceTableId(tableId);
  }
  @GetMapping("/source-columns/{columnId}/profile")
  SourceColumnProfile profile(@PathVariable Long columnId) {
    return cols.findById(columnId).orElseThrow();
  }
  @PostMapping("/projects/{projectId}/target-model/import-json")
  TargetModel importTargetModel(@PathVariable Long projectId,
                                @RequestParam("file") MultipartFile file)
      throws java.io.IOException {
    return targetImport.importJson(projectId, file);
  }
  @GetMapping("/projects/{projectId}/target-model")
  java.util.Map<String, Object> targetModel(@PathVariable Long projectId) {
    return java.util.Map.of(
        "targetObjectTypes",
        types.findByTargetModelProjectIdAndActiveTrueOrderBySortOrderAsc(
            projectId));
  }
  @GetMapping("/projects/{projectId}/target-object-types")
  List<TargetObjectType> targetTypes(@PathVariable Long projectId) {
    return types.findByTargetModelProjectIdAndActiveTrueOrderBySortOrderAsc(
        projectId);
  }
  @GetMapping("/target-object-types/{objectTypeId}/attributes")
  List<TargetAttribute> attributes(@PathVariable Long objectTypeId) {
    return attrs.findByTargetObjectTypeIdOrderBySortOrderAsc(objectTypeId);
  }
  @GetMapping("/projects/{projectId}/mappings")
  List<FieldMapping> projectMappings(@PathVariable Long projectId) {
    return mappings.findByMappingSetProjectId(projectId);
  }
  @PutMapping("/mappings/{mappingId}")
  FieldMapping putMapping(@PathVariable Long mappingId,
                          @RequestBody FieldMapping fm) {
    fm.id = mappingId;
    return mappings.save(fm);
  }
  @DeleteMapping("/mappings/{mappingId}")
  void deleteMapping(@PathVariable Long mappingId) {
    mappings.deleteById(mappingId);
  }
  @GetMapping("/target-object-types/{objectTypeId}/mapping-matrix")
  List<FieldMapping> matrix(@PathVariable Long objectTypeId) {
    return mappings.findByTargetObjectTypeId(objectTypeId);
  }
  @GetMapping("/projects/{projectId}/lookup-tables")
  List<LookupTable> lookupTables(@PathVariable Long projectId) {
    return lookups.findAllByProjectId(projectId);
  }
  @PostMapping("/projects/{projectId}/lookup-tables")
  LookupTable createLookup(@PathVariable Long projectId,
                           @RequestBody LookupTable lt) {
    lt.project = projects.findById(projectId).orElseThrow();
    return lookups.save(lt);
  }
  @GetMapping("/lookup-tables/{lookupTableId}/values")
  List<LookupValue> lookupVals(@PathVariable Long lookupTableId) {
    return lookupValues.findAll()
        .stream()
        .filter(v
                -> v.lookupTable != null &&
                       Objects.equals(v.lookupTable.id, lookupTableId))
        .toList();
  }
  @PostMapping("/lookup-tables/{lookupTableId}/values")
  LookupValue createLookupValue(@PathVariable Long lookupTableId,
                                @RequestBody LookupValue v) {
    v.lookupTable = lookups.findById(lookupTableId).orElseThrow();
    return lookupValues.save(v);
  }
  @PutMapping("/lookup-values/{lookupValueId}")
  LookupValue putLookupValue(@PathVariable Long lookupValueId,
                             @RequestBody LookupValue v) {
    v.id = lookupValueId;
    return lookupValues.save(v);
  }
  @GetMapping("/projects/{projectId}/gap-decisions")
  List<GapDecision> gapDecisions(@PathVariable Long projectId) {
    return gaps.findAllByProjectId(projectId);
  }
  @PostMapping("/projects/{projectId}/gap-decisions")
  GapDecision createGap(@PathVariable Long projectId,
                        @RequestBody GapDecision g) {
    g.project = projects.findById(projectId).orElseThrow();
    return gaps.save(g);
  }
  @PutMapping("/gap-decisions/{gapDecisionId}")
  GapDecision putGap(@PathVariable Long gapDecisionId,
                     @RequestBody GapDecision g) {
    g.id = gapDecisionId;
    return gaps.save(g);
  }
  @PostMapping("/projects/{projectId}/audit/run")
  List<AuditFinding> run(@PathVariable Long projectId) {
    return audit.run(projectId);
  }
  @GetMapping("/projects/{projectId}/audit-findings")
  List<AuditFinding> auditFindings(@PathVariable Long projectId) {
    return findings.findAllByProjectId(projectId);
  }
  @GetMapping("/target-object-types/{objectTypeId}/audit-summary")
  Map<String, Object> auditSummary(@PathVariable Long objectTypeId) {
    return Map.of(
        "objectTypeId", objectTypeId, "openFindings",
        findings.findAll()
            .stream()
            .filter(f
                    -> f.targetObjectType != null &&
                           Objects.equals(f.targetObjectType.id, objectTypeId))
            .count());
  }
  @PostMapping("/projects/{projectId}/exports/{type}")
  DocumentExport export(@PathVariable Long projectId,
                        @PathVariable ExportType type) {
    return exports.export(projectId, type);
  }
}
