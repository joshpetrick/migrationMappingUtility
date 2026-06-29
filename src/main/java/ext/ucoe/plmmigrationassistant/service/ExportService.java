package ext.ucoe.plmmigrationassistant.service;
import ext.ucoe.plmmigrationassistant.domain.*;
import ext.ucoe.plmmigrationassistant.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import static ext.ucoe.plmmigrationassistant.domain.Enums.*;
@Service
public class ExportService {
  private final MigrationProjectRepository projects;
  private final FieldMappingRepository mappings;
  private final DocumentExportRepository exports;
  public ExportService(MigrationProjectRepository p, FieldMappingRepository m,
                       DocumentExportRepository e) {
    projects = p;
    mappings = m;
    exports = e;
  }
  @Transactional
  public DocumentExport export(Long projectId, ExportType type) {
    MigrationProject p = projects.findById(projectId).orElseThrow();
    StringBuilder b = new StringBuilder();
    b.append("# ")
        .append(label(type))
        .append("\n\nProject: ")
        .append(p.name)
        .append("\nCustomer: ")
        .append(p.customerName)
        .append("\nSource: ")
        .append(p.sourceSystem)
        .append("\nTarget: ")
        .append(p.targetSystem)
        .append("\n\n");
    if (type == ExportType.MAPPING_DEFINITIONS_JSON) {
      b.setLength(0);
      b.append("[\n");
      List<FieldMapping> all = mappings.findByMappingSetProjectId(projectId);
      for (int i = 0; i < all.size(); i++) {
        FieldMapping fm = all.get(i);
        b.append("  {\"targetObjectType\":\"")
            .append(fm.targetObjectType.displayName)
            .append("\",\"targetField\":\"")
            .append(fm.targetAttribute.attributeName)
            .append("\",\"sourceTable\":\"")
            .append(fm.sourceTable == null ? "" : fm.sourceTable.tableName)
            .append("\",\"sourceField\":\"")
            .append(fm.sourceColumn == null ? "" : fm.sourceColumn.columnName)
            .append("\",\"mappingType\":\"")
            .append(fm.mappingType)
            .append("\",\"status\":\"")
            .append(fm.mappingStatus)
            .append("\"}")
            .append(i + 1 < all.size() ? "," : "")
            .append("\n");
      }
      b.append("]\n");
    } else {
      b.append("## Mapping Matrix by Target Object Type\n\n");
      for (FieldMapping fm : mappings.findByMappingSetProjectId(projectId)) {
        b.append("- ")
            .append(fm.targetObjectType.displayName)
            .append(".")
            .append(fm.targetAttribute.attributeName)
            .append(" <= ")
            .append(fm.sourceTable == null ? "" : fm.sourceTable.tableName)
            .append(fm.sourceColumn == null ? ""
                                            : "." + fm.sourceColumn.columnName)
            .append(" | ")
            .append(fm.mappingType)
            .append(" | ")
            .append(fm.mappingStatus)
            .append("\n");
      }
      b.append("\n## Implementation Notes\n\nThis deterministic export " +
               "documents implementation requirements only. It does not " +
               "generate executable scripts, production SQL, transformation " +
               "code, or AI-generated pseudocode.\n");
    }
    DocumentExport de = new DocumentExport();
    de.project = p;
    de.exportType = type;
    de.fileName =
        type.name().toLowerCase() + "-" + projectId +
        (type == ExportType.MAPPING_DEFINITIONS_JSON ? ".json" : ".md");
    de.generatedBy = "system";
    de.status = "GENERATED";
    de.filePath = "in-database";
    de.content = b.toString();
    return exports.save(de);
  }
  private String label(ExportType t) { return t.name().replace('_', ' '); }
}
