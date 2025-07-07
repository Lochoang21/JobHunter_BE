package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.transaction.Transactional;
import vn.hoidanit.jobhunter.domain.response.RestResponse;
import vn.hoidanit.jobhunter.service.EmailService;
import vn.hoidanit.jobhunter.service.SubscriberService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;

import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("api/v1")
public class EmailController {

    private final EmailService emailService;
    private final SubscriberService subscriberService;

    public EmailController(EmailService emailService, SubscriberService subscriberService) {
        this.emailService = emailService;
        this.subscriberService = subscriberService;
    }

    @GetMapping("/email")
    @ApiMessage("Send simple Email")
    public RestResponse<String> getMethodName() {
        this.subscriberService.sendSubscribersEmailJobs();

        RestResponse<String> response = new RestResponse<>();
        response.setStatusCode(200);
        response.setMessage("Send simple Email");
        response.setData("send oke");

        return response;
    }

    @GetMapping("/email/subscriber/{subscriberId}")
    @ApiMessage("Send Email to Specific Subscriber")
    public RestResponse<String> sendEmailToSpecificSubscriber(@PathVariable("subscriberId") long subscriberId) {
        try {
            this.subscriberService.sendEmailToSpecificSubscriber(subscriberId);

            RestResponse<String> response = new RestResponse<>();
            response.setStatusCode(200);
            response.setMessage("Email đã được gửi thành công cho subscriber ID: " + subscriberId);
            response.setData("send oke");

            return response;
        } catch (RuntimeException e) {
            RestResponse<String> response = new RestResponse<>();
            response.setStatusCode(400);
            response.setMessage("Lỗi: " + e.getMessage());
            response.setData(null);

            return response;
        }
    }

    @GetMapping("/email/subscriber/email/{email}")
    @ApiMessage("Send Email to Subscriber by Email")
    public RestResponse<String> sendEmailToSubscriberByEmail(@PathVariable("email") String email) {
        try {
            this.subscriberService.sendEmailToSubscriberByEmail(email);

            RestResponse<String> response = new RestResponse<>();
            response.setStatusCode(200);
            response.setMessage("Email đã được gửi thành công cho subscriber: " + email);
            response.setData("send oke");

            return response;
        } catch (RuntimeException e) {
            RestResponse<String> response = new RestResponse<>();
            response.setStatusCode(400);
            response.setMessage("Lỗi: " + e.getMessage());
            response.setData(null);
            return response;
        }
    }

}