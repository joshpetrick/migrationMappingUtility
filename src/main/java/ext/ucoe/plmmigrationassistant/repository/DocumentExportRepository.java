package ext.ucoe.plmmigrationassistant.repository;

import ext.ucoe.plmmigrationassistant.domain.DocumentExport;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentExportRepository
    extends JpaRepository<DocumentExport, Long> {
  List<DocumentExport> findAllByProjectId(Long projectId);
}
