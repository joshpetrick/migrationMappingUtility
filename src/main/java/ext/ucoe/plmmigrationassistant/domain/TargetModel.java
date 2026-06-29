package ext.ucoe.plmmigrationassistant.domain;
import jakarta.persistence.*;
import java.time.Instant;
@Entity
public class TargetModel {
  @Id @GeneratedValue public Long id;
  @ManyToOne public MigrationProject project;
  public String name;
  @Column(length = 2000) public String description;
  public String version;
  public Instant importedAt = Instant.now();
  public String status;
}
