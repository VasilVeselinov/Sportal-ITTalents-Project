package sportal.model.file;

import org.springframework.web.multipart.MultipartFile;
import sportal.model.db.pojo.Picture;
import sportal.model.db.pojo.Video;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class FileManagerDAO extends Thread {

    private List<MultipartFile> multipartFiles;
    private String packageName;
    private List<Picture> pictures;
    private MultipartFile multipartFile;
    private Video video;

    public FileManagerDAO(List<MultipartFile> multipartFiles, String packageName, List<Picture> pictures) {
        this.multipartFiles = multipartFiles;
        this.packageName = packageName;
        this.pictures = pictures;
    }

    public FileManagerDAO(MultipartFile multipartFile, String packageName, Video video) {
        this.multipartFile = multipartFile;
        this.packageName = packageName;
        this.video = video;
    }

    @Override
    public void run() {
        if (this.pictures != null) {
            for (int i = 0; i < multipartFiles.size(); i++) {
                try {
                    this.savePicture(this.multipartFiles.get(i),
                            this.packageName + this.pictures.get(i).getUrlOfPicture());
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        } else {
            try {
                this.saveVideo(this.multipartFile,
                        this.packageName + this.video.getUrlOfVideo());
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void savePicture(MultipartFile multipartFile, String fullPathUrlOfPicture) throws IOException {
        multipartFile.transferTo(Paths.get(fullPathUrlOfPicture));
    }

    private void saveVideo(MultipartFile multipartFile, String fullPathUrlOfPicture) throws IOException {
        multipartFile.transferTo(Paths.get(fullPathUrlOfPicture));
    }
}
