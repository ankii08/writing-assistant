package com.ankit.project.writing_assistant.service;

import com.ankit.project.writing_assistant.entity.Draft;
import com.ankit.project.writing_assistant.repository.DraftRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WritingService {

    private final DraftRepository draftRepository;

    public WritingService(DraftRepository draftRepository) {
        this.draftRepository = draftRepository;
    }

    public List<Draft> getAllDrafts() {
        return draftRepository.findAll();
    }

    public Draft saveDraft(Draft draft) {
        return draftRepository.save(draft);
    }

}
