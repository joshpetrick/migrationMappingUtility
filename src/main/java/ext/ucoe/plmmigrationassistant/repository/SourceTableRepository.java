package ext.ucoe.plmmigrationassistant.repository;
import ext.ucoe.plmmigrationassistant.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface SourceTableRepository
    extends JpaRepository<SourceTable, Long> {
  List<SourceTable> findBySourceImportProjectId(Long projectId);
  List<SourceTable> findBySourceImportId(Long sourceImportId);
  Optional<SourceTable> findFirstByTableName(String tableName);
}
