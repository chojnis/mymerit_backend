package com.mymerit.mymerit.api.controller;

import com.mymerit.mymerit.api.payload.request.SolutionRequest;
import com.mymerit.mymerit.api.payload.response.JobOfferDetailsResponse;
import com.mymerit.mymerit.api.payload.response.JobOfferListResponse;
import com.mymerit.mymerit.domain.entity.JobOffer;
import com.mymerit.mymerit.domain.entity.Solution;
import com.mymerit.mymerit.domain.service.JobOfferService;
import com.mymerit.mymerit.domain.service.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Range;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.*;

@Controller
public class JobOfferController {
    JobOfferService jobOfferService;

    JobOfferController(JobOfferService jobOfferService) {
        this.jobOfferService = jobOfferService;
    }

    @GetMapping("/job/{id}")
    ResponseEntity<JobOfferDetailsResponse> getJobOfferById(@PathVariable String id){
        return jobOfferService.getJobOfferDetailsResponse(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/job")
    ResponseEntity<JobOffer> addJobOffer(@RequestBody @Valid JobOffer jobOffer){
        return ResponseEntity.ok(jobOfferService.addJobOffer(jobOffer));
    }

    @GetMapping("/jobs")
    ResponseEntity<Page<JobOfferListResponse>> jobOffers(
            @RequestParam(defaultValue = "") Set<String> languages,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "") String q,
            @RequestParam(defaultValue = "0") Integer minSalary,
            @RequestParam(defaultValue = "40000") Integer maxSalary,
            @RequestParam(defaultValue = "0") Integer minCredits,
            @RequestParam(defaultValue = "10000") Integer maxCredits,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().toString()}") @DateTimeFormat(pattern = "yyyy-MM-dd") Date minOpensIn,
            @RequestParam(defaultValue = "#{T(java.time.LocalDate).now().plusYears(1).toString()}") @DateTimeFormat(pattern = "yyyy-MM-dd") Date maxOpensIn,
            @SortDefault(sort = "taskOpensAt", direction = Sort.Direction.ASC) Sort sort
    ){

        Page<JobOfferListResponse> jobOffersPage = jobOfferService.getJobOffers(
                q, languages, page, minCredits, maxCredits, minSalary, maxSalary, minOpensIn, maxOpensIn, sort
        );

        return ResponseEntity.ok(jobOffersPage);
    }


    @PostMapping("/job/solution/{jobOfferId}")
    ResponseEntity<JobOffer> addSolution(@PathVariable String jobOfferId, @RequestParam List<MultipartFile> files, @AuthenticationPrincipal UserDetailsImpl userDetails) throws IOException {
        return Optional.ofNullable(jobOfferService.addSolution(jobOfferId, files, userDetails.getId()))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job offer not found for id: " + jobOfferId));
        //gdy updateujemy solution to usunac poprzednie pliki w bazie danych, lub jakos je zastapic
    }

}
