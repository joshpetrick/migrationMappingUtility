package ext.ucoe.plmmigrationassistant.service;

import static ext.ucoe.plmmigrationassistant.domain.Enums.*;

import ext.ucoe.plmmigrationassistant.domain.*;
import ext.ucoe.plmmigrationassistant.repository.*;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExportService {
  private final MigrationProjectRepository projects;
  private final FieldMappingRepository mappings;
  private final DocumentExportRepository exports;
  private final AuditFindingRepository findings;
  private final GapDecisionRepository gaps;

  public ExportService(MigrationProjectRepository projects,
                       FieldMappingRepository mappings,
                       DocumentExportRepository exports,
                       AuditFindingRepository findings,
                       GapDecisionRepository gaps) {
    this.projects = projects;
    this.mappings = mappings;
    this.exports = exports;
    this.findings = findings;
    this.gaps = gaps;
  }

  @Transactional
  public DocumentExport export(Long projectId, ExportType type) {
    MigrationProject project = projects.findById(projectId).orElseThrow();
    String content = type == ExportType.MAPPING_DEFINITIONS_JSON
                         ? mappingDefinitionsJson(projectId)
                         : projectDocumentation(projectId, project);

    DocumentExport documentExport = new DocumentExport();
    documentExport.project = project;
    documentExport.exportType = type;
    documentExport.fileName =
        "project-documentation-" + projectId +
        (type == ExportType.MAPPING_DEFINITIONS_JSON ? ".json" : ".md");
    documentExport.generatedBy = "system";
    documentExport.status = "GENERATED";
    documentExport.filePath = "in-database";
    documentExport.content = content;
    return exports.save(documentExport);
  }

  private String projectDocumentation(Long projectId,
                                      MigrationProject project) {
    StringBuilder builder = new StringBuilder();
    builder.append("# PLM Migration Assistant Project Documentation\n\n")
        .append("Project: ")
        .append(project.name)
        .append("\nCustomer: ")
        .append(project.customerName)
        .append("\nSource: ")
        .append(project.sourceSystem)
        .append("\nTarget: ")
        .append(project.targetSystem)
        .append("\n\n");

    builder.append("## Project Overview\n\n")
        .append("- Scope: ")
        .append(project.migrationScope == null ? "" : project.migrationScope)
        .append("\n- Architect: ")
        .append(project.architectOwner == null ? "" : project.architectOwner)
        .append("\n\n");

    builder.append("## Mapping Matrix by Target Object Type\n\n");
    for (FieldMapping mapping : mappings.findByMappingSetProjectId(projectId)) {
      builder.append("- ")
          .append(mapping.targetObjectType.displayName)
          .append(".")
          .append(mapping.targetAttribute.attributeName)
          .append(" <= ")
          .append(mapping.sourceTable == null ? ""
                                              : mapping.sourceTable.tableName)
          .append(mapping.sourceColumn == null
                      ? ""
                      : "." + mapping.sourceColumn.columnName)
          .append(" | ")
          .append(mapping.mappingType)
          .append(" | ")
          .append(mapping.mappingStatus)
          .append(" | ")
          .append(mapping.transformationRequirement == null
                      ? ""
                      : mapping.transformationRequirement)
          .append("\n");
    }

    builder.append("\n## Open Audit Items\n\n");
    for (AuditFinding finding : findings.findAllByProjectId(projectId)) {
      builder.append("- ")
          .append(finding.severity)
          .append(" | ")
          .append(finding.category)
          .append(" | ")
          .append(finding.message)
          .append("\n");
    }

    builder.append("\n## Gap and Decision Log\n\n");
    for (GapDecision gap : gaps.findAllByProjectId(projectId)) {
      builder.append("- ")
          .append(gap.severity)
          .append(" | ")
          .append(gap.status)
          .append(" | ")
          .append(gap.title)
          .append(" | Owner: ")
          .append(gap.owner == null ? "" : gap.owner)
          .append("\n");
    }

    builder.append("\n## Customer Review and Signoff\n\n")
        .append("Review open audit items, gap decisions, lookup values, and " +
                "mappings marked for customer review before approval.\n");

    builder.append("\n## Implementation Requirements\n\n")
        .append(
            "This deterministic export documents implementation requirements " +
            "only. It does not generate executable scripts, production SQL, " +
            "transformation code, or AI-generated pseudocode.\n");

    return builder.toString();
  }

  private String mappingDefinitionsJson(Long projectId) {
    StringBuilder builder = new StringBuilder("[\n");
    List<FieldMapping> all = mappings.findByMappingSetProjectId(projectId);
    for (int i = 0; i < all.size(); i++) {
      FieldMapping mapping = all.get(i);
      builder.append("  {\"targetObjectType\":\"")
          .append(mapping.targetObjectType.displayName)
          .append("\",\"targetField\":\"")
          .append(mapping.targetAttribute.attributeName)
          .append("\",\"sourceTable\":\"")
          .append(mapping.sourceTable == null ? ""
                                              : mapping.sourceTable.tableName)
          .append("\",\"sourceField\":\"")
          .append(mapping.sourceColumn == null
                      ? ""
                      : mapping.sourceColumn.columnName)
          .append("\",\"mappingType\":\"")
          .append(mapping.mappingType)
          .append("\",\"status\":\"")
          .append(mapping.mappingStatus)
          .append("\"}")
          .append(i + 1 < all.size() ? "," : "")
          .append("\n");
    }
    return builder.append("]\n").toString();
  }
}
