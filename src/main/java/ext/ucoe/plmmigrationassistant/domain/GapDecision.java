package ext.ucoe.plmmigrationassistant.domain;
import jakarta.persistence.*;
import java.time.*;
import static ext.ucoe.plmmigrationassistant.domain.Enums.*;
@Entity
public class GapDecision {
  @Id @GeneratedValue public Long id;
  @ManyToOne public MigrationProject project;
  public String title;
  @Column(length = 4000) public String description;
  @Enumerated(EnumType.STRING) public Severity severity = Severity.WARNING;
  @ManyToOne public SourceTable sourceTable;
  @ManyToOne public SourceColumnProfile sourceColumn;
  @ManyToOne public TargetObjectType targetObjectType;
  @ManyToOne public TargetAttribute targetAttribute;
  @Column(length = 2000) public String recommendation;
  @Column(length = 2000) public String customerResponse;
  public String owner;
  @Enumerated(EnumType.STRING) public GapStatus status = GapStatus.OPEN;
  public LocalDate dueDate;
  public LocalDate resolvedDate;
  @Column(length = 2000) public String notes;
}
