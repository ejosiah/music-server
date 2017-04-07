package com.josiahebhomenye.music;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.SneakyThrows;

import javax.sound.sampled.AudioFormat;

@ChannelHandler.Sharable
public class AudioFormatEncoder extends MessageToByteEncoder<AudioFormat> {

	@Override
	protected void encode(ChannelHandlerContext ctx, AudioFormat format, ByteBuf out) throws Exception {
		ByteBuf buf  = Unpooled.buffer();
		buf.writeBoolean(format.isBigEndian());
		buf.writeInt(format.getChannels());
		writeEncoding(buf, format.getEncoding());
		buf.writeFloat(format.getFrameRate());
		buf.writeInt(format.getFrameSize());
		buf.writeFloat(format.getSampleRate());
		buf.writeInt(format.getSampleSizeInBits());

		out.writeInt(buf.readableBytes());
		out.writeBytes(buf);
	}

	@SneakyThrows
	private void writeEncoding(ByteBuf out, AudioFormat.Encoding encoding) {
		if(encoding == AudioFormat.Encoding.ALAW){
			out.writeInt(1);
		}else if(encoding == AudioFormat.Encoding.PCM_FLOAT){
			out.writeInt(2);
		}else if(encoding == AudioFormat.Encoding.PCM_SIGNED){
			out.writeInt(3);
		}else if(encoding == AudioFormat.Encoding.PCM_UNSIGNED){
			out.writeInt(4);
		}else if(encoding == AudioFormat.Encoding.ULAW){
			out.writeInt(5);
		}
	}
}
