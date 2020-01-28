package sportal.model.data_validators;

import sportal.exception.BadRequestException;
import sportal.exception.ExistsObjectException;
import sportal.model.dto.category.CategoryRequestDTO;
import sportal.model.pojo.Category;

import java.util.List;

public class CategoryValidator extends AbstractValidator {

    public static CategoryRequestDTO checkForValidData(
            CategoryRequestDTO categoryRequestDTO) throws BadRequestException {
        if (categoryRequestDTO == null) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        if (categoryRequestDTO.getCategoryName() == null || categoryRequestDTO.getCategoryName().isEmpty()) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        if (categoryRequestDTO.getId() < 1) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        return categoryRequestDTO;
    }

    public static List<Category> conformityCheck(List<Category> existsCategories,
                                                 List<CategoryRequestDTO> categories) {
        int countValidCategory = 0;
        for (CategoryRequestDTO categoryDTO : categories) {
            for (Category category : existsCategories) {
                if (categoryDTO.getId() == category.getId()) {
                    countValidCategory++;
                }
            }
        }
        if (countValidCategory != categories.size()) {
            throw new ExistsObjectException(SOME_OF_THE_CATEGORIES_DO_NOT_EXIST);
        }
        return Category.fromCategoryRequestDTOToCategory(categories);
    }

    public static CategoryRequestDTO checkForValidNewCategory(
            CategoryRequestDTO categoryRequestDTO) throws BadRequestException {
        if (categoryRequestDTO == null) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        if (categoryRequestDTO.getCategoryName().isEmpty() || categoryRequestDTO.getCategoryName() == null) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        return categoryRequestDTO;
    }
}
