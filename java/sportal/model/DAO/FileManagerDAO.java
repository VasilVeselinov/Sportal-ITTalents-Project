package sportal.model.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;

@Getter
@Setter
@AllArgsConstructor
public class FileManagerDAO extends Thread {

    private MultipartFile multipartFile;
    private String urlOfPicture;

    @Override
    public void run() {
        try {
            this.savePicture(this.multipartFile, this.urlOfPicture);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void savePicture(MultipartFile multipartFile, String urlOfPicture) throws IOException {
        multipartFile.transferTo(Paths.get(urlOfPicture));
    }
}
