package sportal.model.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sportal.model.db.pojo.Article;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

}
