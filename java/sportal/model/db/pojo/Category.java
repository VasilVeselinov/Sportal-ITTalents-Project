package sportal.model.db.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.service.dto.CategoryServiceDTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category extends BasePOJO {

    private String categoryName;

    public Category(String categoryName) {
        this.setCategoryName(categoryName);
    }

    public Category(long id, String categoryName) {
        this(categoryName);
        this.setId(id);
    }

    public static List<Category> fromDTOToPojo(List<CategoryServiceDTO> categories) {
        List<Category> pojoList = new ArrayList<>();
        for (CategoryServiceDTO category : categories) {
            pojoList.add(new Category(category.getId(), category.getCategoryName()));
        }
        return pojoList;
    }
}
