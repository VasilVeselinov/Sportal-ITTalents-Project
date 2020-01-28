package sportal.model.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.dto.category.CategoryRequestDTO;

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

    public Category(CategoryRequestDTO editDTO) {
        this.setId(editDTO.getId());
        this.setCategoryName(editDTO.getCategoryName());
    }

    public Category(String categoryName) {
        this.setCategoryName(categoryName);
    }

    public static List<Category> fromCategoryRequestDTOToCategory(List<CategoryRequestDTO> categories) {
        List<Category> categoryList =new ArrayList<>();
        for (CategoryRequestDTO categoryRequestDTO: categories){
         categoryList.add(new Category(categoryRequestDTO));
        }
        return categoryList;
    }
}
