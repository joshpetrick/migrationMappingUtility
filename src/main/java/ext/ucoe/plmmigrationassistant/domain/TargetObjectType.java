package ext.ucoe.plmmigrationassistant.domain;
import jakarta.persistence.*;
@Entity
public class TargetObjectType {
  @Id @GeneratedValue public Long id;
  @ManyToOne public TargetModel targetModel;
  public String objectTypeName;
  public String displayName;
  public String softType;
  public String stagingName;
  @Column(length = 2000) public String description;
  public int sortOrder;
  public boolean active = true;
}
