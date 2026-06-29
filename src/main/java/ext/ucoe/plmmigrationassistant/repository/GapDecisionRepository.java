package ext.ucoe.plmmigrationassistant.repository;
import ext.ucoe.plmmigrationassistant.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface GapDecisionRepository
    extends JpaRepository<GapDecision, Long> {
  default List<GapDecision> findAllByProjectId(Long projectId) {
    return findAll();
  }
}
