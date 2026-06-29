package ext.ucoe.plmmigrationassistant.domain;
import jakarta.persistence.*;
import java.time.Instant;
@Entity
public class MappingSet {
  @Id @GeneratedValue public Long id;
  @ManyToOne public MigrationProject project;
  public String name;
  public String version;
  public String status;
  public Instant createdAt = Instant.now();
  public Instant updatedAt = Instant.now();
}
