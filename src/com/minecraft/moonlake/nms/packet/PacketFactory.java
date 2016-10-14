package com.minecraft.moonlake.nms.packet;

import com.minecraft.moonlake.nms.packet.exception.PacketException;
import com.minecraft.moonlake.reflect.Reflect;
import org.bukkit.entity.Player;

/**
 * <h1>PacketFactory</h1>
 * 数据包工厂类
 *
 * @version 1.0
 * @author Month_Light
 */
public class PacketFactory {

    /**
     * PacketFactory Static Instance;
     */
    private static PacketFactory packetFactoryInstance;

    private PacketFactory() {

    }

    /**
     * 获取 PacketFactory 对象
     *
     * @return PacketFactory
     */
    public static PacketFactory get() {

        if(packetFactoryInstance == null) {

            packetFactoryInstance = new PacketFactory();
        }
        return packetFactoryInstance;
    }

    /**
     * 将指定数据包对象发送到指定玩家
     *
     * @param players 玩家
     * @param packet 数据包
     * @throws IllegalArgumentException 如果玩家对象为 {@code null} 则抛出异常
     * @throws IllegalArgumentException 如果数据包对象为 {@code null} 则抛出异常
     * @throws PacketException 如果发送数据包错误则抛出异常
     */
    public void sendPacket(Player[] players, Object packet) throws PacketException {

        PacketReflect.get().send(players, packet);
    }

    /**
     * 获取指定 Packet 的实例对象
     *
     * @param clazz Packet
     * @param <T> Packet
     * @return Packet 实例对象
     * @throws PacketException 如果获取错误则抛出异常
     * @deprecated 已过时, 建议使用参数获取实例对象
     * @see #instance(Class, Object...)
     */
    @Deprecated
    public <T extends Packet> T instance(Class<T> clazz) throws PacketException {

        return instance(clazz, new Object[] { });
    }

    /**
     * 获取指定 Packet 的实例对象
     *
     * @param clazz Packet
     * @param args Packet 构造参数
     * @param <T> Packet
     * @return Packet 实例对象
     * @throws PacketException 如果获取错误则抛出异常
     */
    public <T extends Packet> T instance(Class<T> clazz, Object... args) throws PacketException {

        try {

            return (T) Reflect.instantiateObject(clazz, args);
        }
        catch (Exception e) {

            throw new PacketException("The get nms packet instance exception.", e);
        }
    }
}
