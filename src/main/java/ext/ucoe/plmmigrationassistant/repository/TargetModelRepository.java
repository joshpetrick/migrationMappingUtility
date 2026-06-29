package ext.ucoe.plmmigrationassistant.repository;
import ext.ucoe.plmmigrationassistant.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface TargetModelRepository
    extends JpaRepository<TargetModel, Long> {
  default List<TargetModel> findAllByProjectId(Long projectId) {
    return findAll();
  }
}
