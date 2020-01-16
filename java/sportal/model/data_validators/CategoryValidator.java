package sportal.model.data_validators;

import sportal.exception.BadRequestException;
import sportal.exception.ExistsObjectException;
import sportal.model.dto.category.CategoryRequestDTO;
import sportal.model.pojo.Category;

import java.util.List;

import static sportal.controller.AbstractController.WRONG_REQUEST;

public class CategoryValidator extends AbstractValidator {

    public static CategoryRequestDTO checkForValidData(
            CategoryRequestDTO categoryRequestDTO) throws BadRequestException {
        if (categoryRequestDTO == null) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        if (categoryRequestDTO.getCategoryName() == null || categoryRequestDTO.getCategoryName().isEmpty()) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        if (categoryRequestDTO.getCategoryId() < 0) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        return categoryRequestDTO;
    }

    public static List<Category> conformityCheck(List<Category> existsCategories,
                                                 List<CategoryRequestDTO> categories) {
        int countValidCategory = 0;
        for (CategoryRequestDTO categoryDTO : categories) {
            for (Category category : existsCategories) {
                if (categoryDTO.getCategoryId() == category.getId()) {
                    countValidCategory++;
                }
            }
        }
        if (countValidCategory != categories.size()) {
            throw new ExistsObjectException(SOME_OF_THE_CATEGORIES_DO_NOT_EXIST);
        }
        return Category.fromCategoryRequestDTOToCategory(categories);
    }
}
