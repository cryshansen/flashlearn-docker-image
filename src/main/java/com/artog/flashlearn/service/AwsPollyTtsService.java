package com.artog.flashlearn.service;


import software.amazon.awssdk.services.polly.PollyClient;
import software.amazon.awssdk.services.polly.model.*;
import software.amazon.awssdk.core.SdkBytes;
import org.springframework.stereotype.Service;

import com.artog.flashlearn.service.TtsService;

@Service("awsPollyTtsService")
public class AwsPollyTtsService {


    public byte[] synthesize(String text) throws Exception {
        /*try (PollyClient polly = PollyClient.create()) {

            SynthesizeSpeechRequest synthReq = SynthesizeSpeechRequest.builder()
                    .text(text)
                    .voiceId("Joanna")    // Choose any Polly voice
                    .outputFormat(OutputFormat.MP3)
                    .build();

            //SynthesizeSpeechResponse synthRes = polly.synthesizeSpeech(synthReq);
           // SdkBytes audioBytes = synthRes.audioStream().readAllBytes();

            return  null ;// audioBytes.asByteArray();
        }*/
    	return  null ;
    }
}
