package com.artog.flashlearn.repo;




import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.artog.flashlearn.model.StudySession;

@Repository
public interface StudySessionRepository extends JpaRepository<StudySession, Long> {
	// Spring Data will auto-implement save(), saveAll(), findById(), etc.
    Page<StudySession> findByUser_Id(Long userId, Pageable pageable);
    List<StudySession> findByUserId(Long userId);
    
}

/*
 * Old way of doing it. 
 * public class StudySessionRepository {
 */

	/*Data Access Layer logic */
/*	public StudySession save(StudySession session) {
		return session;
		
	}
}*/
