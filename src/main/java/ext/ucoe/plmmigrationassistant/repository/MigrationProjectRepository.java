package ext.ucoe.plmmigrationassistant.repository;
import ext.ucoe.plmmigrationassistant.domain.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface MigrationProjectRepository
    extends JpaRepository<MigrationProject, Long> {
  default List<MigrationProject> findAllByProjectId(Long projectId) {
    return findAll();
  }
}
