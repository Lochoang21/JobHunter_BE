package vn.hoidanit.jobhunter.controller;

import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResCeateResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResFetchResumeDTO;
import vn.hoidanit.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import vn.hoidanit.jobhunter.service.ResumeService;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;
import com.turkraft.springfilter.boot.Filter;
import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecification;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
    private final UserService userService;

    @Autowired
    FilterBuilder filterBuilder;

        @Autowired
    private FilterSpecificationConverter filterSpecificationConverter;

    public ResumeController(ResumeService resumeService, UserService userService) {
        this.resumeService = resumeService;
        this.userService = userService;
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
    @ApiMessage("Fetch all resumes with Pagination")
    public ResponseEntity<ResultPaginationDTO> fetchAllResumes(@Filter Specification<Resume> spec, Pageable pageable) {
        List<Long> arrJobIds = null;
        String email = SecurityUtil.getCurrentUserLogin().isPresent() == true ?
        SecurityUtil.getCurrentUserLogin().get() : "";

        User currrentUser = this.userService.handleGetUserByUsername(email);
        if (currrentUser != null) {
            Company userCompany = currrentUser.getCompany();

            if (userCompany != null) {
                List<Job> companyJobs = userCompany.getJobs();
                if (companyJobs != null && companyJobs.size() > 0) {
                    arrJobIds = companyJobs.stream().map(x -> x.getId()).collect(Collectors.toList());
                }
            }
        }
        Specification<Resume> jobsInSpec = filterSpecificationConverter.convert(filterBuilder.field("job").in(filterBuilder.input(arrJobIds)).get());
       
       Specification<Resume> finalSpec = jobsInSpec.and(spec);
        return ResponseEntity.ok().body(this.resumeService.fetchAllResume(finalSpec, pageable));
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

    @GetMapping("/resumes/all")
@ApiMessage("Fetch all resumes (Admin only)")
public ResponseEntity<ResultPaginationDTO> fetchAllResumesAdmin(@Filter Specification<Resume> spec, Pageable pageable) {
    // Bỏ qua filter theo company, trả về tất cả
    return ResponseEntity.ok().body(this.resumeService.fetchAllResume(spec, pageable));
}
    
}
