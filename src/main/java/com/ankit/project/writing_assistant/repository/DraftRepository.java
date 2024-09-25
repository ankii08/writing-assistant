package com.ankit.project.writing_assistant.repository;

import com.ankit.project.writing_assistant.entity.Draft;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DraftRepository extends JpaRepository<Draft, Long> {
}