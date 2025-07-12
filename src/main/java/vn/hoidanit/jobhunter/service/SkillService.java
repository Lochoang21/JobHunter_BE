package vn.hoidanit.jobhunter.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;

@Service
public class SkillService {
    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public boolean isNameExist(String name) {
        return this.skillRepository.existsByName(name);
    }

    public Skill handleCreateSkill (Skill skill) {
        return this.skillRepository.save(skill);
    }

    public Skill updateSkill (Skill skill) {
        return this.skillRepository.save(skill);
    }

    public Skill getSkillById (long id) {
        Optional<Skill> skillOptional = this.skillRepository.findById(id);
       if(skillOptional.isPresent()){
           return  skillOptional.get();
       }

         return null;
               
    }

    public ResultPaginationDTO handleGetAllSkill(Specification<Skill> spec, Pageable pageable) {
        Page<Skill> pageSkill = this.skillRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pageSkill.getTotalPages());
        meta.setTotal(pageSkill.getTotalElements());

        rs.setMeta(meta);

        rs.setResult(pageSkill.getContent());

        return rs;
    }

    public void deleteSkill(long id) {

        Optional <Skill> skillOptional = this.skillRepository.findById(id);
        Skill currentSkill = skillOptional.get();
        
        currentSkill.getJobs().forEach(job -> job.getSkills().remove(currentSkill));

        //delete Subscriber
        currentSkill.getSubscribers().forEach(subs -> subs.getSkills().remove(currentSkill));


        this.skillRepository.delete(currentSkill);
    }
}
