package sportal.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class ExceptionObject {

    private String messages;
    private int status;
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime time;
    private String exceptionClass;

    @Override
    public String toString() {
        return "ExceptionObject{" +
                "messages='" + messages + '\'' +
                ", status=" + status +
                ", time=" + time +
                ", exceptionClass='" + exceptionClass + '\'' +
                '}';
    }
}
