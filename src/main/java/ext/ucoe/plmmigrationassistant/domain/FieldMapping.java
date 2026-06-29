package ext.ucoe.plmmigrationassistant.domain;
import static ext.ucoe.plmmigrationassistant.domain.Enums.*;

import jakarta.persistence.*;
import java.time.Instant;
@Entity
public class FieldMapping {
  @Id @GeneratedValue public Long id;
  @ManyToOne public MappingSet mappingSet;
  @ManyToOne public TargetObjectType targetObjectType;
  @ManyToOne public TargetAttribute targetAttribute;
  @ManyToOne public SourceTable sourceTable;
  @ManyToOne public SourceColumnProfile sourceColumn;
  @Column(length = 2000) public String sourceExpression;
  @Enumerated(EnumType.STRING)
  public MappingType mappingType = MappingType.DIRECT;
  @Enumerated(EnumType.STRING)
  public MappingStatus mappingStatus = MappingStatus.NOT_STARTED;
  public String defaultValue;
  @ManyToOne public LookupTable lookupTable;
  @Column(length = 4000) public String transformationRequirement;
  @Column(length = 4000) public String implementationNotes;
  @Column(length = 4000) public String validationNotes;
  @Column(length = 4000) public String assumptions;
  public boolean customerDecisionRequired;
  public String approvalStatus;
  public String owner;
  public Instant updatedAt = Instant.now();
}
