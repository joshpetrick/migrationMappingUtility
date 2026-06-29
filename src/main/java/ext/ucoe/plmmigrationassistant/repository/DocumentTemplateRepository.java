package ext.ucoe.plmmigrationassistant.repository;
import ext.ucoe.plmmigrationassistant.domain.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface DocumentTemplateRepository
    extends JpaRepository<DocumentTemplate, Long> {
  default List<DocumentTemplate> findAllByProjectId(Long projectId) {
    return findAll();
  }
}
