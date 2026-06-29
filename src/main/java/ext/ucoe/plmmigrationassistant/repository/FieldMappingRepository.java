package ext.ucoe.plmmigrationassistant.repository;
import ext.ucoe.plmmigrationassistant.domain.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface FieldMappingRepository
    extends JpaRepository<FieldMapping, Long> {
  Optional<FieldMapping> findByTargetAttributeId(Long targetAttributeId);
  List<FieldMapping> findByMappingSetProjectId(Long projectId);
  List<FieldMapping> findByTargetObjectTypeId(Long objectTypeId);
}
