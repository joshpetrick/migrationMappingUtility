package ext.ucoe.plmmigrationassistant.domain;
import jakarta.persistence.*;
@Entity
public class SourceTable {
  @Id @GeneratedValue public Long id;
  @ManyToOne public SourceImport sourceImport;
  public String tableName;
  public String displayName;
  public long rowCount;
  public int columnCount;
}
