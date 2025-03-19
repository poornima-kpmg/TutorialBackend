package com.example.TutorialBackend.repository;

import com.example.TutorialBackend.model.TutorialDetails;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface TutorialDetailsRepository extends JpaRepository<TutorialDetails, Long> {

    @Transactional
    void deleteById(long id);

    @Transactional
    void deleteByTutorialId(long tutorialId);
}
