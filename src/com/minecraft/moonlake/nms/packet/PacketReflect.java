package com.minecraft.moonlake.nms.packet;

import com.minecraft.moonlake.nms.packet.exception.PacketException;
import com.minecraft.moonlake.nms.packet.exception.PacketInitializeException;
import com.minecraft.moonlake.validate.Validate;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static com.minecraft.moonlake.reflect.Reflect.*;

/**
 * <h1>PacketReflect</h1>
 * 数据包反射实现类
 *
 * @version 1.0
 * @author Month_Light
 */
class PacketReflect {

    private Class<?> CLASS_PACKET;
    private Class<?> CLASS_CRAFTPLAYER;
    private Class<?> CLASS_ENTITYPLAYER;
    private Class<?> CLASS_PLAYERCONNECTION;
    private Method METHOD_GETHANDLE;
    private Method METHOD_SENDPACKET;
    private Field FIELD_PLAYERCONNECTION;

    private static PacketReflect packetReflectInstance;

    private PacketReflect() {

        try {

            // Packet Class
            CLASS_PACKET = PackageType.MINECRAFT_SERVER.getClass("Packet");
            CLASS_CRAFTPLAYER = PackageType.CRAFTBUKKIT_ENTITY.getClass("CraftPlayer");
            CLASS_ENTITYPLAYER = PackageType.MINECRAFT_SERVER.getClass("EntityPlayer");
            CLASS_PLAYERCONNECTION = PackageType.MINECRAFT_SERVER.getClass("PlayerConnection");

            // Packet Method
            METHOD_GETHANDLE = getMethod(CLASS_CRAFTPLAYER, "getHandle");
            METHOD_SENDPACKET = getMethod(CLASS_PLAYERCONNECTION, "sendPacket", CLASS_PACKET);

            // Packet Field
            FIELD_PLAYERCONNECTION = getField(CLASS_ENTITYPLAYER, true, "playerConnection");
        }
        catch (Exception e) {

            throw new PacketInitializeException("The nms packet reflect raw initialize exception.", e);
        }
    }

    public static PacketReflect get() throws PacketException {

        if(packetReflectInstance == null) {

            packetReflectInstance = new PacketReflect();
        }
        return packetReflectInstance;
    }

    public Object getNMSPlayer(Player player) throws PacketException {

        Validate.notNull(player, "The player object is null.");

        try {

            return METHOD_GETHANDLE.invoke(player);
        }
        catch (Exception e) {

            throw new PacketException("The get nms player handle exception.", e);
        }
    }

    public void send(Player[] players, Object packet) throws PacketException {

        Validate.notNull(players, "The player object is null.");
        Validate.notNull(packet, "The packet object is null.");

        try {

            for(final Player player : players) {

                METHOD_SENDPACKET.invoke(FIELD_PLAYERCONNECTION.get(METHOD_GETHANDLE.invoke(player)), packet);
            }
        }
        catch (Exception e) {

            throw new PacketException("The nms packet send exception.", e);
        }
    }
}
