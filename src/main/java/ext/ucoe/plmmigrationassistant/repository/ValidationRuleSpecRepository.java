package ext.ucoe.plmmigrationassistant.repository;
import ext.ucoe.plmmigrationassistant.domain.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ValidationRuleSpecRepository
    extends JpaRepository<ValidationRuleSpec, Long> {
  default List<ValidationRuleSpec> findAllByProjectId(Long projectId) {
    return findAll();
  }
}
