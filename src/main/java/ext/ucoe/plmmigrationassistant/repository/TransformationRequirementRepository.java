package ext.ucoe.plmmigrationassistant.repository;
import ext.ucoe.plmmigrationassistant.domain.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TransformationRequirementRepository
    extends JpaRepository<TransformationRequirement, Long> {
  default List<TransformationRequirement> findAllByProjectId(Long projectId) {
    return findAll();
  }
}
