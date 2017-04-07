package com.josiahebhomenye.music;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.socket.ServerSocketChannel;

/**
 * Created by jay on 06/04/2017.
 */
public class ServerLogger extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ServerSocketChannel channel = (ServerSocketChannel)ctx.channel();
		System.out.printf("Server listening on port: %s\n", channel.localAddress().getPort());
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		System.out.println("Server going offline");
	}
}
