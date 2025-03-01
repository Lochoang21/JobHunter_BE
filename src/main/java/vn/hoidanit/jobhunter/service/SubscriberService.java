package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.Subscriber;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.repository.SubscriberRepository;

@Service
public class SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final SkillRepository skillRepository;

    public SubscriberService(SubscriberRepository subscriberRepository, SkillRepository skillRepository) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
    }

    public boolean isExistEmail(String email) {
        return this.subscriberRepository.existsByEmail(email);
    }

    public Subscriber findById(long id){
         Optional<Subscriber> suOptional = this.subscriberRepository.findById(id);
       if(suOptional.isPresent()){
           return  suOptional.get();
       }

         return null;
               
    }

    public Subscriber createSubcriber(Subscriber subscriber) {
        if(subscriber.getSkills() != null){
            List<Long> reqSkill = subscriber.getSkills().stream().map(x -> x.getId()).collect(Collectors.toList());
            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkill);
            subscriber.setSkills(dbSkills);
        }
        return this.subscriberRepository.save(subscriber);
    }

    public Subscriber updateSubscriber(Subscriber subDB, Subscriber subscriber) {
        //check skills
        if(subscriber.getSkills() != null){
            List<Long> reqSkill = subscriber.getSkills().stream().map(x -> x.getId()).collect(Collectors.toList());

            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkill);
            subDB.setSkills(dbSkills);
        }
        return this.subscriberRepository.save(subDB);
    }
}
