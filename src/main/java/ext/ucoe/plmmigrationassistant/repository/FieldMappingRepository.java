package ext.ucoe.plmmigrationassistant.repository;
import ext.ucoe.plmmigrationassistant.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface FieldMappingRepository
    extends JpaRepository<FieldMapping, Long> {
  Optional<FieldMapping> findByTargetAttributeId(Long targetAttributeId);
  List<FieldMapping> findByMappingSetProjectId(Long projectId);
  List<FieldMapping> findByTargetObjectTypeId(Long objectTypeId);
}
