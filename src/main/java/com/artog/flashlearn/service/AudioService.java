package com.artog.flashlearn.service;

import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.artog.flashlearn.model.Flashcard;
import com.artog.flashlearn.repo.FlashcardRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


@Service
public class AudioService {

    private final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
    private static final String DATA_DIR = "./data/";
    private static final String EXPORT_DIR = "./data/export/";

    @Autowired
    private FlashcardRepository flashcardRepo;

    @Autowired
    private OpenAiTtsService ttsClient; // your OpenAI TTS integration
    
    public Flashcard generateAudioIfMissing(Long id) throws Exception {
        Flashcard flashcard = flashcardRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Flashcard not found"));

        if (flashcard.getAnswerAudioPath() == null || flashcard.getAnswerAudioPath().isBlank()) {
            // 1. Call OpenAI TTS
            byte[] audioBytes = ttsClient.synthesize(flashcard.getAnswer().toString());

            // 2. Build file path
            Long userId = flashcard.getSession().getUser().getId();
           
            String filePath = String.format("audio/%d/%d_answer.mp3", userId, flashcard.getId());

            // Ensure directory exists
            java.nio.file.Path path = java.nio.file.Paths.get(filePath).toAbsolutePath();
            java.nio.file.Files.createDirectories(path.getParent());

            // 3. Save file
            java.nio.file.Files.write(path, audioBytes);

            // 4. Save path in DB
            flashcard.setAnswerAudioPath(filePath);
            flashcardRepo.save(flashcard);
        }
        if (flashcard.getQuestionAudioPath() == null || flashcard.getQuestionAudioPath().isBlank()) {
            // 1. Call OpenAI TTS
            byte[] audioBytes = ttsClient.synthesize(flashcard.getQuestion().toString());

            // 2. Build file path
            Long userId = flashcard.getSession().getUser().getId();
            String filePath = String.format("audio/%d/%d_question.mp3", userId, flashcard.getId());
            // Ensure directory exists
            java.nio.file.Path path = java.nio.file.Paths.get(filePath).toAbsolutePath();
            java.nio.file.Files.createDirectories(path.getParent());

            // 3. Save file
            java.nio.file.Files.write(path, audioBytes);

            // 4. Save path in DB
            flashcard.setQuestionAudioPath(filePath);
            flashcardRepo.save(flashcard);
        }

        return flashcard;
    }

    public Flashcard generateQuestionAudioIfMissing(Long id) throws Exception {
        Flashcard flashcard = flashcardRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Flashcard not found"));

        if (flashcard.getQuestionAudioPath() == null || flashcard.getQuestionAudioPath().isBlank()) {
            Long userId = flashcard.getSession().getUser().getId();
            String filePath = String.format("audio/%d/%d_question.mp3", userId, flashcard.getId());

            java.nio.file.Path path = java.nio.file.Paths.get(filePath).toAbsolutePath();
            java.nio.file.Files.createDirectories(path.getParent());

            byte[] audioBytes = ttsClient.synthesize(flashcard.getQuestion().toString());
            java.nio.file.Files.write(path, audioBytes);

            flashcard.setQuestionAudioPath(filePath);
            flashcardRepo.save(flashcard);
        }

        return flashcard;
    }

    public Flashcard generateAnswerAudioIfMissing(Long id) throws Exception {
        Flashcard flashcard = flashcardRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Flashcard not found"));

        if (flashcard.getAnswerAudioPath() == null || flashcard.getAnswerAudioPath().isBlank()) {
            Long userId = flashcard.getSession().getUser().getId();
            String filePath = String.format("audio/%d/%d_answer.mp3", userId, flashcard.getId());

            java.nio.file.Path path = java.nio.file.Paths.get(filePath).toAbsolutePath();
            java.nio.file.Files.createDirectories(path.getParent());

            byte[] audioBytes = ttsClient.synthesize(flashcard.getAnswer().toString());
            java.nio.file.Files.write(path, audioBytes);

            flashcard.setAnswerAudioPath(filePath);
            flashcardRepo.save(flashcard);
        }

        return flashcard;
    }
  
 // ==========================
    // 🎞  SLIDES DATA
    // ==========================
    public Map<String, Object> generateSlidesAudio(String fileName) {
    	
    	//filename is slides-data.json
        try {
            Path path = Paths.get(DATA_DIR + fileName );
            List<Map<String, Object>> slides = mapper.readValue(path.toFile(), List.class);

            int index = 1;
            for (Map<String, Object> slide : slides) {
                String caption = (String) slide.get("caption");
                if (caption != null && !slide.containsKey("audio")) {
                    String audioPath = ttsClient.synthesizeToFile(caption, "slide_" + index);
                    slide.put("audio", audioPath);
                }
                index++;
            }

            Path out = Paths.get(EXPORT_DIR + fileName.replace(".json", "-with-audio.json"));
            Files.createDirectories(out.getParent());
            mapper.writeValue(out.toFile(), slides);

            return Map.of("status", "done", "type", "slides", "output", out.toString());

        } catch (Exception e) {
            return Map.of("error", e.getMessage());
        }
    }

