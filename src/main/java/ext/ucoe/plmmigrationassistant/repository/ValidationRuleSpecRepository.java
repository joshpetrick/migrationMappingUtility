package ext.ucoe.plmmigrationassistant.repository;
import ext.ucoe.plmmigrationassistant.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface ValidationRuleSpecRepository
    extends JpaRepository<ValidationRuleSpec, Long> {
  default List<ValidationRuleSpec> findAllByProjectId(Long projectId) {
    return findAll();
  }
}
