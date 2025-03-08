package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import vn.hoidanit.jobhunter.service.EmailService;
import vn.hoidanit.jobhunter.service.SubscriberService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("api/v1")
public class EmailController {

    private final EmailService emailService;
    private final SubscriberService subscriberService;

    public EmailController(EmailService emailService, SubscriberService subscriberService){
        this.emailService = emailService;
        this.subscriberService= subscriberService;
    }

    @GetMapping("/email")
    @ApiMessage("Send simple Email")
    // @Scheduled(cron =  "*/30 * * * * *")
    // @Transactional
    public String getMethodName() {
        this.subscriberService.sendSubscribersEmailJobs();
        return "send oke";
    }
    
     
}