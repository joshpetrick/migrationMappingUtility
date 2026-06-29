package ext.ucoe.plmmigrationassistant.service;
import ext.ucoe.plmmigrationassistant.domain.*;
import ext.ucoe.plmmigrationassistant.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import static ext.ucoe.plmmigrationassistant.domain.Enums.*;
@Service
public class AuditService {
  private final TargetObjectTypeRepository types;
  private final TargetAttributeRepository attrs;
  private final FieldMappingRepository mappings;
  private final AuditFindingRepository findings;
  private final MigrationProjectRepository projects;
  public AuditService(TargetObjectTypeRepository t, TargetAttributeRepository a,
                      FieldMappingRepository m, AuditFindingRepository f,
                      MigrationProjectRepository p) {
    types = t;
    attrs = a;
    mappings = m;
    findings = f;
    projects = p;
  }
  @Transactional
  public List<AuditFinding> run(Long projectId) {
    MigrationProject p = projects.findById(projectId).orElseThrow();
    List<AuditFinding> out = new ArrayList<>();
    for (TargetObjectType type :
         types.findByTargetModelProjectIdAndActiveTrueOrderBySortOrderAsc(
             projectId)) {
      for (TargetAttribute attr :
           attrs.findByTargetObjectTypeIdOrderBySortOrderAsc(type.id)) {
        FieldMapping fm =
            mappings.findByTargetAttributeId(attr.id).orElse(null);
        if (attr.required &&
            (fm == null ||
             blank(fm.defaultValue) && fm.sourceColumn == null &&
                 fm.mappingStatus != MappingStatus.NOT_APPLICABLE)) {
          out.add(add(p, type, attr, fm, Severity.ERROR, "REQUIRED_UNMAPPED",
                      "Required target field has no mapping, default, or " +
                      "approved not-applicable justification."));
        }
        if (fm != null) {
          if (fm.mappingType == MappingType.LOOKUP && fm.lookupTable == null)
            out.add(
                add(p, type, attr, fm, Severity.ERROR, "LOOKUP_MISSING",
                    "Mapping type is LOOKUP but no lookup table is assigned."));
          if (fm.mappingType == MappingType.DEFAULT_VALUE &&
              blank(fm.defaultValue))
            out.add(add(p, type, attr, fm, Severity.ERROR, "DEFAULT_MISSING",
                        "Mapping type is DEFAULT_VALUE but no default value " +
                        "is assigned."));
          if (fm.mappingType == MappingType.DIRECT && fm.sourceColumn == null)
            out.add(
                add(p, type, attr, fm, Severity.ERROR, "DIRECT_SOURCE_MISSING",
                    "Mapping type is DIRECT but no source field is assigned."));
          if (fm.mappingStatus != MappingStatus.APPROVED)
            out.add(add(p, type, attr, fm, Severity.WARNING, "NOT_APPROVED",
                        "Mapping exists but approval status is not APPROVED."));
          if (attr.required && blank(fm.validationNotes))
            out.add(add(p, type, attr, fm, Severity.WARNING,
                        "VALIDATION_NOTE_MISSING",
                        "Required target field has no validation note."));
          if (fm.customerDecisionRequired)
            out.add(add(p, type, attr, fm, Severity.WARNING,
                        "CUSTOMER_DECISION_OPEN",
                        "Customer decision is open."));
          if (attr.maxLength != null && fm.sourceColumn != null &&
              fm.sourceColumn.maxLength != null &&
              fm.sourceColumn.maxLength > attr.maxLength)
            out.add(add(p, type, attr, fm, Severity.WARNING, "MAX_LENGTH",
                        "Source max length exceeds target max length."));
        }
      }
    }
    return out;
  }
  private AuditFinding add(MigrationProject p, TargetObjectType t,
                           TargetAttribute a, FieldMapping fm, Severity s,
                           String cat, String msg) {
    AuditFinding af = new AuditFinding();
    af.project = p;
    af.targetObjectType = t;
    af.targetAttribute = a;
    af.fieldMapping = fm;
    af.severity = s;
    af.category = cat;
    af.message = msg;
    return findings.save(af);
  }
  private boolean blank(String s) { return s == null || s.isBlank(); }
}
