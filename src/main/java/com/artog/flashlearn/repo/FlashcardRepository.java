package com.artog.flashlearn.repo;

import java.util.List;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



import com.artog.flashlearn.model.Flashcard;

@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard, Long> {

	
    // Spring Data will auto-implement save(), saveAll(), findById(), etc.
	
	 List<Flashcard> findBySession_Id(Long sessionId);
	// List<Flashcard> findByUser_Id(Long sessionId);
	 // find all flashcards where the session.user.id = :userId
	 List<Flashcard> findBySession_User_Id(Long userId);
	
	 @Query("SELECT f FROM Flashcard f " +
	           "WHERE f.session.user.id = :userId " +
	           "AND (LOWER(f.question) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
	           "OR LOWER(f.answer) LIKE LOWER(CONCAT('%', :keyword, '%')))")
	    Page<Flashcard> searchByUserIdAndKeyword(
	            @Param("userId") Long userId,
	            @Param("keyword") String keyword,
	            Pageable pageable
	    );

}
