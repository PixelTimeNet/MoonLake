package com.minecraft.moonlake.nms.packet;

import com.minecraft.moonlake.nms.packet.exception.PacketException;
import com.minecraft.moonlake.nms.packet.exception.PacketInitializeException;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static com.minecraft.moonlake.reflect.Reflect.*;

/**
 * <h1>PacketPlayOutPlayerInfo_v1_10_R1</h1>
 * 数据包输出玩家信息 v1.10-R1 实现类（详细doc待补充...）
 *
 * @version 1.0
 * @author Month_Light
 */
class PacketPlayOutPlayerInfo_v1_10_R1 extends PacketPlayOutPlayerInfo<PacketPlayOutPlayerInfo_v1_10_R1> {

    private final static Class<?> CLASS_PACKETPLAYOUTPLAYERINFO;
    private final static Class<?> CLASS_PACKETPLAYOUTPLAYERINFO_ENUMPLAYERINFOACTION;
    private final static Method METHOD_VALUEOF;

    static {

        try {

            CLASS_PACKETPLAYOUTPLAYERINFO = net.minecraft.server.v1_10_R1.PacketPlayOutPlayerInfo.class;
            CLASS_PACKETPLAYOUTPLAYERINFO_ENUMPLAYERINFOACTION = net.minecraft.server.v1_10_R1.PacketPlayOutPlayerInfo.EnumPlayerInfoAction.class;
            METHOD_VALUEOF = getMethod(CLASS_PACKETPLAYOUTPLAYERINFO_ENUMPLAYERINFOACTION, "valueOf", String.class);
        }
        catch (Exception e) {

            throw new PacketInitializeException("The nms packet play out player info reflect raw initialize exception.", e);
        }
    }

    @Deprecated
    public PacketPlayOutPlayerInfo_v1_10_R1() {

        super(null, null);
    }

    public PacketPlayOutPlayerInfo_v1_10_R1(Action action, Player player) {

        super(action, player);
    }

    @Override
    public void send(Player... player) throws PacketException {

        try {

            Object enumPlayerInfoAction = METHOD_VALUEOF.invoke(null, getAction().get().name());
            Object nmsPlayer = PacketReflect.get().getNMSPlayer(getPlayer().get());

            Constructor<?> packetConstructor = getConstructor(CLASS_PACKETPLAYOUTPLAYERINFO, CLASS_PACKETPLAYOUTPLAYERINFO_ENUMPLAYERINFOACTION, net.minecraft.server.v1_10_R1.EntityPlayer[].class);
            Object packet = packetConstructor.newInstance(enumPlayerInfoAction, new net.minecraft.server.v1_10_R1.EntityPlayer[] { (net.minecraft.server.v1_10_R1.EntityPlayer) nmsPlayer });

            PacketReflect.get().send(player, packet);
        }
        catch (Exception e) {

            throw new PacketException("The nms packet play out player info send exception.", e);
        }
    }
}
