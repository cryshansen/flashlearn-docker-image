package com.artog.flashlearn.service;
//package com.artog.tts.service

import org.springframework.stereotype.Service;
/**
 * The superclass to the individual speech services of openai , google ai and oolly amazon apis 
 * Their classes implement this one polymorphism /inheritance 
 */
@Service
public class TtsService {
	
	public byte[] synthesize(String text) throws Exception {
		return null;
	}
	
	
	
	
	
}

