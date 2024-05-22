package platform.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import platform.models.Snippet;

import java.util.List;

public interface SnippetsRepository extends CrudRepository<Snippet, String> {
    @Query("SELECT s FROM Snippet s WHERE s.isViewRestricted = false AND s.isTimeRestricted = false ORDER BY s.date DESC")
    List<Snippet> findNotRestricted(Pageable pageable);
}
