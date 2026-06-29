package ext.ucoe.plmmigrationassistant.controller;
import static ext.ucoe.plmmigrationassistant.domain.Enums.*;

import ext.ucoe.plmmigrationassistant.domain.*;
import ext.ucoe.plmmigrationassistant.repository.*;
import ext.ucoe.plmmigrationassistant.service.*;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@Controller
public class WebController {
  private final MigrationProjectRepository projects;
  private final DemoDataService demo;
  private final TargetObjectTypeRepository types;
  private final TargetAttributeRepository attrs;
  private final SourceTableRepository tables;
  private final SourceColumnProfileRepository cols;
  private final MappingSetRepository sets;
  private final FieldMappingRepository mappings;
  private final AuditService audit;
  private final AuditFindingRepository findings;
  private final ExportService export;
  private final DocumentExportRepository exports;
  private final LookupTableRepository lookups;
  private final GapDecisionRepository gaps;
  private final CsvImportService csvImport;
  private final TargetModelImportService targetImport;

  public WebController(MigrationProjectRepository p, DemoDataService d,
                       TargetObjectTypeRepository t,
                       TargetAttributeRepository a, SourceTableRepository st,
                       SourceColumnProfileRepository c, MappingSetRepository s,
                       FieldMappingRepository m, AuditService au,
                       AuditFindingRepository f, ExportService e,
                       DocumentExportRepository ex, LookupTableRepository l,
                       GapDecisionRepository g, CsvImportService csv,
                       TargetModelImportService target) {
    projects = p;
    demo = d;
    types = t;
    attrs = a;
    tables = st;
    cols = c;
    sets = s;
    mappings = m;
    audit = au;
    findings = f;
    export = e;
    exports = ex;
    lookups = l;
    gaps = g;
    csvImport = csv;
    targetImport = target;
  }
  @GetMapping("/")
  String home(Model model) {
    model.addAttribute("projects", projects.findAll());
    return "projects";
  }
  @PostMapping("/projects")
  String create(@RequestParam String name, @RequestParam String customerName) {
    MigrationProject p = new MigrationProject();
    p.name = name;
    p.customerName = customerName;
    p.sourceSystem = "Oracle Agile PLM";
    p.targetSystem = "PTC Windchill";
    p.status = ProjectStatus.DRAFT;
    projects.save(p);
    return "redirect:/projects/" + p.id;
  }
  @PostMapping("/demo-seed")
  String seed() {
    return "redirect:/projects/" + demo.seed().id;
  }
  @GetMapping("/projects/{id}")
  String dashboard(@PathVariable Long id, Model model) {
    List<AuditFinding> projectFindings = findings.findAllByProjectId(id);
    List<GapDecision> projectGaps = gaps.findAllByProjectId(id);
    boolean hasErrors = projectFindings.stream().anyMatch(
        finding -> finding.severity == Severity.ERROR);
    String healthStatus = hasErrors ? "Blocked"
                          : projectGaps.isEmpty() && projectFindings.isEmpty()
                              ? "On Track"
                              : "Needs Review";

    model.addAttribute("project", projects.findById(id).orElseThrow());
    model.addAttribute(
        "types",
        types.findByTargetModelProjectIdAndActiveTrueOrderBySortOrderAsc(id));
    model.addAttribute("sources", tables.findBySourceImportProjectId(id));
    model.addAttribute("findings", projectFindings);
    model.addAttribute("exports", exports.findAllByProjectId(id));
    model.addAttribute("gaps", projectGaps);
    model.addAttribute("healthStatus", healthStatus);
    model.addAttribute("hasErrors", hasErrors);
    return "dashboard";
  }
  @GetMapping("/projects/{id}/workspace")
  String workspace(@PathVariable Long id,
                   @RequestParam(required = false) Long objectTypeId,
                   Model model) {
    var ots =
        types.findByTargetModelProjectIdAndActiveTrueOrderBySortOrderAsc(id);
    TargetObjectType selected = objectTypeId == null
                                    ? (ots.isEmpty() ? null : ots.get(0))
                                    : types.findById(objectTypeId).orElse(null);
    model.addAttribute("project", projects.findById(id).orElseThrow());
    model.addAttribute("types", ots);
    model.addAttribute("selected", selected);
    model.addAttribute(
        "attributes",
        selected == null
            ? java.util.List.of()
            : attrs.findByTargetObjectTypeIdOrderBySortOrderAsc(selected.id));
    var tabMappings =
        mappings.findByTargetObjectTypeId(selected == null ? -1 : selected.id);
    Map<Long, FieldMapping> mappingByAttribute =
        tabMappings.stream().collect(Collectors.toMap(
            mapping
            -> mapping.targetAttribute.id,
            Function.identity(), (existing, replacement) -> replacement));

    model.addAttribute("sourceTables", tables.findBySourceImportProjectId(id));
    model.addAttribute("allColumns", cols.findAll());
    model.addAttribute("mappings", tabMappings);
    model.addAttribute("mappingByAttribute", mappingByAttribute);
    model.addAttribute("lookupTables", lookups.findAllByProjectId(id));
    model.addAttribute("mappingTypes", MappingType.values());
    model.addAttribute("mappingStatuses", MappingStatus.values());
    return "workspace";
  }
  @PostMapping("/projects/{projectId}/mappings")
  String
  saveMapping(@PathVariable Long projectId,
              @RequestParam Long targetAttributeId,
              @RequestParam(required = false) Long sourceColumnId,
              @RequestParam MappingType mappingType,
              @RequestParam MappingStatus mappingStatus,
              @RequestParam(required = false) String defaultValue,
              @RequestParam(required = false) String transformationRequirement,
              @RequestParam(required = false) String validationNotes,
              @RequestParam(required = false) String implementationNotes,
              @RequestParam(required = false) String assumptions,
              @RequestParam(required = false) String owner) {
    TargetAttribute a = attrs.findById(targetAttributeId).orElseThrow();
    MappingSet set =
        sets.findFirstByProjectIdOrderByCreatedAtDesc(projectId).orElseGet(
            () -> {
              MappingSet s = new MappingSet();
              s.project = projects.findById(projectId).orElseThrow();
              s.name = "Default Mapping Set";
              s.version = "1";
              s.status = "DRAFT";
              return sets.save(s);
            });
    FieldMapping fm = mappings.findByTargetAttributeId(targetAttributeId)
                          .orElseGet(FieldMapping::new);
    fm.mappingSet = set;
    fm.targetAttribute = a;
    fm.targetObjectType = a.targetObjectType;
    if (sourceColumnId != null) {
      SourceColumnProfile sc = cols.findById(sourceColumnId).orElse(null);
      fm.sourceColumn = sc;
      fm.sourceTable = sc == null ? null : sc.sourceTable;
    } else {
      fm.sourceColumn = null;
      fm.sourceTable = null;
    }
    fm.mappingType = mappingType;
    fm.mappingStatus = mappingStatus;
    fm.defaultValue = defaultValue;
    fm.transformationRequirement = transformationRequirement;
    fm.validationNotes = validationNotes;
    fm.implementationNotes = implementationNotes;
    fm.assumptions = assumptions;
    fm.owner = owner;
    fm.updatedAt = Instant.now();
    fm.approvalStatus =
        mappingStatus == MappingStatus.APPROVED ? "APPROVED" : "PENDING";
    mappings.save(fm);
    return "redirect:/projects/" + projectId +
        "/workspace?objectTypeId=" + a.targetObjectType.id;
  }
  @PostMapping("/projects/{id}/source-imports/csv")
  String uploadSourceCsv(@PathVariable Long id,
                         @RequestParam("file") MultipartFile file)
      throws java.io.IOException {
    csvImport.importCsv(id, file);
    return "redirect:/projects/" + id;
  }

