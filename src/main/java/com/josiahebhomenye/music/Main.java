package com.josiahebhomenye.music;

import lombok.SneakyThrows;

import java.io.File;

/**
 * Created by jay on 06/04/2017.
 */
public class Main {

	@SneakyThrows
	public static void main(String[] args){
		File file = new File("/Users/jay/playlist.txt");
		MusicServer music = new MusicServer(file);
		Runtime.getRuntime().addShutdownHook(new Thread(music::stop));

		try{
			music.run();
		}finally {
			music.stop();
		}
	}
}
