package com.josiahebhomenye.music;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.SneakyThrows;

import javax.sound.sampled.AudioInputStream;
import java.io.File;


public class MusicServer implements Runnable {
	private EventLoopGroup group;
	private ServerBootstrap bootstrap;
	private PlayList playList;
	private ChannelGroup subscribers;
	private AudioFormatEncoder encoder;
	private ConnectionManger connectionManger;

	@SneakyThrows
	MusicServer(File file){
		playList = new PlayList(file, false, true);
		encoder = new AudioFormatEncoder();
		subscribers = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
		connectionManger = new ConnectionManger(playList.format(), subscribers);
		group = new NioEventLoopGroup();
		bootstrap = new ServerBootstrap();
		bootstrap
				.group(group)
				.channel(NioServerSocketChannel.class)
				.handler(new ServerLogger())
				.childHandler(new ChannelInitializer<Channel>() {
					@Override
					protected void initChannel(Channel ch) throws Exception {
						ch.pipeline()
							.addLast(encoder)
							.addLast(connectionManger);


					}
				});
	}

	@Override
	@SneakyThrows
	public void run() {
		bootstrap.bind(9999).sync().channel();
		byte[] buf = new byte[1024 * 1024];

		for(AudioInputStream in = playList.next(); in != null; in = playList.next()){
			int read;
			while((read = in.read(buf)) != -1){
				ByteBuf byteBuf = Unpooled.copiedBuffer(buf, 0, read);
				subscribers.writeAndFlush(byteBuf);
			}
	//		TimeUnit.MINUTES.sleep(1);

		}
	}

	private boolean isRunning(){
		return group != null;
	}

	@SneakyThrows
	void stop(){
		if(isRunning()) {
			group.shutdownGracefully().sync();
			group = null;
		}
	}

}
