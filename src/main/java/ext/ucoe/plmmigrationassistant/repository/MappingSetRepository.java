package ext.ucoe.plmmigrationassistant.repository;
import ext.ucoe.plmmigrationassistant.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface MappingSetRepository extends JpaRepository<MappingSet, Long> {
  Optional<MappingSet> findFirstByProjectIdOrderByCreatedAtDesc(Long projectId);
}
