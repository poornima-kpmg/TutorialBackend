package com.example.TutorialBackend.controller;
import com.example.TutorialBackend.exception.ResourceNotFoundException;
import com.example.TutorialBackend.model.Tutorial;
import com.example.TutorialBackend.model.TutorialDetails;
import com.example.TutorialBackend.repository.TutorialDetailsRepository;
import com.example.TutorialBackend.repository.TutorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api")
public class TutorialDetailsController {

    @Autowired
    private TutorialDetailsRepository tutorialDetailsRepository;

    @Autowired
    private TutorialRepository tutorialRepository;

    @PostMapping("/tutorials/{tutorialId}/details")
    public ResponseEntity<TutorialDetails>
    createDetails(@PathVariable(value = "tutorialId") long tutorialId,
                  @RequestBody TutorialDetails tutorialDetails)
        throws ResourceNotFoundException{
        Tutorial _tutorial =
                tutorialRepository.findById(tutorialId)
                        .orElseThrow(() -> new
                                ResourceNotFoundException(
                                        "No Tutorial Found With id " + tutorialId
                        ));

        //tutorialDetails.setCreatedOn(new Date());
        tutorialDetails.setCreatedOn(tutorialDetails.getCreatedOn());
        System.out.println("Created On is " + tutorialDetails.getCreatedOn());
        tutorialDetails.setTutorial(_tutorial);
        TutorialDetails details = tutorialDetailsRepository.save(tutorialDetails);
        return new ResponseEntity<>(details, HttpStatus.CREATED);
    }

    @PutMapping("/details/{id}")
    public ResponseEntity<TutorialDetails>
    updateDetails(@PathVariable("id") long id,
                  @RequestBody TutorialDetails tutorialDetails) throws ResourceNotFoundException{

        TutorialDetails details = tutorialDetailsRepository.
                findById(id).orElseThrow(
                        () -> new
                                ResourceNotFoundException("Id " + id +
                                " not found!"));
                        details.setCreatedBy(tutorialDetails.getCreatedBy());

                        return new ResponseEntity<>(
                                tutorialDetailsRepository.save(details),
                                HttpStatus.OK
                        );
    }

    @GetMapping({"/details/{id}", "/tutorials/{id}/details"})
    public ResponseEntity<TutorialDetails> getDetailsById(
            @PathVariable(value = "id") Long id) throws ResourceNotFoundException{
        TutorialDetails details = tutorialDetailsRepository.findById(id)
                .orElseThrow(() -> new
                        ResourceNotFoundException("No Tutorial Detail Found with id: " + id));
        return new ResponseEntity<>(details, HttpStatus.OK);
    }

    @DeleteMapping("/details/{id}")
    public ResponseEntity<HttpStatus> deleteDetails(@PathVariable("id") long id) {
        tutorialDetailsRepository.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @DeleteMapping("/tutorials/{tutorialId}/details")
    public ResponseEntity<TutorialDetails>
    deleteDetailsOfTutorials(@PathVariable("tutorialId") long tutorialId)

        throws ResourceNotFoundException{

        if(!tutorialRepository.existsById(tutorialId)){
            throw new ResourceNotFoundException("No tutorial found with id: " + tutorialId);
        }
        tutorialDetailsRepository.deleteByTutorialId(tutorialId);
        tutorialRepository.deleteById(tutorialId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
