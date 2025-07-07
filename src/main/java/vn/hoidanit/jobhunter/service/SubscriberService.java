package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Subscriber;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.email.ResEmailJob;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.repository.SubscriberRepository;

@Service
public class SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final SkillRepository skillRepository;
    private final JobRepository jobRepository;
    private final EmailService emailService;

    public SubscriberService(SubscriberRepository subscriberRepository, SkillRepository skillRepository,
            JobRepository jobRepository, EmailService emailService) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
        this.jobRepository = jobRepository;
        this.emailService = emailService;
    }

    public ResultPaginationDTO handleGetAllSkill(Specification<Subscriber> spec, Pageable pageable) {
        Page<Subscriber> pageSubscriber = this.subscriberRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());

        meta.setPages(pageSubscriber.getTotalPages());
        meta.setTotal(pageSubscriber.getTotalElements());

        rs.setMeta(meta);

        rs.setResult(pageSubscriber.getContent());

        return rs;
    }

    public boolean isExistEmail(String email) {
        return this.subscriberRepository.existsByEmail(email);
    }

    public Subscriber findByEmail(String email) {
        return this.subscriberRepository.findByEmail(email);
    }

    // @Scheduled(cron = "*/10 * * * * *")
    // public void testCron(){
    // System.out.println(">>> test cron");
    // }

    public Subscriber findById(long id) {
        Optional<Subscriber> suOptional = this.subscriberRepository.findById(id);
        if (suOptional.isPresent()) {
            return suOptional.get();
        }

        return null;

    }

    public Subscriber createSubcriber(Subscriber subscriber) {
        if (subscriber.getSkills() != null) {
            List<Long> reqSkill = subscriber.getSkills().stream().map(x -> x.getId()).collect(Collectors.toList());
            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkill);
            subscriber.setSkills(dbSkills);
        }
        return this.subscriberRepository.save(subscriber);
    }

    public Subscriber updateSubscriber(Subscriber subDB, Subscriber subscriber) {
        // check skills
        if (subscriber.getSkills() != null) {
            List<Long> reqSkill = subscriber.getSkills().stream().map(x -> x.getId()).collect(Collectors.toList());

            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkill);
            subDB.setSkills(dbSkills);
        }
        return this.subscriberRepository.save(subDB);
    }

    public ResEmailJob convertJobToSendEmail(Job job) {
        ResEmailJob res = new ResEmailJob();
        res.setName(job.getName());
        res.setSalary(job.getSalary());
        res.setDescription(job.getDescription());
        res.setLevel(job.getLevel());
        res.setCompany(new ResEmailJob.CompanyEmail(job.getCompany().getName()));

        List<Skill> skills = job.getSkills();

        List<ResEmailJob.SkillEmail> skillEmails = skills.stream()
                .map(skill -> new ResEmailJob.SkillEmail(skill.getName())).collect(Collectors.toList());

        res.setSkills(skillEmails);
        return res;
    }

    public void sendSubscribersEmailJobs() {
        List<Subscriber> listSubs = this.subscriberRepository.findAll();
        if (listSubs != null && listSubs.size() > 0) {
            for (Subscriber sub : listSubs) {
                List<Skill> listSkills = sub.getSkills();
                if (listSkills != null && listSkills.size() > 0) {
                    List<Job> listJobs = this.jobRepository.findBySkillsIn(listSkills);
                    if (listJobs != null && listJobs.size() > 0) {

                        List<ResEmailJob> arr = listJobs.stream().map(
                                job -> this.convertJobToSendEmail(job)).collect(Collectors.toList());
                        this.emailService.sendEmailFromTemplateSync(
                                sub.getEmail(),
                                "Cơ hội việc làm hot đang chờ đón bạn, khám phá ngay",
                                "job",
                                sub.getName(),
                                arr);
                    }
                }
            }
        }
    }

    public void sendEmailToSpecificSubscriber(long subscriberId) {
        Subscriber subscriber = this.findById(subscriberId);
        if (subscriber == null) {
            throw new RuntimeException("Subscriber không tồn tại với ID: " + subscriberId);
        }

        List<Skill> listSkills = subscriber.getSkills();
        if (listSkills != null && listSkills.size() > 0) {
            List<Job> listJobs = this.jobRepository.findBySkillsIn(listSkills);
            if (listJobs != null && listJobs.size() > 0) {
                List<ResEmailJob> arr = listJobs.stream().map(
                        job -> this.convertJobToSendEmail(job)).collect(Collectors.toList());
                this.emailService.sendEmailFromTemplateSync(
                        subscriber.getEmail(),
                        "Cơ hội việc làm hot đang chờ đón bạn, khám phá ngay",
                        "job",
                        subscriber.getName(),
                        arr);
            }
        }
    }

    public void sendEmailToSubscriberByEmail(String email) {
        Subscriber subscriber = this.findByEmail(email);
        if (subscriber == null) {
            throw new RuntimeException("Subscriber không tồn tại với email: " + email);
        }

        List<Skill> listSkills = subscriber.getSkills();
        if (listSkills != null && listSkills.size() > 0) {
            List<Job> listJobs = this.jobRepository.findBySkillsIn(listSkills);
            if (listJobs != null && listJobs.size() > 0) {
                List<ResEmailJob> arr = listJobs.stream().map(
                        job -> this.convertJobToSendEmail(job)).collect(Collectors.toList());
                this.emailService.sendEmailFromTemplateSync(
                        subscriber.getEmail(),
                        "Cơ hội việc làm hot đang chờ đón bạn, khám phá ngay",
                        "job",
                        subscriber.getName(),
                        arr);
            }
        }
    }
}
