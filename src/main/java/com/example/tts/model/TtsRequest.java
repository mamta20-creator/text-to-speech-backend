
package com.example.tts.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class TtsRequest {

	@NotBlank(message = "Text is required")
	@Size(max = 1000, message = "Text cannot exceed 1000 characters")
	private String text;

	@NotBlank(message = "Language is required")
	private String language;

	@NotBlank(message = "Voice is required")
	private String voice;

	public TtsRequest() {
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getVoice() {
		return voice;
	}

	public void setVoice(String voice) {
		this.voice = voice;
	}
}
