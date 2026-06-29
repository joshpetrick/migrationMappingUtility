package ext.ucoe.plmmigrationassistant.domain;
import jakarta.persistence.*;
import java.time.*;
@Entity
public class LookupValue {
  @Id @GeneratedValue public Long id;
  @ManyToOne public LookupTable lookupTable;
  public String sourceValue;
  public String targetValue;
  public String status;
  public boolean approved;
  public boolean unresolved;
  @Column(length = 2000) public String notes;
  public LocalDate customerApprovalDate;
  public Instant updatedAt = Instant.now();
}
