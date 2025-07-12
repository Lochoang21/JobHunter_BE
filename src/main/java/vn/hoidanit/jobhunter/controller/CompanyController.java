package vn.hoidanit.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.response.ResultPaginationDTO;
import vn.hoidanit.jobhunter.service.CompanyService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;
import vn.hoidanit.jobhunter.util.error.IdInvalidException;

@Controller
@RequestMapping("/api/v1")
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService){
        this.companyService = companyService;
    }

    @GetMapping("/companies")
    @ApiMessage("fetch all companies")
    public ResponseEntity<ResultPaginationDTO> getAllCompany(
       @Filter Specification<Company> spec, Pageable pageable
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(this.companyService.handleGetAllCompany(spec, pageable));
    }

    @GetMapping("/companies/{id}")  
    public ResponseEntity<Company> getCompanyById(@PathVariable("id") long id) throws IdInvalidException {
        Company fetchCompany = this.companyService.handleGetCompanyById(id);
        if (fetchCompany == null) {
            throw new IdInvalidException("Company với Id: " + id + " không tồn tại!");
        }
        return ResponseEntity.ok(fetchCompany);
    }

    @PostMapping("/companies")
    public ResponseEntity<Company> createNewCompany(@Valid @RequestBody Company postmanCompany) throws IdInvalidException {
        Company newCompany = this.companyService.handleCreateCompany(postmanCompany);
        return ResponseEntity.status(HttpStatus.CREATED).body(newCompany);
    }

    @PutMapping("/companies")
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company company) throws IdInvalidException {
        Company companyUpdate = this.companyService.handleUpdatecompany(company);
        if (companyUpdate == null) {
            throw new IdInvalidException("Company với id: " + company.getId() + " không tồn tại!");
        }
        return ResponseEntity.ok(companyUpdate);
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable("id") long id) throws IdInvalidException {
        this.companyService.handleDeleteCompany(id);
        return ResponseEntity.ok(null);
    }
}
