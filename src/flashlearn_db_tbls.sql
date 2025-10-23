CREATE TABLE users (
    id BIGINT PRIMARY KEY,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    subscriptionTier VARCHAR(50) NOT NULL DEFAULT 'FREE'
);


CREATE TABLE study_sessions (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    topic VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    user_id_fk BIGINT NOT NULL,
    FOREIGN KEY (user_id_fk) REFERENCES users(id)
);

CREATE TABLE flashcards (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    question TEXT NOT NULL,
    answer TEXT NOT NULL,
    difficulty VARCHAR(50) NOT NULL DEFAULT 'EASY',  -- (EASY, MEDIUM, HARD)
    color VARCHAR(50) NOT NULL DEFAULT 'success',   -- (success, warning, danger)
    study_session_id_fk BIGINT NOT NULL,
    FOREIGN KEY (study_session_id_fk) REFERENCES study_sessions(id)
);

-- New table for memory cues (separate from flashcards to allow multiple cues per card)
CREATE TABLE memory_cues (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    flashcard_id_fk BIGINT NOT NULL,
    cue_type VARCHAR(50) NOT NULL,                 -- ('USER', 'AI')
    cue_text TEXT NOT NULL,                        -- plain text or JSON/HTML snippet
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (flashcard_id_fk) REFERENCES flashcards(id)
);