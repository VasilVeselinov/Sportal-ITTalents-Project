package sportal.model.validators;

import sportal.exception.NotExistsObjectException;
import sportal.model.db.pojo.Category;
import sportal.model.service.dto.CategoryServiceDTO;

import java.util.List;

public class CategoryValidator {

    private static final String SOME_OF_THE_CATEGORIES_DO_NOT_EXIST = "Some of the categories do not exist!";

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
            throw new NotExistsObjectException(SOME_OF_THE_CATEGORIES_DO_NOT_EXIST);
        }
        return categories;
    }
}
