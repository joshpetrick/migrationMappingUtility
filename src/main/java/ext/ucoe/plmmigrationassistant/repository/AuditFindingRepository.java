package ext.ucoe.plmmigrationassistant.repository;
import ext.ucoe.plmmigrationassistant.domain.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface AuditFindingRepository
    extends JpaRepository<AuditFinding, Long> {
  default List<AuditFinding> findAllByProjectId(Long projectId) {
    return findAll();
  }
}
