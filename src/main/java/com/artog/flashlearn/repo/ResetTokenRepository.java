package com.artog.flashlearn.repo;


import com.artog.flashlearn.model.ResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ResetTokenRepository extends JpaRepository<ResetToken, Long> {
    Optional<ResetToken> findByToken(String token);

	Object findByToken(ResetToken token);

	Object save(Class<ResetToken> class1);
}