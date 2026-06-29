package ext.ucoe.plmmigrationassistant.domain;
import jakarta.persistence.*;
import java.time.*;
import static ext.ucoe.plmmigrationassistant.domain.Enums.*;
@Entity
public class DocumentExport {
  @Id @GeneratedValue public Long id;
  @ManyToOne public MigrationProject project;
  @Enumerated(EnumType.STRING) public ExportType exportType;
  public String fileName;
  public Instant generatedAt = Instant.now();
  public String generatedBy;
  public String status;
  public String filePath;
  @Lob public String content;
}
