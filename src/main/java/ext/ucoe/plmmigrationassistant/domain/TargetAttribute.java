package ext.ucoe.plmmigrationassistant.domain;
import jakarta.persistence.*;
@Entity
public class TargetAttribute {
  @Id @GeneratedValue public Long id;
  @ManyToOne public TargetObjectType targetObjectType;
  public String attributeName;
  public String displayName;
  public String dataType;
  public boolean required;
  public Integer maxLength;
  @Column(length = 2000) public String legalValues;
  public String defaultValue;
  public String exampleValue;
  @Column(length = 2000) public String description;
  @Column(length = 2000) public String validationNotes;
  @Column(length = 2000) public String stagingNotes;
  public int sortOrder;
}
