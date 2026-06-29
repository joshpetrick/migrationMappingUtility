package ext.ucoe.plmmigrationassistant.domain;
import jakarta.persistence.*;
import java.time.*;
import static ext.ucoe.plmmigrationassistant.domain.Enums.*;
@Entity
public class AuditFinding {
  @Id @GeneratedValue public Long id;
  @ManyToOne public MigrationProject project;
  @ManyToOne public TargetObjectType targetObjectType;
  @ManyToOne public TargetAttribute targetAttribute;
  @ManyToOne public FieldMapping fieldMapping;
  @Enumerated(EnumType.STRING) public Severity severity;
  public String category;
  @Column(length = 4000) public String message;
  @Enumerated(EnumType.STRING) public AuditStatus status = AuditStatus.OPEN;
  public Instant createdAt = Instant.now();
  public Instant resolvedAt;
}
