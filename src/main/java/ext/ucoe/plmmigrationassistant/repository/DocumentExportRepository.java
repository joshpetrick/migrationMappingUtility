package ext.ucoe.plmmigrationassistant.repository;
import ext.ucoe.plmmigrationassistant.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface DocumentExportRepository
    extends JpaRepository<DocumentExport, Long> {
  default List<DocumentExport> findAllByProjectId(Long projectId) {
    return findAll();
  }
}
