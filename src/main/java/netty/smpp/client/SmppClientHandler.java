package netty.smpp.client;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufUtil;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty.smpp.api.BadCommandIDException;
import netty.smpp.api.Connection;
import netty.smpp.api.message.Bind;
import netty.smpp.api.message.SMPPPacket;
import netty.smpp.api.util.SMPPIO;
import netty.smpp.api.version.SMPPVersion;
import netty.smpp.traditional.SmppReceiverConfig;

import java.net.UnknownHostException;

/**
 * Listing 2.6 of <i>Netty in Action</i>
 *
 * @author <a href="mailto:nmaurer@redhat.com">Norman Maurer</a>
 */
@Sharable
public class SmppClientHandler extends
        SimpleChannelInboundHandler<ByteBuf> {

    private SmppReceiverConfig config = new SmppReceiverConfig();

    Connection myConnection = null;

    public SmppClientHandler() {
        try {
            myConnection = new Connection(config.getHost(), config.getPort(), true);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws BadCommandIDException {

        Bind bindReq = (Bind) myConnection.newInstance(SMPPPacket.BIND_RECEIVER);

        bindReq.setVersion(SMPPVersion.getDefaultVersion());
        bindReq.setSystemId(config.getSystemId());
        bindReq.setPassword(config.getPassword());
        bindReq.setSystemType(config.getSystemType());
        bindReq.setAddressTon(config.getBindTON());
        bindReq.setAddressNpi(config.getBindNPI());
        bindReq.setAddressRange(config.getAddressRange());

        byte[] commandLength = SMPPIO.intToBytes(bindReq.getLength(), 4);
        byte[] commandId = SMPPIO.intToBytes(bindReq.getCommandId(), 4);
        byte[] commandStatus = SMPPIO.intToBytes(bindReq.getCommandStatus(), 4);
        byte[] sequenceNumber = SMPPIO.intToBytes(bindReq.getSequenceNum(), 4);


        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeBytes(commandLength);
        byteBuf.writeBytes(commandId);
        byteBuf.writeBytes(commandStatus);
        byteBuf.writeBytes(sequenceNumber);


        writeCSString(byteBuf, bindReq.getSystemId());
        writeCSString(byteBuf, bindReq.getPassword());
        writeCSString(byteBuf, bindReq.getSystemType());
        byteBuf.writeBytes(SMPPIO.intToBytes(bindReq.getVersion().getVersionID(), 1));
        byteBuf.writeBytes(SMPPIO.intToBytes(bindReq.getAddressTon(), 1));
        byteBuf.writeBytes(SMPPIO.intToBytes(bindReq.getAddressNpi(), 1));
        writeCSString(byteBuf, bindReq.getAddressRange());

        // ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
        ctx.writeAndFlush(byteBuf);
    }

    private void writeCSString(ByteBuf byteBuf, String str) {
        byteBuf.writeBytes(str.getBytes());
        byteBuf.writeByte(0);
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx,
                             ByteBuf in) {
        System.out.println("Client received: ");
      //  System.out.println("Client received: " + ByteBufUtil
        //        .hexDump(in));
        // read the cmdLength
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,
                                Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
