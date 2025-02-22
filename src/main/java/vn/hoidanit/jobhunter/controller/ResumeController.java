package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResCeateResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResFetchResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import vn.hoidanit.jobhunter.service.ResumeService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;

import java.util.Optional;

import org.springframework.boot.autoconfigure.rsocket.RSocketProperties.Server.Spec;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1")
public class ResumeController {
    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/resumes")
    @ApiMessage("Create a new resume")
    public ResponseEntity<ResCeateResumeDTO> createResume(@Valid @RequestBody Resume resume) throws IdInvalidException {
        {
            // check id
            boolean isNameExist = this.resumeService.checkResumeExitByUserAndJob(resume);
            if (!isNameExist) {
                throw new IdInvalidException("User va Job khong ton tai");
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(this.resumeService.handleCreateResume(resume));

        }
    }

    @PutMapping("/resumes")
    @ApiMessage("Update a resume")
    public ResponseEntity<ResUpdateResumeDTO> updateResume(@RequestBody Resume resume) throws IdInvalidException {
        Optional<Resume> resumeOptional = this.resumeService.fetchResumeById(resume.getId());
        if (resumeOptional.isEmpty()) {
            throw new IdInvalidException("Resume với id: " + resume.getId() + " không tồn tại!");
        }
        Resume currentResume = resumeOptional.get();

        currentResume.setStatus(resume.getStatus());
        return ResponseEntity.ok().body(this.resumeService.handleUpdateResume(currentResume));

    }

    @GetMapping("/resumes")
    @ApiMessage("Fetch all resumes")
    public ResponseEntity<ResultPaginationDTO> fetchAllResumes(@Filter Specification<Resume> spec, Pageable pageable) {

        return ResponseEntity.ok().body(this.resumeService.fetchAllResume(spec, pageable));
    }

    @GetMapping("/resumes/{id}")
    @ApiMessage("Get a resume by id")
    public ResponseEntity<ResFetchResumeDTO> getResumeById(@PathVariable("id") long id) throws IdInvalidException {
        Optional<Resume> currentResume = this.resumeService.fetchResumeById(id);
        if (!currentResume.isPresent()) {
            throw new IdInvalidException("Resume voi id: " + id + " khong ton tai!");
        }

        return ResponseEntity.ok().body(this.resumeService.getResumeById(currentResume.get()) );
    }

    @DeleteMapping("/resumes/{id}")
    @ApiMessage("Delete a resume by id")
    public ResponseEntity<Void> deleteResume(@PathVariable("id") long id) throws IdInvalidException {
        Optional<Resume> currentResume = this.resumeService.fetchResumeById(id);
        if (!currentResume.isPresent()) {
            throw new IdInvalidException("Resume voi id: " + id + " khong ton tai!");
        }

        this.resumeService.deleteResume(id);
        return ResponseEntity.ok().body(null);
    }

    @PostMapping("/resumes/by-user")
    @ApiMessage("Get list resume by user")
    public ResponseEntity<ResultPaginationDTO> fetchResumeByUser(Pageable pageable) {
        //TODO: process POST request
        
        return ResponseEntity.ok().body(this.resumeService.fetchResumeByUser(pageable));
    }
    
}
