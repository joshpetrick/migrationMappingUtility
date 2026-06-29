package ext.ucoe.plmmigrationassistant.repository;
import ext.ucoe.plmmigrationassistant.domain.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface LookupTableRepository
    extends JpaRepository<LookupTable, Long> {
  default List<LookupTable> findAllByProjectId(Long projectId) {
    return findAll();
  }
}
