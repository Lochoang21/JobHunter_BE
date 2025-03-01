package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Subscriber;
import vn.hoidanit.jobhunter.service.SubscriberService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1")
public class SubscriberController {
    private final SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @PostMapping("/subscribers")
    @ApiMessage("Create a Subscriber")
    public ResponseEntity<Subscriber> createSubcriber(@Valid @RequestBody Subscriber subscriber)
            throws IdInvalidException {
        boolean isExistEmail = this.subscriberService.isExistEmail(subscriber.getEmail());
        if (isExistEmail == true) {
            throw new IdInvalidException("Email: " + subscriber.getEmail() + " đã tồn tại");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.subscriberService.createSubcriber(subscriber));
    }

    @PutMapping("/subscribers")
    @ApiMessage("Update a Subscriber")
    public ResponseEntity<Subscriber> updateSubscriber(@Valid @RequestBody Subscriber subscriber)
            throws IdInvalidException {
        Subscriber supscriberOptional = this.subscriberService.findById(subscriber.getId());
        if (supscriberOptional == null) {
            throw new IdInvalidException("Subscriber với Id: " + subscriber.getId() + " không tồn tại");
        }
        return ResponseEntity.ok()
                .body(this.subscriberService.updateSubscriber(supscriberOptional, subscriber));
    }

}
