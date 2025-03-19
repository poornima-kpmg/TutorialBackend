package com.example.TutorialBackend.controller;

import com.example.TutorialBackend.exception.ResourceNotFoundException;
import com.example.TutorialBackend.model.Tag;
import com.example.TutorialBackend.model.Tutorial;
import com.example.TutorialBackend.repository.TagRepository;
import com.example.TutorialBackend.repository.TutorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class TagController {

    @Autowired
    private TutorialRepository tutorialRepository;

    @Autowired
    private TagRepository tagRepository;

    @PostMapping("/tutorials/{tutorialId}/tags")
    public ResponseEntity<Tag> addTag(@PathVariable("tutorialId") long tutorialId,
                                      @RequestBody Tag tagRequest){
        Tag tag = tutorialRepository.findById(tutorialId)
                .map(tutorial -> {
                    tutorial.addTag(tagRequest);
                    return tagRepository.save(tagRequest);
                }).orElseThrow(() -> new
                        ResourceNotFoundException("No Tutorial found with id : " + tutorialId));

        return new ResponseEntity<>(tag, HttpStatus.CREATED);
    }

    @GetMapping("/tutorials/{tutorialId}/tags")
    public ResponseEntity<List<Tag>> getAllTagsByTutorialId
            (@PathVariable(value ="tutorialId") long tutorialId) throws ResourceNotFoundException {
        if(!tutorialRepository.existsById(tutorialId)){
            throw new ResourceNotFoundException("No tutorial found with id : " + tutorialId);
        }
        List<Tag> tags=tagRepository.findTagsByTutorialsId(tutorialId);
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @GetMapping("/tags/{id}")
    public ResponseEntity<Tag> getTagsById(@PathVariable(value="id") long id) throws ResourceNotFoundException{
        Tag tag = tagRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("No Tutorial found with id: " + id));

        return  new ResponseEntity<>(tag, HttpStatus.OK);

    }


    @GetMapping("tags/{tagId}/tutorials")
    public ResponseEntity<List<Tutorial>> getAllTutorialsByTagId(
            @PathVariable(value = "tagId") long tagId) throws ResourceNotFoundException {
        if(!tutorialRepository.existsById(tagId)){
            throw new ResourceNotFoundException("No tutorial found with id: " + tagId);
        }
        List<Tutorial> tutorials= tutorialRepository.findTutorialByTagsId(tagId);
        return new ResponseEntity<>(tutorials, HttpStatus.OK);
    }

    @PutMapping("/tags/{id}")
    public ResponseEntity<Tag> updateTagsById(@PathVariable(value="id") long id,
                                              @RequestBody Tag tagRequest)
            throws ResourceNotFoundException{
        Tag tag = tagRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Tag with id:" + id +"not found"));

        tag.setName(tagRequest.getName());
        return new ResponseEntity<>(tagRepository.save(tag), HttpStatus.OK);
    }

    @DeleteMapping("/tutorials/{tutorialId}/tags/{tagsId}")
    public ResponseEntity<HttpStatus> deleteTagFromTutorial(@PathVariable(value="tutorialId")long tutorialId,
                                                            @PathVariable(value = "tagsId") long tagsId)
            throws ResourceNotFoundException{
        Tutorial tutorial = tutorialRepository.findById(tutorialId)
                .orElseThrow(()->new ResourceNotFoundException("Tutorial with id "+ tutorialId+"not found!"));

        tutorial.removeTag(tagsId);
        tutorialRepository.save(tutorial);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/tags/{tagsId}")
    public ResponseEntity<HttpStatus> deleteTag(@PathVariable(value="tagsId") long tagsId){
        tagRepository.deleteById(tagsId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
