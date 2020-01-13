package sportal.model.data_validators;

import sportal.exception.BadRequestException;
import sportal.model.dto.category.CategoryEditDTO;

import static sportal.controller.AbstractController.WRONG_REQUEST;

public class CategoryValidator extends AbstractValidator {


    public static CategoryEditDTO checkForValidData(CategoryEditDTO categoryEditDTO) throws BadRequestException {
        if (categoryEditDTO == null) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        if (
                categoryEditDTO.getOldCategory() == null ||
                        categoryEditDTO.getNewCategoryName() == null ||
                        categoryEditDTO.getNewCategoryName().isEmpty()
        ) {
            throw new BadRequestException(WRONG_REQUEST);
        }
        return categoryEditDTO;
    }
}
