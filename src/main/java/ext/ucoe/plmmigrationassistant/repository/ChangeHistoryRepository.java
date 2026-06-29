package ext.ucoe.plmmigrationassistant.repository;
import ext.ucoe.plmmigrationassistant.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface ChangeHistoryRepository
    extends JpaRepository<ChangeHistory, Long> {
  default List<ChangeHistory> findAllByProjectId(Long projectId) {
    return findAll();
  }
}
