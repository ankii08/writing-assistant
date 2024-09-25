package com.ankit.project.writing_assistant.controller;

import com.ankit.project.writing_assistant.entity.Draft;
import com.ankit.project.writing_assistant.service.OpenAIService;
import com.ankit.project.writing_assistant.service.WritingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class WritingAssistantController {

    private final OpenAIService openAIService;
    private final WritingService writingService;

    public WritingAssistantController(OpenAIService openAIService, WritingService writingService) {
        this.openAIService = openAIService;
        this.writingService = writingService;
    }

    // Display the editor page
    @GetMapping("/editor")
    public String showEditor(Model model) {
        model.addAttribute("draft", new Draft()); // Initialize an empty draft for the editor
        return "editor";
    }

    // Handle text generation via OpenAI API
    @PostMapping("/generate")
    public String generateText(@RequestParam String prompt, Model model) {
        try {
            String aiText = openAIService.generateText(prompt); // Call the OpenAI service for text
            model.addAttribute("generatedText", aiText); // Add the generated text to the model
        } catch (Exception e) {
            model.addAttribute("error", "Error generating text: " + e.getMessage()); // Handle any errors
        }
        return "editor"; // Return to the editor view
    }

    // Handle image generation via OpenAI DALL·E API
    @PostMapping("/generateImage")
    public String generateImage(@RequestParam String prompt, Model model) {
        try {
            String imageUrl = openAIService.generateImage(prompt); // Call the OpenAI service for image
            model.addAttribute("generatedImageUrl", imageUrl); // Add the generated image URL to the model
        } catch (Exception e) {
            model.addAttribute("error", "Error generating image: " + e.getMessage()); // Handle any errors
        }
        return "editor"; // Return to the editor view
    }

    // Save a draft to the database
    @PostMapping("/save")
    public String saveDraft(@ModelAttribute Draft draft) {
        writingService.saveDraft(draft); // Save the draft using the WritingService
        return "redirect:/drafts"; // Redirect to the list of drafts
    }

    // List all saved drafts
    @GetMapping("/drafts")
    public String listDrafts(Model model) {
        model.addAttribute("drafts", writingService.getAllDrafts()); // Fetch all drafts and add to model
        return "drafts"; // Render the drafts list view
    }
}
