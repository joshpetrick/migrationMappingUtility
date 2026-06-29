package ext.ucoe.plmmigrationassistant.service;
import ext.ucoe.plmmigrationassistant.domain.*;
import ext.ucoe.plmmigrationassistant.repository.*;
import com.fasterxml.jackson.databind.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
@Service
public class TargetModelImportService {
  private final MigrationProjectRepository projects;
  private final TargetModelRepository models;
  private final TargetObjectTypeRepository types;
  private final TargetAttributeRepository attrs;
  private final ObjectMapper mapper = new ObjectMapper();
  public TargetModelImportService(MigrationProjectRepository p,
                                  TargetModelRepository m,
                                  TargetObjectTypeRepository t,
                                  TargetAttributeRepository a) {
    projects = p;
    models = m;
    types = t;
    attrs = a;
  }
  @Transactional
  public TargetModel importJson(Long projectId, MultipartFile file)
      throws IOException {
    JsonNode root = mapper.readTree(file.getInputStream());
    MigrationProject p = projects.findById(projectId).orElseThrow();
    TargetModel tm = new TargetModel();
    tm.project = p;
    tm.name = root.path("name").asText("Imported Windchill Target Model");
    tm.version = root.path("version").asText("imported");
    tm.description = root.path("description").asText("");
    tm.status = "IMPORTED";
    models.save(tm);
    int typeOrder = 1;
    for (JsonNode typeNode : root.path("targetObjectTypes")) {
      TargetObjectType t = new TargetObjectType();
      t.targetModel = tm;
      t.objectTypeName = typeNode.path("objectTypeName").asText();
      t.displayName = typeNode.path("displayName").asText(t.objectTypeName);
      t.softType = typeNode.path("softType").asText(null);
      t.stagingName = typeNode.path("stagingName").asText();
      t.description = typeNode.path("description").asText("");
      t.sortOrder = typeNode.path("sortOrder").asInt(typeOrder++);
      t.active = true;
      types.save(t);
      int attrOrder = 1;
      for (JsonNode attrNode : typeNode.path("attributes")) {
        TargetAttribute a = new TargetAttribute();
        a.targetObjectType = t;
        a.attributeName = attrNode.path("attributeName").asText();
        a.displayName = attrNode.path("displayName").asText(a.attributeName);
        a.dataType = attrNode.path("dataType").asText("string");
        a.required = attrNode.path("required").asBoolean(false);
        a.maxLength = attrNode.hasNonNull("maxLength")
                          ? attrNode.path("maxLength").asInt()
                          : null;
        a.legalValues = attrNode.path("legalValues").asText(null);
        a.defaultValue = attrNode.path("defaultValue").asText(null);
        a.exampleValue = attrNode.path("exampleValue").asText(null);
        a.description = attrNode.path("description").asText("");
        a.validationNotes = attrNode.path("validationNotes").asText("");
        a.stagingNotes = attrNode.path("stagingNotes").asText("");
        a.sortOrder = attrNode.path("sortOrder").asInt(attrOrder++);
        attrs.save(a);
      }
    }
    return tm;
  }
}
