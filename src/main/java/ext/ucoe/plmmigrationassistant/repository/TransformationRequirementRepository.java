package ext.ucoe.plmmigrationassistant.repository;
import ext.ucoe.plmmigrationassistant.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface TransformationRequirementRepository
    extends JpaRepository<TransformationRequirement, Long> {
  default List<TransformationRequirement> findAllByProjectId(Long projectId) {
    return findAll();
  }
}
