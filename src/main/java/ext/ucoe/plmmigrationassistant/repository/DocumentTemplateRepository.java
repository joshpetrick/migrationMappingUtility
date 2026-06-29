package ext.ucoe.plmmigrationassistant.repository;
import ext.ucoe.plmmigrationassistant.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface DocumentTemplateRepository
    extends JpaRepository<DocumentTemplate, Long> {
  default List<DocumentTemplate> findAllByProjectId(Long projectId) {
    return findAll();
  }
}
