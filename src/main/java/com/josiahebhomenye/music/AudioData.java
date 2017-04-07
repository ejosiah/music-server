package com.josiahebhomenye.music;


import lombok.Value;

import javax.sound.sampled.AudioFormat;

@Value
public class AudioData {
	private int blockNo;
	private AudioFormat format;
	private long length;
	private byte[] chuck;
}
