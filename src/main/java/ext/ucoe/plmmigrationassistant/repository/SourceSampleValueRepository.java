package ext.ucoe.plmmigrationassistant.repository;
import ext.ucoe.plmmigrationassistant.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface SourceSampleValueRepository
    extends JpaRepository<SourceSampleValue, Long> {
  default List<SourceSampleValue> findAllByProjectId(Long projectId) {
    return findAll();
  }
}
