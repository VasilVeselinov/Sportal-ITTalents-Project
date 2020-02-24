package sportal.model.validators;

import sportal.exception.ExistsObjectException;
import sportal.model.db.pojo.Category;
import sportal.model.service.dto.CategoryServiceDTO;

import java.util.List;

public class CategoryValidator extends AbstractValidator {

    public static List<CategoryServiceDTO> conformityCheck(List<Category> existsCategories,
                                                           List<CategoryServiceDTO> categories) {
        int countValidCategory = 0;
        for (CategoryServiceDTO categoryDTO : categories) {
            for (Category category : existsCategories) {
                if (categoryDTO.getId() == category.getId()) {
                    countValidCategory++;
                }
            }
        }
        if (countValidCategory != categories.size()) {
            throw new ExistsObjectException(SOME_OF_THE_CATEGORIES_DO_NOT_EXIST);
        }
        return categories;
    }
}
