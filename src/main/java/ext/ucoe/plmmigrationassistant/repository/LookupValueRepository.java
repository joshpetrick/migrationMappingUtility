package ext.ucoe.plmmigrationassistant.repository;
import ext.ucoe.plmmigrationassistant.domain.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface LookupValueRepository
    extends JpaRepository<LookupValue, Long> {
  default List<LookupValue> findAllByProjectId(Long projectId) {
    return findAll();
  }
}
