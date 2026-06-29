package ext.ucoe.plmmigrationassistant.service;
import ext.ucoe.plmmigrationassistant.domain.*;
import ext.ucoe.plmmigrationassistant.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import static ext.ucoe.plmmigrationassistant.domain.Enums.*;
@Service
public class DemoDataService {
  private final MigrationProjectRepository projects;
  private final SourceImportRepository imports;
  private final SourceTableRepository tables;
  private final SourceColumnProfileRepository cols;
  private final TargetModelRepository models;
  private final TargetObjectTypeRepository types;
  private final TargetAttributeRepository attrs;
  private final MappingSetRepository sets;
  private final FieldMappingRepository mappings;
  private final LookupTableRepository lookups;
  private final LookupValueRepository values;
  private final GapDecisionRepository gaps;
  public DemoDataService(MigrationProjectRepository p, SourceImportRepository i,
                         SourceTableRepository t,
                         SourceColumnProfileRepository c,
                         TargetModelRepository m, TargetObjectTypeRepository ty,
                         TargetAttributeRepository a, MappingSetRepository s,
                         FieldMappingRepository fm, LookupTableRepository l,
                         LookupValueRepository v, GapDecisionRepository g) {
    projects = p;
    imports = i;
    tables = t;
    cols = c;
    models = m;
    types = ty;
    attrs = a;
    sets = s;
    mappings = fm;
    lookups = l;
    values = v;
    gaps = g;
  }
  @Transactional
  public MigrationProject seed() {
    MigrationProject p = new MigrationProject();
    p.name = "Agile to Windchill Demo";
    p.customerName = "Example Customer";
    p.sourceSystem = "Oracle Agile PLM";
    p.targetSystem = "PTC Windchill";
    p.migrationScope = "Parts, Documents, BOMs, Attachments";
    p.architectOwner = "migration consultant";
    p.description = "Demo project for deterministic source-to-Windchill " +
                    "mapping specifications.";
    projects.save(p);
    SourceImport si = new SourceImport();
    si.project = p;
    si.importName = "Sample Agile CSV exports";
    si.sourceSystem = p.sourceSystem;
    si.fileName = "agile_item.csv";
    si.importType = "CSV";
    si.status = "IMPORTED";
    imports.save(si);
    SourceTable item = table(si, "ITEM", 3, 9), bom = table(si, "BOM", 2, 5),
                att = table(si, "ATTACHMENTS", 2, 5);
    col(item, "ITEM_NUMBER", 0, 3, 40, "P-1001|P-1002|P-1003");
    col(item, "DESCRIPTION", 0, 3, 80, "Widget|Bracket|Housing");
    col(item, "LIFECYCLE_PHASE", 0, 3, 12, "Prototype|Released|Obsolete");
    col(item, "REV", 0, 3, 2, "A|B|C");
    col(item, "CLASS", 0, 2, 20, "Part|Document");
    col(item, "SUBCLASS", 0, 3, 25, "Engineering Part|Service Part|Reference");
    col(item, "UOM", 0, 3, 5, "each|kg|m");
    col(item, "MAKE_BUY", 0, 2, 4, "Make|Buy");
    col(item, "MATERIAL", 1, 2, 30, "Steel|Plastic");
    col(bom, "PARENT_ITEM", 0, 2, 40, "P-1001|P-1002");
    col(bom, "CHILD_ITEM", 0, 2, 40, "P-2001|P-2002");
    col(bom, "FIND_NUMBER", 0, 2, 4, "10|20");
    col(bom, "QUANTITY", 0, 2, 4, "1|2");
    col(bom, "UOM", 0, 1, 5, "each");
    col(att, "ITEM_NUMBER", 0, 2, 40, "P-1001|P-1002");
    col(att, "FILE_NAME", 0, 2, 120, "spec.pdf|image.png");
    col(att, "FILE_PATH", 0, 2, 255, "/vault/spec.pdf|/vault/image.png");
    col(att, "FILE_CATEGORY", 0, 2, 30, "Specification|Image");
    col(att, "FILE_SIZE", 0, 2, 10, "1024|2048");
    TargetModel tm = new TargetModel();
    tm.project = p;
    tm.name = "Windchill MVP Target Model";
    tm.version = "demo";
    tm.status = "IMPORTED";
    models.save(tm);
    TargetObjectType wt = type(tm, "WTPart", 1),
                     doc = type(tm, "WTDocument", 2),
                     links = type(tm, "BOM Links", 3),
                     content = type(tm, "Attachments / Content", 4);
    TargetAttribute number =
        attr(wt, "number", "Number", "string", true, 40, null, 1);
    TargetAttribute name =
        attr(wt, "name", "Name", "string", true, 120, null, 2);
    TargetAttribute unit = attr(wt, "defaultUnit", "Default Unit", "enum", true,
                                10, "each, kg, m, l", 3);
    TargetAttribute life = attr(wt, "lifecycleState", "Lifecycle State", "enum",
                                true, 20, "INWORK, RELEASED, OBSOLETE", 4);
    attr(wt, "container", "Container", "string", true, 120, null, 5);
    attr(wt, "folder", "Folder", "string", false, 255, null, 6);
    attr(wt, "revision", "Revision", "string", false, 10, null, 7);
    attr(wt, "source", "Source", "enum", false, 10, "MAKE, BUY", 8);
    attr(wt, "partType", "Part Type", "string", false, 80, null, 9);
    attr(wt, "material", "Material", "string", false, 80, null, 10);
    attr(wt, "makeBuy", "Make/Buy", "enum", false, 10, "MAKE, BUY", 11);
    attr(doc, "number", "Document Number", "string", true, 40, null, 1);
    attr(doc, "name", "Document Name", "string", true, 120, null, 2);
    attr(links, "parentNumber", "Parent Part", "string", true, 40, null, 1);
    attr(links, "childNumber", "Child Part", "string", true, 40, null, 2);
    attr(content, "fileName", "File Name", "string", true, 120, null, 1);
    MappingSet ms = new MappingSet();
    ms.project = p;
    ms.name = "MVP Mapping Set";
    ms.version = "1";
    ms.status = "DRAFT";
    sets.save(ms);
    map(ms, wt, number, item, "ITEM_NUMBER", MappingType.DIRECT,
        MappingStatus.APPROVED);
    map(ms, wt, name, item, "DESCRIPTION", MappingType.DIRECT,
        MappingStatus.PROPOSED);
    FieldMapping lmap =
        map(ms, wt, life, item, "LIFECYCLE_PHASE", MappingType.LOOKUP,
            MappingStatus.NEEDS_CUSTOMER_REVIEW);
    LookupTable lt = new LookupTable();
    lt.project = p;
    lt.name = "lifecycle_crosswalk";
    lt.sourceSystem = p.sourceSystem;
    lt.targetSystem = p.targetSystem;
    lt.relatedTargetObjectType = wt;
    lt.relatedTargetAttribute = life;
    lt.status = "IN_REVIEW";
    lt.owner = p.architectOwner;
    lookups.save(lt);
    lmap.lookupTable = lt;
    mappings.save(lmap);
    lookup(lt, "Prototype", "INWORK", false, true);
    lookup(lt, "Released", "RELEASED", true, false);
    lookup(lt, "Obsolete", "OBSOLETE", true, false);
    GapDecision gd = new GapDecision();
    gd.project = p;
    gd.title = "Confirm Prototype lifecycle mapping";
    gd.description = "Customer must approve Agile lifecycle phase Prototype " +
                     "mapping to Windchill INWORK.";
    gd.severity = Severity.WARNING;
    gd.targetObjectType = wt;
    gd.targetAttribute = life;
    gd.owner = p.architectOwner;
    gd.status = GapStatus.CUSTOMER_ACTION_REQUIRED;
    gaps.save(gd);
    return p;
  }
  private SourceTable table(SourceImport si, String n, long r, int cc) {
    SourceTable t = new SourceTable();
    t.sourceImport = si;
    t.tableName = n;
    t.displayName = n;
    t.rowCount = r;
    t.columnCount = cc;
    return tables.save(t);
  }
  private SourceColumnProfile col(SourceTable t, String n, long nulls,
                                  long distinct, int max, String samples) {
    SourceColumnProfile c = new SourceColumnProfile();
    c.sourceTable = t;
    c.columnName = n;
    c.dataTypeGuess = "string";
    c.nullCount = nulls;
    c.nullPercentage = t.rowCount == 0 ? 0 : (100.0 * nulls / t.rowCount);
    c.distinctCount = distinct;
    c.maxLength = max;
    c.sampleValues = samples;
    c.duplicateCount = Math.max(0, t.rowCount - distinct);
    c.likelyEnum = distinct <= 10;
    c.duplicateIndicator = c.duplicateCount > 0;
    return cols.save(c);
  }
  private TargetObjectType type(TargetModel m, String n, int o) {
    TargetObjectType t = new TargetObjectType();
    t.targetModel = m;
    t.objectTypeName = n;
    t.displayName = n;
    t.stagingName = "STG_" + n.toUpperCase().replaceAll("[^A-Z0-9]", "_");
    t.sortOrder = o;
    return types.save(t);
  }
  private TargetAttribute attr(TargetObjectType t, String n, String d,
                               String type, boolean req, Integer max,
                               String legal, int o) {
    TargetAttribute a = new TargetAttribute();
    a.targetObjectType = t;
    a.attributeName = n;
    a.displayName = d;
    a.dataType = type;
    a.required = req;
    a.maxLength = max;
    a.legalValues = legal;
    a.sortOrder = o;
    return attrs.save(a);
  }
  private FieldMapping map(MappingSet ms, TargetObjectType tot,
                           TargetAttribute a, SourceTable st, String sc,
                           MappingType mt, MappingStatus status) {
    FieldMapping fm = new FieldMapping();
    fm.mappingSet = ms;
    fm.targetObjectType = tot;
    fm.targetAttribute = a;
    fm.sourceTable = st;
    fm.sourceColumn =
        cols.findFirstBySourceTableTableNameAndColumnName(st.tableName, sc)
            .orElse(null);
    fm.mappingType = mt;
    fm.mappingStatus = status;
    fm.approvalStatus =
        status == MappingStatus.APPROVED ? "APPROVED" : "PENDING";
    fm.owner = "migration consultant";
    return mappings.save(fm);
  }
  private void lookup(LookupTable lt, String s, String t, boolean approved,
                      boolean unresolved) {
    LookupValue v = new LookupValue();
    v.lookupTable = lt;
    v.sourceValue = s;
    v.targetValue = t;
    v.approved = approved;
    v.unresolved = unresolved;
    v.status = approved ? "APPROVED" : "NEEDS_CUSTOMER_REVIEW";
    values.save(v);
  }
}
