package sportal.controller.models.category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.controller.validation.NameValid;

import javax.validation.constraints.Positive;

@NoArgsConstructor
@Getter
@Setter
public class CategoryRequestModel {

    @Positive(message = "Id must be greater than 0!")
    private long id;
    @NameValid
    private String categoryName;
}
