package ext.ucoe.plmmigrationassistant.domain;
import jakarta.persistence.*;
import java.time.Instant;
@Entity
public class SourceImport {
  @Id @GeneratedValue public Long id;
  @ManyToOne public MigrationProject project;
  public String importName;
  public String sourceSystem;
  public String fileName;
  public String importType;
  public String status;
  public Instant importedAt = Instant.now();
}
