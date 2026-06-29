package ext.ucoe.plmmigrationassistant.repository;
import ext.ucoe.plmmigrationassistant.domain.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface DocumentExportRepository
    extends JpaRepository<DocumentExport, Long> {
  default List<DocumentExport> findAllByProjectId(Long projectId) {
    return findAll();
  }
}
