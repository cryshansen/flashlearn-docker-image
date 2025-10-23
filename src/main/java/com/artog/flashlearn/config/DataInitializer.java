package com.artog.flashlearn.config;

import com.artog.flashlearn.model.Flashcard;
import com.artog.flashlearn.repo.FlashcardRepository;
import com.artog.flashlearn.service.FlashcardImportService;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {
	@Value("${app.data.init:false}")
	private boolean initData;

	/*@Autowired
    private FlashcardRepository flashcardRepository;

    @Override*/
   /* public void run(String... args) throws Exception {
        flashcardRepository.saveAll(List.of(
            new Flashcard("What is polymorphism?", "The ability of an object to take many forms."),
            new Flashcard("What is inheritance?", "A mechanism where a new class is derived from an existing class.")
        ));
    }*/
	
    @Bean
    public CommandLineRunner loadFlashcards(FlashcardImportService importer) {
    	
        return args -> {
        	 if (!initData) {
                 System.out.println("⚙️ Skipping data initialization (app.data.init=false)");
                 return;
             }

             System.out.println("📘 Loading flashcards from JSON...");
            importer.importAllJsonFiles("./data", 1L); // 1L = user id
            System.out.println("Flashcards Import complete.");
        };
    }
}
