package sportal.model.validators;

import sportal.exception.BadRequestException;
import sportal.exception.ExistsObjectException;
import sportal.model.db.pojo.Category;
import sportal.model.service.dto.CategoryServiceDTO;

import java.util.List;

public class CategoryValidator extends AbstractValidator {

    public static CategoryServiceDTO checkForValidData(
            CategoryServiceDTO serviceDTO) throws BadRequestException {
        if (serviceDTO == null) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        if (serviceDTO.getCategoryName() == null || serviceDTO.getCategoryName().isEmpty()) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        if (serviceDTO.getId() < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        return serviceDTO;
    }

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

    public static CategoryServiceDTO checkForValidNewCategory(
            CategoryServiceDTO serviceDTO) throws BadRequestException {
        if (serviceDTO == null) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        if (serviceDTO.getCategoryName().isEmpty() || serviceDTO.getCategoryName() == null) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        return serviceDTO;
    }
}
