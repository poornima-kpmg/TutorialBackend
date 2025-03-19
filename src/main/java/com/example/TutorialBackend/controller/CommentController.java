package com.example.TutorialBackend.controller;

import com.example.TutorialBackend.exception.ResourceNotFoundException;
import com.example.TutorialBackend.model.Comment;
import com.example.TutorialBackend.model.Tutorial;
import com.example.TutorialBackend.model.TutorialDetails;
import com.example.TutorialBackend.repository.CommentRepository;
import com.example.TutorialBackend.repository.TutorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.*;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TutorialRepository tutorialRepository;

    @PostMapping("/tutorials/{tutorialId}/comment")
    public ResponseEntity<Comment> createContent(
            @PathVariable(value = "tutorialId") long tutorialId,
            @RequestBody Comment commentRequest) throws ResourceNotFoundException {

        Comment comment = tutorialRepository.findById(tutorialId)
                .map(tutorial -> {
                    commentRequest.setTutorial(tutorial);
                    return commentRepository.save(commentRequest);
                }).orElseThrow(() ->
                        new ResourceNotFoundException("No Tutorial Found with id : " + tutorialId));

        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    @GetMapping("/tutorials/{tutorialId}/comment")
    public ResponseEntity<List<Comment>> getAllCommentsByTutorialId(
            @PathVariable(value = "tutorialId") long tutorialId) throws ResourceNotFoundException {
        if(!tutorialRepository.existsById(tutorialId)){
            throw new ResourceNotFoundException("No Tutorial Found with id : " + tutorialId);
        }
        List<Comment> comments = commentRepository.findByTutorialId(tutorialId);
        return new ResponseEntity<>(comments, HttpStatus.CREATED);
    }


    @GetMapping("comments/{id}")
    public ResponseEntity<Comment> getCommentsByTutorialId(
            @PathVariable(value = "id") long id) throws ResourceNotFoundException {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("No Tutorial Found with id : " + id));
        return new ResponseEntity<>(comment, HttpStatus.OK);
    }

    @PutMapping("comments/{id}")
    public ResponseEntity<Comment> updateComment(
            @PathVariable(value = "id") long id,
            @RequestBody Comment commentRequest) throws ResourceNotFoundException {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No Comment Found with id : " + id));

        comment.setContent(commentRequest.getContent());
        return new ResponseEntity<>(commentRepository.save(comment), HttpStatus.OK);
    }


    @DeleteMapping("/comments/{id}")
    public ResponseEntity<HttpStatus> deleteComment(@PathVariable("id") long id){
        commentRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/tutorials/{TutorialId}/comments")
    public ResponseEntity<List<Comment>> deleteAllCommentOfTutorial(
            @PathVariable("tutorialId") long tutorialId) throws ResourceNotFoundException {
    if(!tutorialRepository.existsById(tutorialId)){
        throw new ResourceNotFoundException("No Tutorial Found with id : " + tutorialId);
    }
    commentRepository.deleteByTutorialId(tutorialId);
    return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}