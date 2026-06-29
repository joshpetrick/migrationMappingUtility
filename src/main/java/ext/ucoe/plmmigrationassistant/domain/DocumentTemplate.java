package ext.ucoe.plmmigrationassistant.domain;
import jakarta.persistence.*;
@Entity
public class DocumentTemplate {
  @Id @GeneratedValue public Long id;
  public String name;
  public String exportType;
  @Lob public String templateBody;
  public boolean active = true;
}
