package ext.ucoe.plmmigrationassistant.domain;
import jakarta.persistence.*;
@Entity
public class LookupTable {
  @Id @GeneratedValue public Long id;
  @ManyToOne public MigrationProject project;
  public String name;
  @Column(length = 2000) public String description;
  public String sourceSystem;
  public String targetSystem;
  @ManyToOne public TargetObjectType relatedTargetObjectType;
  @ManyToOne public TargetAttribute relatedTargetAttribute;
  public String status;
  public String owner;
  @Column(length = 2000) public String notes;
}
