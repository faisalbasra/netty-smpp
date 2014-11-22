package netty.smpp.util;


import io.netty.buffer.ByteBuf;

public class IOUtil {

    public static void writeCString(String str, ByteBuf byteBuf) {
        byteBuf.writeBytes(str.getBytes());
        byteBuf.writeByte(0);
    }


}