  @PostMapping("/projects/{id}/target-model/import-json")
  String uploadTargetModel(@PathVariable Long id,
                           @RequestParam("file") MultipartFile file)
      throws java.io.IOException {
    targetImport.importJson(id, file);
    return "redirect:/projects/" + id;
  }

  @PostMapping("/projects/{id}/audit")
  String runAudit(@PathVariable Long id) {
    audit.run(id);
    return "redirect:/projects/" + id;
  }
  @PostMapping("/projects/{id}/exports/{type}")
  String doExport(@PathVariable Long id, @PathVariable ExportType type) {
    export.export(id, type);
    return "redirect:/projects/" + id;
  }

  @GetMapping("/projects/{projectId}/exports/{exportId}/download")
  ResponseEntity<String> downloadExport(@PathVariable Long projectId,
                                        @PathVariable Long exportId) {
    DocumentExport documentExport = exports.findById(exportId).orElseThrow();
    if (documentExport.project == null ||
        !documentExport.project.id.equals(projectId)) {
      throw new IllegalArgumentException(
          "Export does not belong to this project.");
    }

    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + documentExport.fileName + "\"")
        .contentType(MediaType.TEXT_PLAIN)
        .body(documentExport.content == null ? "" : documentExport.content);
  }

  @GetMapping("/help")
  String help() {
    return "help";
  }
}
