package sportal.controller.models.category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.pojo.Category;
import sportal.model.service.dto.CategoryServiceDTO;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class CategoryResponseModel {

    private long id;
    private String categoryName;

    public CategoryResponseModel(CategoryServiceDTO serviceDTO) {
        this.setId(serviceDTO.getId());
        this.setCategoryName(serviceDTO.getCategoryName());
    }

    public static List<CategoryResponseModel> fromDTOToModel(List<CategoryServiceDTO> categories) {
        List<CategoryResponseModel> responseDTOList = new ArrayList<>();
        for (CategoryServiceDTO category : categories) {
            responseDTOList.add(new CategoryResponseModel(category));
        }
        return responseDTOList;
    }
}
