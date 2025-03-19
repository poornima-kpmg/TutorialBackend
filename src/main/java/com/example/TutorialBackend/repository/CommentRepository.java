package com.example.TutorialBackend.repository;

import com.example.TutorialBackend.model.Comment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByTutorialId(long commentId);

    @Transactional
    void deleteByTutorialId(long tutorialId);
}
