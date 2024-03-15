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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Set;

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
            @RequestParam(defaultValue = "*") Set<String> languages,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "0") Integer minSalary,
            @RequestParam(defaultValue = "99999") Integer maxSalary,
            @RequestParam(defaultValue = "0") Integer minCredits,
            @RequestParam(defaultValue = "99999") Integer maxCredits,
            @SortDefault(sort = "opensAt", direction = Sort.Direction.DESC) Sort sort){

        Range<Integer> salaryRange = Range.of(Range.Bound.inclusive(minSalary), Range.Bound.inclusive(maxSalary));
        Range<Integer> creditsRange = Range.of(Range.Bound.inclusive(minCredits), Range.Bound.inclusive(maxCredits));
        PageRequest pageRequest = PageRequest.of(page, 4, sort);

        Page<JobOfferListResponse> jobOffersPage = jobOfferService.getJobOffers(languages,salaryRange,creditsRange, pageRequest, sort);

        return ResponseEntity.ok(jobOffersPage);
    }


    @PostMapping("/job/solution/{jobOfferId}")
    ResponseEntity<JobOffer> addSolution(@PathVariable String jobOfferId, @RequestBody SolutionRequest solutionRequest,@AuthenticationPrincipal UserDetailsImpl userDetails){
        return ResponseEntity.ok(jobOfferService.addSolution(jobOfferId,solutionRequest,userDetails.getId()));
    }


}
