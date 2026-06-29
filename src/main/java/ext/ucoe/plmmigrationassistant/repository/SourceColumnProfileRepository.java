package ext.ucoe.plmmigrationassistant.repository;
import ext.ucoe.plmmigrationassistant.domain.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface SourceColumnProfileRepository
    extends JpaRepository<SourceColumnProfile, Long> {
  List<SourceColumnProfile> findBySourceTableId(Long sourceTableId);
  Optional<SourceColumnProfile>
  findFirstBySourceTableTableNameAndColumnName(String tableName,
                                               String columnName);
}
