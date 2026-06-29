package ext.ucoe.plmmigrationassistant.domain;
import jakarta.persistence.*;
@Entity
public class TransformationRequirement {
  @Id @GeneratedValue public Long id;
  @ManyToOne public FieldMapping fieldMapping;
  public String name;
  @Column(length = 4000) public String requirement;
  public String status;
  public String owner;
}
