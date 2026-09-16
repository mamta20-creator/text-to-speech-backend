package com.example.tts.service;

import com.example.tts.exception.TtsException;
import com.example.tts.model.TtsRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Set;

@Service
public class TtsService {

    private final RestClient restClient;

    private final String apiKey;

    private static final Set<String> SUPPORTED_LANGUAGES =
            Set.of(
                    "English",
                    "Hindi",
                    "Marathi",
                    "Gujarati",
                    "Spanish",
                    "French",
                    "German"
            );

    private static final Set<String> SUPPORTED_VOICES =
            Set.of(
                    "Female",
                    "Male"
            );

    public TtsService(
            @Value("${elevenlabs.api.key}") String apiKey) {

        this.apiKey = apiKey;

        this.restClient = RestClient.builder()
                .baseUrl("https://api.elevenlabs.io")
                .build();
    }

    // STEP 4
    // Communicate with ElevenLabs
    public byte[] convertTextToSpeech(TtsRequest request) {

        validateLanguage(request.getLanguage());

        validateVoice(request.getVoice());

        // STEP 4 + 5 + 6
        byte[] audio = callElevenLabs(request);

        if (audio == null || audio.length == 0) {

            throw new TtsException(
                    "ElevenLabs did not return audio."
            );
        }

        return audio;
    }

    // STEP 3
    // Verify language
    private void validateLanguage(String language) {

        if (!SUPPORTED_LANGUAGES.contains(language)) {

            throw new TtsException(
                    "Unsupported language: " + language
            );
        }
    }

    // STEP 3
    // Verify voice
    private void validateVoice(String voice) {

        if (!SUPPORTED_VOICES.contains(voice)) {

            throw new TtsException(
                    "Unsupported voice: " + voice
            );
        }
    }

    // STEP 4
    // Call ElevenLabs API
    private byte[] callElevenLabs(TtsRequest request) {

        try {

            String voiceId =
                    getVoiceId(request.getVoice());

            byte[] audio =
                    restClient.post()

                            .uri(
                                    "/v1/text-to-speech/"
                                    + voiceId
                                    + "?output_format=mp3_44100_128"
                            )

                            .header(
                                    "xi-api-key",
                                    apiKey
                            )

                            .contentType(
                                    MediaType.APPLICATION_JSON
                            )

                            .body(
                                    new ElevenLabsRequest(
                                            request.getText()
                                    )
                            )

                            .retrieve()

                            .body(byte[].class);

            return audio;

        } catch (Exception exception) {

         exception.printStackTrace();
            throw new TtsException(
                "ElevenLabs error:"+exception.getMessage(),
                    //"Failed to generate speech using ElevenLabs.",
                    exception
            );
        }
    }

    // Voice selection
    private String getVoiceId(String voice) {

        switch (voice) {

            case "Female":

                // Rachel
                return "hpp4J3VqNfWAUOO0d1Us";

            case "Male":

                // George
                return "JBFqnCBsd6RMkjVDRZzb";

            default:

                throw new TtsException(
                        "Unsupported voice: " + voice
                );
        }
    }

    // Request body sent to ElevenLabs
    private static class ElevenLabsRequest {

        private String text;

        private String model_id =
                "eleven_multilingual_v2";

        public ElevenLabsRequest(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        public String getModel_id() {
            return model_id;
        }
    }
}