
package com.example.tts.controller;

import com.example.tts.model.TtsRequest;
import com.example.tts.service.TtsService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tts")
@CrossOrigin(origins="*")
public class TtsController {

	private final TtsService ttsService;

	public TtsController(TtsService ttsService) {
		this.ttsService = ttsService;
	}

	@PostMapping(value = "/convert", produces = "audio/mpeg")
	public ResponseEntity<byte[]> convertTextToSpeech(@Valid @RequestBody TtsRequest request) {

		byte[] audio = ttsService.convertTextToSpeech(request);

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"speech.mp3\"")
				.contentType(MediaType.parseMediaType("audio/mpeg")).body(audio);
	}
}
