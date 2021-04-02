package com.netty.protocol;

import com.netty.Serialzer.JsonSerializer;
import com.netty.Serialzer.Serializer;
import com.netty.packet.Body;
import com.netty.packet.Header;
import com.netty.packet.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import lombok.extern.slf4j.Slf4j;

import static com.netty.constant.Crypt.JSON_CRYPT;


/**
 * @author min
 */
@Slf4j
public class PacketProtocol {



    private static volatile PacketProtocol instance;

    public static PacketProtocol getInstance() {
        if (instance == null) {
            synchronized (PacketProtocol.class) {
                if (instance == null) {
                    instance = new PacketProtocol();
                }
            }
        }
        return instance;
    }

    private PacketProtocol() {

    }

    public ByteBuf encode(ByteBufAllocator alloc, Packet packet) {
        ByteBuf byteBuf = alloc.ioBuffer();
        return encode(byteBuf, packet);
    }

    public ByteBuf encode(ByteBuf byteBuf, Packet packet) {
        Header header = packet.getHeader();
        Body body = packet.getBody();


        byteBuf.writeInt(header.getMagic());
        byteBuf.writeByte(header.getVersion());
        byteBuf.writeByte(header.getCrypt());
        byteBuf.writeByte(header.getCmd());


        byte[] bytes = serializer(header.getCrypt()).serialize(body);
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);

        return byteBuf;
    }

    public Serializer serializer(byte crypt) {
        Serializer serializer = null;
        switch (crypt) {
            case JSON_CRYPT:
                serializer = JsonSerializer.getInstance();
                break;
            default:
        }
        return serializer;
    }


    public Packet decode(ByteBuf buf) {
        //解码过程，即组解析信包
        int magic = buf.readInt();
        byte version = buf.readByte();
        byte crypt = buf.readByte();
        byte cmd = buf.readByte();
        int dataLen = buf.readInt();
        byte[] dataBytes = new byte[dataLen];
        buf.readBytes(dataBytes);

        Header header = new Header(magic, version, crypt, cmd, dataLen);
        Body body = serializer(header.getCrypt()).deSerialize(Body.class, dataBytes);
        return new Packet(header, body);
    }
}
