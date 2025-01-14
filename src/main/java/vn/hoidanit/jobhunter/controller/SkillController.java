package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.micrometer.core.instrument.Meter.Id;
import vn.hoidanit.jobhunter.service.SkillService;

import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1")
public class SkillController {
    
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }
    
    @PostMapping("/skills")
    @ApiMessage("Create a new skill")
    public ResponseEntity<Skill> createSkill(@Valid @RequestBody Skill skill) throws IdInvalidException {
        
        if(skill.getName() != null && this.skillService.isNameExist(skill.getName())){
            throw new IdInvalidException("Skill với tên: " + skill.getName() + " đã tồn tại!");
        }
        
        return ResponseEntity.status(HttpStatus.CREATED).body(this.skillService.handleCreateSkill(skill));
    }

    @PutMapping("/skills")
    @ApiMessage("Update a skill")
    public ResponseEntity<Skill> updateSkill(@Valid @RequestBody Skill skill) throws IdInvalidException {
        Skill currentSkill = this.skillService.getSkillById(skill.getId());
        //check id
        if(currentSkill == null){
            throw new IdInvalidException("Skill với id: " + skill.getId() + " khong tồn tại!");
        }
        //check name    
        if(skill.getName() != null && !currentSkill.getName().equals(skill.getName()) && this.skillService.isNameExist(skill.getName())){
            throw new IdInvalidException("Skill với tên: " + skill.getName() + " đã tồn tại!");
        }
        currentSkill.setName(skill.getName());
        return ResponseEntity.status(HttpStatus.OK).body(this.skillService.updateSkill(currentSkill));
    }

    @GetMapping("/skills")
    @ApiMessage("Fetch all skills")
    public ResponseEntity<ResultPaginationDTO> getAllSkill(Specification<Skill> spec, Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(this.skillService.handleGetAllSkill(spec, pageable));
    }

    @DeleteMapping("/skills/{id}")
    @ApiMessage("Delete a skill")
    public ResponseEntity<Void> deleteSkill(@PathVariable("id") long id) throws IdInvalidException {
        Skill currentSkill = this.skillService.getSkillById(id);
        if(currentSkill == null){
            throw new IdInvalidException("Skill với id: " + id + " không tồn tại!");
        }
        this.skillService.deleteSkill(id);
        return ResponseEntity.ok().body(null);
    }
    
    
}
