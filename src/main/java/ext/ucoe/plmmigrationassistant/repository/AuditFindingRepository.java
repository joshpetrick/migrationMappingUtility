package ext.ucoe.plmmigrationassistant.repository;

import ext.ucoe.plmmigrationassistant.domain.AuditFinding;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditFindingRepository
    extends JpaRepository<AuditFinding, Long> {
  List<AuditFinding> findAllByProjectId(Long projectId);

  void deleteByProjectId(Long projectId);
}
