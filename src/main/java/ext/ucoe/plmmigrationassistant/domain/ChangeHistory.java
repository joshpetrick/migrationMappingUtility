package ext.ucoe.plmmigrationassistant.domain;
import jakarta.persistence.*;
import java.time.*;
@Entity
public class ChangeHistory {
  @Id @GeneratedValue public Long id;
  public String entityType;
  public Long entityId;
  public String changedBy;
  @Column(length = 4000) public String changeSummary;
  public Instant changedAt = Instant.now();
}
