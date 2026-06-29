package ext.ucoe.plmmigrationassistant.repository;
import ext.ucoe.plmmigrationassistant.domain.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TargetObjectTypeRepository
    extends JpaRepository<TargetObjectType, Long> {
  List<TargetObjectType>
  findByTargetModelProjectIdAndActiveTrueOrderBySortOrderAsc(Long projectId);
}
