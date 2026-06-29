package ext.ucoe.plmmigrationassistant.repository;
import ext.ucoe.plmmigrationassistant.domain.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface MappingSetRepository extends JpaRepository<MappingSet, Long> {
  Optional<MappingSet> findFirstByProjectIdOrderByCreatedAtDesc(Long projectId);
}
