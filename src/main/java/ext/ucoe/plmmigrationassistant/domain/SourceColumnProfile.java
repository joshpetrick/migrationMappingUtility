package ext.ucoe.plmmigrationassistant.domain;
import jakarta.persistence.*;
@Entity
public class SourceColumnProfile {
  @Id @GeneratedValue public Long id;
  @ManyToOne public SourceTable sourceTable;
  public String columnName;
  public String dataTypeGuess;
  public long nullCount;
  public double nullPercentage;
  public long distinctCount;
  public Integer maxLength;
  public String minValue;
  public String maxValue;
  public long duplicateCount;
  @Column(length = 4000) public String sampleValues;
  @Column(length = 2000) public String profileNotes;
  public boolean likelyEnum;
  public boolean duplicateIndicator;
}
