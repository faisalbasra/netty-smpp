package netty.smpp.traditional;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

public class SmppClient {

    public static void main(String[] args) {
        Charset utf8 = Charset.forName("UTF-8");


        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        System.out.println((char) buf.getByte(0));
        int readerIndex = buf.readerIndex();
        int writerIndex = buf.writerIndex();

        buf.setByte(0, (byte) 'B');

        System.out.println((char) buf.getByte(0));

        /*
        assert readerIndex = buf.readerIndex();
        assert writerIndex = buf.writerIndex();
*/

    }
}
