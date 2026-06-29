package ext.ucoe.plmmigrationassistant.repository;
import ext.ucoe.plmmigrationassistant.domain.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface SourceSampleValueRepository
    extends JpaRepository<SourceSampleValue, Long> {
  default List<SourceSampleValue> findAllByProjectId(Long projectId) {
    return findAll();
  }
}
