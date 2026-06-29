package ext.ucoe.plmmigrationassistant.repository;
import ext.ucoe.plmmigrationassistant.domain.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TargetModelRepository
    extends JpaRepository<TargetModel, Long> {
  default List<TargetModel> findAllByProjectId(Long projectId) {
    return findAll();
  }
}