    // ==========================
    // 🧩  PUZZLES DATA
    // ==========================
    public Map<String, Object> generatePuzzlesAudio(String fileName) {
        try {
            Path path = Paths.get(DATA_DIR + fileName);
            Map<String, Object> json = mapper.readValue(path.toFile(), Map.class);

            List<Map<String, Object>> puzzles = (List<Map<String, Object>>) json.get("pages");
            int index = 1;
            for (Map<String, Object> puzzle : puzzles) {
                String question = (String) puzzle.get("title");
                String problem = (String) puzzle.get("problem");
                String idea = (String) puzzle.get("idea");
                String code = (String) puzzle.get("code");

                if (question != null && !puzzle.containsKey("title_audio")) {
                    String audioPath = ttsClient.synthesizeToFile(question, "puzzle_t_" + index);
                    puzzle.put("title_audio", audioPath);
                }
                if (problem != null && !puzzle.containsKey("problem_audio")) {
                    String audioPath = ttsClient.synthesizeToFile(problem, "puzzle_p_" + index);
                    puzzle.put("problem_audio", audioPath);
                }
                if (idea != null && !puzzle.containsKey("idea_audio")) {
                    String audioPath = ttsClient.synthesizeToFile(idea, "puzzle_i_" + index);
                    puzzle.put("idea_audio", audioPath);
                }
                if (code != null && !puzzle.containsKey("code_audio")) {
                    String audioPath = ttsClient.synthesizeToFile(code, "puzzle_c_" + index);
                    puzzle.put("code_audio", audioPath);
                }
                index++;
            }

            Path out = Paths.get(EXPORT_DIR + fileName.replace(".json", "-with-audio.json"));
            Files.createDirectories(out.getParent());
            mapper.writeValue(out.toFile(), json);

            return Map.of("status", "done", "type", "puzzles", "output", out.toString());

        } catch (Exception e) {
            return Map.of("error", e.getMessage());
        }
    }
    // ==========================
    // 🧩  WhiteBoard DATA
    // ==========================
    public Map<String, Object> generateWhiteBoardAudio(String fileName) {
        try {
            Path path = Paths.get(DATA_DIR + fileName);
            List<Map<String, Object>> records = mapper.readValue(path.toFile(), List.class);

            //List<Map<String, Object>> records = (List<Map<String, Object>>) json.get("pages");
            int index = 1;
            for (Map<String, Object> record : records) {
                String question = (String) record.get("question");
                String answer = (String) record.get("answer");
               

                if (question != null && !record.containsKey("question_audio")) {
                    String audioPath = ttsClient.synthesizeToFile(question, "question_" + index);
                    record.put("question_audio", audioPath);
                }
                if (answer != null && !record.containsKey("answer_audio")) {
                    String audioPath = ttsClient.synthesizeToFile(answer, "answer_" + index);
                    record.put("answer_audio", audioPath);
                }
                
                index++;
            }

            Path out = Paths.get(EXPORT_DIR + fileName.replace(".json", "-with-audio.json"));
            Files.createDirectories(out.getParent());
            mapper.writeValue(out.toFile(), records);

            return Map.of("status", "done", "type", "whiteboard", "output", out.toString());

        } catch (Exception e) {
            return Map.of("error", e.getMessage());
        }
    }
    
 // ==========================
    // 🧩  Interview DATA
    // ==========================
    public Map<String, Object> generateInterviewAudio(String fileName) {
        try {
            Path path = Paths.get(DATA_DIR + fileName);
            List<Map<String, Object>> records = mapper.readValue(path.toFile(), List.class);

            //List<Map<String, Object>> puzzles = (List<Map<String, Object>>) json.get("pages");
            int index = 1;
            for (Map<String, Object> record : records) {
                String question = (String) record.get("question");
                String answer = (String) record.get("answer");
               

                if (question != null && !record.containsKey("question_audio")) {
                    String audioPath = ttsClient.synthesizeToFile(question, "interview_q_" + index);
                    record.put("question_audio", audioPath);
                }
                if (answer != null && !record.containsKey("answer_audio")) {
                    String audioPath = ttsClient.synthesizeToFile(answer, "interview_a_" + index);
                    record.put("answer_audio", audioPath);
                }
                
                index++;
            }

            Path out = Paths.get(EXPORT_DIR + fileName.replace(".json", "-with-audio.json"));
            Files.createDirectories(out.getParent());
            mapper.writeValue(out.toFile(), records);

            return Map.of("status", "done", "type", "interviews", "output", out.toString());

        } catch (Exception e) {
            return Map.of("error", e.getMessage());
        }
    }
    // ==========================
    // 📘  STATIC PAGES OR LESSONS
    // ==========================
    public Map<String, Object> generatePagesAudio(String fileName) {
        try {
            Path path = Paths.get(DATA_DIR + fileName);
            List<Map<String, Object>> pages = mapper.readValue(path.toFile(), List.class);

            int index = 1;
            for (Map<String, Object> page : pages) {
                String body = (String) page.get("body");
                if (body != null && !page.containsKey("audio")) {
                    String audioPath = ttsClient.synthesizeToFile(body, "page_" + index);
                    page.put("audio", audioPath);
                }
                index++;
            }

            Path out = Paths.get(EXPORT_DIR + fileName.replace(".json", "-with-audio.json"));
            Files.createDirectories(out.getParent());
            mapper.writeValue(out.toFile(), pages);

            return Map.of("status", "done", "type", "pages", "output", out.toString());

        } catch (Exception e) {
            return Map.of("error", e.getMessage());
        }
    }
    
}
