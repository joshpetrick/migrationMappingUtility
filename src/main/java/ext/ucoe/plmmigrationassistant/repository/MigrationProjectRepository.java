package ext.ucoe.plmmigrationassistant.repository;
import ext.ucoe.plmmigrationassistant.domain.*;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
public interface MigrationProjectRepository
    extends JpaRepository<MigrationProject, Long> {
  default List<MigrationProject> findAllByProjectId(Long projectId) {
    return findAll();
  }
}
