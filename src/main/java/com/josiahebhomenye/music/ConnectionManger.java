package com.josiahebhomenye.music;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import lombok.RequiredArgsConstructor;

import javax.sound.sampled.AudioFormat;

@RequiredArgsConstructor
@ChannelHandler.Sharable
public class ConnectionManger extends ChannelInboundHandlerAdapter {
	private final AudioFormat format;
	private final ChannelGroup subscribers;

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(format);

	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		System.out.printf("client: %s unsubscribed\n", ctx.channel().remoteAddress());
		subscribers.remove(ctx.channel());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("connection accepted from: " + ctx.channel().remoteAddress());
		subscribers.add(ctx.channel());
	}
}
