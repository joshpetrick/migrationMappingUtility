package ext.ucoe.plmmigrationassistant.repository;
import ext.ucoe.plmmigrationassistant.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface LookupValueRepository
    extends JpaRepository<LookupValue, Long> {
  default List<LookupValue> findAllByProjectId(Long projectId) {
    return findAll();
  }
}
