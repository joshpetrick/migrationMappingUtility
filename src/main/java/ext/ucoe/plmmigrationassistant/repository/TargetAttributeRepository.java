package ext.ucoe.plmmigrationassistant.repository;
import ext.ucoe.plmmigrationassistant.domain.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TargetAttributeRepository
    extends JpaRepository<TargetAttribute, Long> {
  List<TargetAttribute>
  findByTargetObjectTypeIdOrderBySortOrderAsc(Long objectTypeId);
}
