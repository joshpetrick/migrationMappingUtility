package ext.ucoe.plmmigrationassistant.repository;
import ext.ucoe.plmmigrationassistant.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface SourceImportRepository
    extends JpaRepository<SourceImport, Long> {
  List<SourceImport> findByProjectId(Long projectId);
}
