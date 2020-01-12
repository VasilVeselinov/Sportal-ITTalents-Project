package sportal.exception;

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
    private LocalDateTime time;
    private String exceptionClass;
}
