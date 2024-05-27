package com.mymerit.mymerit.api.controller;

import com.mymerit.mymerit.api.payload.response.ApiResponse;
import com.mymerit.mymerit.domain.entity.Bookmark;
import com.mymerit.mymerit.domain.service.BookmarkService;
import com.mymerit.mymerit.domain.service.UserDetailsImpl;
import com.mymerit.mymerit.infrastructure.security.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookmark")
@Tag(name = "BookmarkController")
public class BookmarkController {
    private final BookmarkService bookmarkService;
    BookmarkController(BookmarkService bookmarkService){
        this.bookmarkService = bookmarkService;
    }

    @Operation(
            summary = "Adds job offer to bookmarks")
    @PostMapping("/{jobId}")
    public ResponseEntity<ApiResponse> addToBookmarks(@PathVariable String jobId, @CurrentUser UserDetailsImpl userDetails){
        try{
            Bookmark bookmark = bookmarkService.addBookmark(userDetails.getId(), jobId);
            return ResponseEntity.ok(new ApiResponse(true, "Bookmark added successfully", bookmark));
        }catch (Exception e){
            return ResponseEntity.ok(new ApiResponse(false, e.getMessage()));
        }
    }

    @Operation(
            summary = "Removes job offer from bookmarks")
    @DeleteMapping("/{jobId}")
    public ResponseEntity<ApiResponse> removeFromBookmarks(@PathVariable String jobId, @CurrentUser UserDetailsImpl userDetails){
        try{
            bookmarkService.removeBookmark(userDetails.getId(), jobId);
            return ResponseEntity.ok(new ApiResponse(true, "Bookmark removed successfully"));
        }catch (Exception e){
            return ResponseEntity.ok(new ApiResponse(false, e.getMessage()));
        }
    }

    @Operation(
            summary = "Returns bookmarks")
    @GetMapping("")
    public List<Bookmark> getBookmarksForUser(@CurrentUser UserDetailsImpl userDetails){
        return bookmarkService.getBookmarksForUser(userDetails.getId());
    }

}