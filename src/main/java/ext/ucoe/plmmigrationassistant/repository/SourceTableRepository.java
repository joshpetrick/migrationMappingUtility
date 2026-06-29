package ext.ucoe.plmmigrationassistant.repository;
import ext.ucoe.plmmigrationassistant.domain.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface SourceTableRepository
    extends JpaRepository<SourceTable, Long> {
  List<SourceTable> findBySourceImportProjectId(Long projectId);
  List<SourceTable> findBySourceImportId(Long sourceImportId);
  Optional<SourceTable> findFirstByTableName(String tableName);
}
