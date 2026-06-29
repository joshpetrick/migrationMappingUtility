package ext.ucoe.plmmigrationassistant.repository;
import ext.ucoe.plmmigrationassistant.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface TargetObjectTypeRepository
    extends JpaRepository<TargetObjectType, Long> {
  List<TargetObjectType>
  findByTargetModelProjectIdAndActiveTrueOrderBySortOrderAsc(Long projectId);
}
