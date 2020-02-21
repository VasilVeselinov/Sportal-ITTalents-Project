package sportal.model.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sportal.model.db.pojo.Picture;

import java.util.List;

@Repository
public interface PictureRepository extends JpaRepository<Picture, Long> {

    List<Picture> findAllByArticleIdIsNull();
    List<Picture> findAllByArticleId(long articleId);
}
