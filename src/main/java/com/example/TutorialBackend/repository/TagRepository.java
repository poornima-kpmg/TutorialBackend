package com.example.TutorialBackend.repository;

import com.example.TutorialBackend.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    List<Tag> findTagsByTutorialsId(long tutorialId);
}
