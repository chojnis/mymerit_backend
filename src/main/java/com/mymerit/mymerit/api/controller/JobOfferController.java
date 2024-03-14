package com.mymerit.mymerit.api.controller;

import com.mymerit.mymerit.api.payload.response.JobOfferDetailsResponse;
import com.mymerit.mymerit.api.payload.response.JobOfferListResponse;
import com.mymerit.mymerit.domain.entity.JobOffer;
import com.mymerit.mymerit.domain.service.JobOfferService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Range;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
@Controller
public class JobOfferController {

    JobOfferService jobOfferService;

    JobOfferController(JobOfferService jobOfferService){
        this.jobOfferService = jobOfferService;
    }

    @GetMapping("/job/{id}")
    ResponseEntity<JobOfferDetailsResponse> getJobOfferById(@PathVariable String id){
        return jobOfferService.getJobOfferDetailsResponse(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/job")
    ResponseEntity<JobOffer> addJobOffer(@RequestBody JobOffer jobOffer){
        return ResponseEntity.ok(jobOfferService.addJobOffer(jobOffer));
    }
    @GetMapping("/jobs")
    ResponseEntity<Page<JobOfferListResponse>> jobOffers(
            @RequestParam(defaultValue = "*") List<String> technologies,
            @RequestParam(defaultValue = "0") int page ,
            @RequestParam(defaultValue = "0") Integer rangeSalaryMin,
            @RequestParam(defaultValue = "99999")Integer rangeSalaryMax,
            @RequestParam(defaultValue = "0")Integer rangeCreditsMin,
            @RequestParam(defaultValue = "99999")Integer rangeCreditsMax){

        System.out.println(technologies);


        Range<Integer> salaryRange = Range.of(Range.Bound.inclusive(rangeSalaryMin), Range.Bound.inclusive(rangeSalaryMax));
        Range<Integer> creditsRange = Range.of(Range.Bound.inclusive(rangeCreditsMin), Range.Bound.inclusive(rangeCreditsMax));
        PageRequest pageRequest = PageRequest.of(page, 10);

        Page<JobOfferListResponse> jobOffersPage = jobOfferService.getJobOffers(technologies,salaryRange,creditsRange, pageRequest);

        return ResponseEntity.ok(jobOffersPage);
    }





}


