package ext.ucoe.plmmigrationassistant.repository;

import ext.ucoe.plmmigrationassistant.domain.GapDecision;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GapDecisionRepository
    extends JpaRepository<GapDecision, Long> {
  List<GapDecision> findAllByProjectId(Long projectId);
}
