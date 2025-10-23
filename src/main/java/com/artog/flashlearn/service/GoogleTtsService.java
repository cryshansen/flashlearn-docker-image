package com.artog.flashlearn.service;


//import com.google.cloud.texttospeech.v1.*;
//import com.google.protobuf.ByteString;


import org.springframework.stereotype.Service;

@Service("googleTtsService")
public class GoogleTtsService  {


    public byte[] synthesize(String text) throws Exception {
       /* try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {

            // Build the voice request (language and gender can be configurable)
            SynthesisInput input = SynthesisInput.newBuilder()
                    .setText(text)
                    .build();

            VoiceSelectionParams voice = VoiceSelectionParams.newBuilder()
                    .setLanguageCode("en-US")
                    .setSsmlGender(SsmlVoiceGender.NEUTRAL)
                    .build();

            AudioConfig audioConfig = AudioConfig.newBuilder()
                    .setAudioEncoding(AudioEncoding.MP3)
                    .build();

            // Perform TTS request
            SynthesizeSpeechResponse response = textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);

            ByteString audioContents = response.getAudioContent();
            return audioContents.toByteArray();*/
    	
    		return null;
        }
}


