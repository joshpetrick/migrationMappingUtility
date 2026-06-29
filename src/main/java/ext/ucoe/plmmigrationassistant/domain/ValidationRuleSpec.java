package ext.ucoe.plmmigrationassistant.domain;
import jakarta.persistence.*;
import static ext.ucoe.plmmigrationassistant.domain.Enums.*;
@Entity
public class ValidationRuleSpec {
  @Id @GeneratedValue public Long id;
  @ManyToOne public MigrationProject project;
  @ManyToOne public TargetObjectType targetObjectType;
  @ManyToOne public TargetAttribute targetAttribute;
  public String ruleName;
  @Enumerated(EnumType.STRING) public ValidationCategory category;
  @Enumerated(EnumType.STRING) public Severity severity = Severity.WARNING;
  @Column(length = 4000) public String description;
  @Column(length = 4000) public String expectedBehavior;
  @Column(length = 4000) public String implementationNotes;
  public String status;
}
