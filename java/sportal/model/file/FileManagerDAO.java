package sportal.model.file;

import org.springframework.web.multipart.MultipartFile;
import sportal.model.db.pojo.Picture;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class FileManagerDAO extends Thread {

    private List<MultipartFile> multipartFiles;
    private String packageName;
    private List<Picture> pictures;

    public FileManagerDAO(List<MultipartFile> multipartFiles, String packageName, List<Picture> pictures) {
        this.multipartFiles = multipartFiles;
        this.packageName = packageName;
        this.pictures = pictures;
    }

    @Override
    public void run() {
        for (int i = 0; i < multipartFiles.size(); i++) {
            try {
                this.savePicture(this.multipartFiles.get(i + 1),
                        this.packageName + this.pictures.get(i).getUrlOFPicture());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void savePicture(MultipartFile multipartFile, String fullPathUrlOfPicture) throws IOException {
        multipartFile.transferTo(Paths.get(fullPathUrlOfPicture));
    }
}
