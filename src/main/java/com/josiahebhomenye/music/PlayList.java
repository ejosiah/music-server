package com.josiahebhomenye.music;

import lombok.SneakyThrows;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.nio.file.Files;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by jay on 07/04/2017.
 */
public class PlayList {
	private List<File> songs;
	private boolean loop;
	int next;

	public PlayList(File playlist){
		this(playlist, false, false);

	}

	public PlayList(File playlist, boolean shuffle, boolean loop){
		this(extract(playlist), shuffle, loop);

	}

	@SneakyThrows
	public AudioFormat format(){
		return toAudio(songs.get(0)).getFormat();
	}

	@SneakyThrows
	static List<File> extract(File playlist){
		return Files.readAllLines(playlist.toPath()).stream().map(File::new).collect(toList());
	}

	public PlayList(List<File> files){
		this(files, false, false);
	}

	public PlayList(List<File> files, boolean shuffle, boolean loop){
		songs = files;
		loop(loop);
		if(shuffle) shuffle();
	}

	@SneakyThrows
	private static AudioInputStream toAudio(File file){
		AudioInputStream stream = AudioSystem.getAudioInputStream(file);
		if(file.getName().endsWith(".mp3")){
			AudioInputStream oldStream = stream;
			AudioFormat baseFormat = oldStream.getFormat();
			AudioFormat mp3Format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,
					baseFormat.getSampleRate(),
					16,
					baseFormat.getChannels(),
					baseFormat.getChannels() * 2,
					baseFormat.getSampleRate(),
					false);
			stream = AudioSystem.getAudioInputStream(mp3Format, oldStream);
		}
		return stream;
	}

	public void shuffle(){
		Collections.shuffle(songs);
	}

	public void loop(boolean flag){
		loop = flag;
	}

	public AudioInputStream next(){
		if(next >= songs.size() && !loop) return null;
		AudioInputStream in =  toAudio(songs.get(next++));
		if(loop){
			next = next % songs.size();
		}
		return in;
	}
}
