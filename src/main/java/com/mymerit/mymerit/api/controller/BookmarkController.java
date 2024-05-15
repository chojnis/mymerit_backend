package com.mymerit.mymerit.api.controller;

import com.mymerit.mymerit.api.payload.response.ApiResponse;
import com.mymerit.mymerit.domain.entity.Bookmark;
import com.mymerit.mymerit.domain.service.BookmarkService;
import com.mymerit.mymerit.domain.service.UserDetailsImpl;
import com.mymerit.mymerit.infrastructure.security.CurrentUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookmark")
public class BookmarkController {
    private final BookmarkService bookmarkService;
    BookmarkController(BookmarkService bookmarkService){
        this.bookmarkService = bookmarkService;
    }

    @PostMapping("/{jobId}")
    public ResponseEntity<ApiResponse> addToBookmarks(@PathVariable String jobId, @CurrentUser UserDetailsImpl userDetails){
        try{
            Bookmark bookmark = bookmarkService.addBookmark(userDetails.getId(), jobId);
            return ResponseEntity.ok(new ApiResponse(true, "Bookmark added successfully", bookmark));
        }catch (Exception e){
            return ResponseEntity.ok(new ApiResponse(false, e.getMessage()));
        }
    }

    @DeleteMapping("/{jobId}")
    public ResponseEntity<ApiResponse> removeFromBookmarks(@PathVariable String jobId, @CurrentUser UserDetailsImpl userDetails){
        try{
            bookmarkService.removeBookmark(userDetails.getId(), jobId);
            return ResponseEntity.ok(new ApiResponse(true, "Bookmark removed successfully"));
        }catch (Exception e){
            return ResponseEntity.ok(new ApiResponse(false, e.getMessage()));
        }
    }

    @GetMapping("")
    public List<Bookmark> getBookmarksForUser(@CurrentUser UserDetailsImpl userDetails){
        return bookmarkService.getBookmarksForUser(userDetails.getId());
    }

}