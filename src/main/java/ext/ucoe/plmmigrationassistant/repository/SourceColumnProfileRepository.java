package ext.ucoe.plmmigrationassistant.repository;
import ext.ucoe.plmmigrationassistant.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface SourceColumnProfileRepository
    extends JpaRepository<SourceColumnProfile, Long> {
  List<SourceColumnProfile> findBySourceTableId(Long sourceTableId);
  Optional<SourceColumnProfile>
  findFirstBySourceTableTableNameAndColumnName(String tableName,
                                               String columnName);
}
