package ext.ucoe.plmmigrationassistant.domain;

import jakarta.persistence.*;
import java.time.Instant;
import static ext.ucoe.plmmigrationassistant.domain.Enums.*;
@Entity
public class MigrationProject {
  @Id @GeneratedValue public Long id;
  public String name;
  public String customerName;
  public String sourceSystem;
  public String targetSystem;
  @Column(length = 2000) public String description;
  public String migrationScope;
  public String architectOwner;
  @Enumerated(EnumType.STRING)
  public ProjectStatus status = ProjectStatus.DRAFT;
  public Instant createdAt = Instant.now();
  public Instant updatedAt = Instant.now();
}
