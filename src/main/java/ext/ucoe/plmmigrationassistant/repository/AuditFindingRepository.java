package ext.ucoe.plmmigrationassistant.repository;
import ext.ucoe.plmmigrationassistant.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface AuditFindingRepository
    extends JpaRepository<AuditFinding, Long> {
  default List<AuditFinding> findAllByProjectId(Long projectId) {
    return findAll();
  }
}
