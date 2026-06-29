package ext.ucoe.plmmigrationassistant.domain;
import jakarta.persistence.*;
@Entity
public class SourceSampleValue {
  @Id @GeneratedValue public Long id;
  @ManyToOne public SourceColumnProfile sourceColumnProfile;
  public String sampleValue;
}
