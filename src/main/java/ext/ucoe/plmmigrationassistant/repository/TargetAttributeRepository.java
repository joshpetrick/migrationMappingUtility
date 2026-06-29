package ext.ucoe.plmmigrationassistant.repository;
import ext.ucoe.plmmigrationassistant.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface TargetAttributeRepository
    extends JpaRepository<TargetAttribute, Long> {
  List<TargetAttribute>
  findByTargetObjectTypeIdOrderBySortOrderAsc(Long objectTypeId);
}
