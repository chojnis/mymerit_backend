package com.mymerit.mymerit.api.controller;

import com.mymerit.mymerit.api.payload.request.JobOfferRequest;
import com.mymerit.mymerit.api.payload.response.*;
import com.mymerit.mymerit.domain.entity.Feedback;
import com.mymerit.mymerit.domain.entity.JobOffer;
import com.mymerit.mymerit.domain.entity.Solution;
import com.mymerit.mymerit.domain.models.ProgrammingLanguage;
import com.mymerit.mymerit.domain.service.JobOfferService;
import com.mymerit.mymerit.domain.service.UserDetailsImpl;
import com.mymerit.mymerit.infrastructure.security.CurrentUser;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Controller
public class JobOfferController {
    private final JobOfferService jobOfferService;

    JobOfferController(JobOfferService jobOfferService) {
        this.jobOfferService = jobOfferService;
   }

    @GetMapping("/job/{id}")
    ResponseEntity<JobOfferDetailsResponse> getJobOfferById(@PathVariable String id, @CurrentUser UserDetailsImpl user) {
        return ResponseEntity.ok(jobOfferService.getJobOfferDetailsResponse(id, user));

    }

    @PostMapping("/job")
    ResponseEntity<ApiResponse> addJobOffer(@RequestBody @Valid JobOfferRequest jobOfferRequest, @CurrentUser UserDetailsImpl user) {
        try {
            JobOffer added = jobOfferService.addJobOffer(jobOfferRequest, user);
            return ResponseEntity.ok(new ApiResponse(true, "Job offer added successfully", added));
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse(false, e.getMessage()));
        }
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
    ) {
        Page<JobOfferListResponse> jobOffersPage = jobOfferService.getJobOffers(
                q, languages, page, minCredits, maxCredits, minSalary, maxSalary, minOpensIn, maxOpensIn, sort
        );

        return ResponseEntity.ok(jobOffersPage);
    }
    @PostMapping("/job/{jobOfferId}/solution")
    ResponseEntity<JobOffer> addSolution(@PathVariable String jobOfferId, @RequestParam List<MultipartFile> files, @AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam ProgrammingLanguage language) throws IOException {
        return Optional.ofNullable(jobOfferService.addSolution(jobOfferId, files, userDetails.getId(),language))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job offer not found for id: " + jobOfferId));
    }

    @PostMapping("/solution/{solutionId}")
    ResponseEntity<Feedback> addFeedback(@PathVariable String solutionId, @RequestParam List<MultipartFile> files, @RequestParam Integer reward, @RequestParam String comment, @CurrentUser UserDetailsImpl userDetails) {
        return Optional.ofNullable(jobOfferService.addFeedback(solutionId, files, reward, comment, userDetails.getId()))
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Job offer not found for id: " + solutionId));
    }

    @GetMapping("/job/{jobOfferId}/solution")
    public ResponseEntity<List<GridFileResponse>> downloadSolutionFilesForUserAndJobOffer(@PathVariable String jobOfferId, @CurrentUser UserDetailsImpl userDetails) {
        List<GridFileResponse> downloadedFiles = jobOfferService.downloadSolutionFilesForUser(jobOfferId, userDetails.getId());
        return ResponseEntity.ok(downloadedFiles);
    }


    @GetMapping("/solution/{solutionId}")
    public ResponseEntity<SolutionDetailsResponse> downloadSolution(@PathVariable String solutionId) {
        return ResponseEntity.ok(jobOfferService.downloadSolutionFiles(solutionId));
    }

    @GetMapping("/solution/{solutionId}/feedback")
    public ResponseEntity<List<GridFileResponse>> downloadFeedback(@PathVariable String solutionId) {
        List<GridFileResponse> downloadedFiles = jobOfferService.downloadFeedbackForSolution(solutionId);
        return ResponseEntity.ok(downloadedFiles);
    }
}
