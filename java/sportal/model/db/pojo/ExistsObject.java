package sportal.model.db.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class ExistsObject {

    private Long leftColumnId;
    private Long rightColumnId;
    private Long leftId;
}
