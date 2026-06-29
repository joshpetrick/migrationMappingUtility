package ext.ucoe.plmmigrationassistant.repository;
import ext.ucoe.plmmigrationassistant.domain.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ChangeHistoryRepository
    extends JpaRepository<ChangeHistory, Long> {
  default List<ChangeHistory> findAllByProjectId(Long projectId) {
    return findAll();
  }
}
