package ext.ucoe.plmmigrationassistant.repository;
import ext.ucoe.plmmigrationassistant.domain.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface GapDecisionRepository
    extends JpaRepository<GapDecision, Long> {
  default List<GapDecision> findAllByProjectId(Long projectId) {
    return findAll();
  }
}
