package vn.hoidanit.jobhunter.domain.response.resume;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResCeateResumeDTO {
    private long id;
    private Instant createAt;
    private String createBy;
}
