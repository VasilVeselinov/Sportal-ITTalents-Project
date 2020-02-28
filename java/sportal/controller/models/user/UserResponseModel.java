package sportal.controller.models.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sportal.model.service.dto.UserServiceDTO;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class UserResponseModel {

    private long id;
    private String username;
    private String userEmail;

    public UserResponseModel(UserServiceDTO serviceDTO) {
        this.id = serviceDTO.getId();
        this.username = serviceDTO.getUsername();
        this.userEmail = serviceDTO.getUserEmail();
    }

    public static List<UserResponseModel> fromDTOToModel(List<UserServiceDTO> users) {
        List<UserResponseModel> modelList = new ArrayList<>();
        for (UserServiceDTO dto: users){
            modelList.add(new UserResponseModel(dto));
        }
        return modelList;
    }
}
