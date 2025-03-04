package vn.hoidanit.jobhunter.domain.response.email;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.LevelEnum;

@Getter
@Setter
public class ResEmailJob {
    private String name;
    private double salary;
    private CompanyEmail company;
    private String description;
    private LevelEnum level;
    private List<SkillEmail> skills;


    @Getter
    @Setter
    @AllArgsConstructor
    public static class CompanyEmail {
    
        private String name;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class SkillEmail {
    
        private String name;
    }
}
