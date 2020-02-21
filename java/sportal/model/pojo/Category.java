package sportal.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.controller.models.category.CategoryRequestModel;
import sportal.model.service.dto.CategoryServiceDTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String categoryName;

    public Category(CategoryServiceDTO editDTO) {
        this.setId(editDTO.getId());
        this.setCategoryName(editDTO.getCategoryName());
    }

    public Category(String categoryName) {
        this.setCategoryName(categoryName);
    }

    public static List<Category> fromDTOToPojo(List<CategoryServiceDTO> categories) {
        List<Category> pojoList =new ArrayList<>();
        for (CategoryServiceDTO category: categories){
            pojoList.add(new Category(category));
        }
        return pojoList;
    }
}
