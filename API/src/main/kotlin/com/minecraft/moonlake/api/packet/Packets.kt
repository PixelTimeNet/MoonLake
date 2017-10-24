/*
 * Copyright (C) 2016-Present The MoonLake (mcmoonlake@hotmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.minecraft.moonlake.api.packet

import com.minecraft.moonlake.api.converter.ConverterEquivalentIgnoreNull
import com.minecraft.moonlake.api.reflect.accessor.AccessorConstructor
import com.minecraft.moonlake.api.reflect.accessor.AccessorField
import com.minecraft.moonlake.api.reflect.accessor.AccessorMethod
import com.minecraft.moonlake.api.reflect.accessor.Accessors
import com.minecraft.moonlake.api.utility.MinecraftPlayerMembers
import com.minecraft.moonlake.api.utility.MinecraftReflection
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import org.bukkit.entity.Player
import java.util.*

object Packets {

    @JvmStatic
    private val packetDataSerializerConstructor: AccessorConstructor<out Any> by lazy {
        Accessors.getAccessorConstructor(MinecraftReflection.getPacketDataSerializerClass(), false, ByteBuf::class.java) }
    @JvmStatic
    private val packetDataSerializerBuffer: AccessorField by lazy {
        Accessors.getAccessorField(MinecraftReflection.getPacketDataSerializerClass(), ByteBuf::class.java, true) }
    @JvmStatic
    private val packetRead: AccessorMethod by lazy {
        Accessors.getAccessorMethod(MinecraftReflection.getPacketClass(), "a", false, MinecraftReflection.getPacketDataSerializerClass()) }
    @JvmStatic
    private val packetWrite: AccessorMethod by lazy {
        Accessors.getAccessorMethod(MinecraftReflection.getPacketClass(), "b", false, MinecraftReflection.getPacketDataSerializerClass()) }
    @JvmStatic
    private val processPacket: AccessorMethod by lazy {
        Accessors.getAccessorMethod(MinecraftReflection.getPacketClass(), Void::class.java, true, arrayOf(MinecraftReflection.getPacketListenerClass())) }
    @JvmStatic
    private val sendPacket: AccessorMethod by lazy {
        Accessors.getAccessorMethod(MinecraftReflection.getPlayerConnectionClass(), "sendPacket", false, MinecraftReflection.getPacketClass()) }
    @JvmStatic
    private val converter: ConverterEquivalentIgnoreNull<PacketBukkit> by lazy {
        getPacketConverter() }
    @JvmStatic
    private val lookupBukkit: MutableMap<Class<*>, Class<out PacketBukkit>> by lazy {
        Collections.synchronizedMap(HashMap<Class<*>, Class<out PacketBukkit>>()) }

    init {

        /**
         * Packet Direction : Client -> Server
         */

        // Handshaking Start
        registerPacketBukkit("PacketHandshakingInSetProtocol", PacketInHandshake::class.java)

        // Status Start
        registerPacketBukkit("PacketStatusInStart", PacketInStatusStart::class.java)
        registerPacketBukkit("PacketStatusInPing", PacketInStatusPing::class.java)

        // Login Start
        registerPacketBukkit("PacketLoginInEncryptionBegin", PacketInLoginEncryptionResponse::class.java)
        registerPacketBukkit("PacketLoginInStart", PacketInLoginStart::class.java)

        // Play Start
        registerPacketBukkit("PacketPlayInArmAnimation", PacketInArmAnimation::class.java)
        registerPacketBukkit("PacketPlayInBlockDig", PacketInBlockDig::class.java)
        registerPacketBukkit("PacketPlayInBlockPlace", PacketInBlockPlaceLegacyAdapter())
        registerPacketBukkit("PacketPlayInChat", PacketInChat::class.java)
        registerPacketBukkit("PacketPlayInCustomPayload", PacketInPayload::class.java)
        registerPacketBukkit("PacketPlayInClientCommand", PacketInClientStatus::class.java)
        registerPacketBukkit("PacketPlayInEnchantItem", PacketInEnchantItem::class.java)
        registerPacketBukkit("PacketPlayInEntityAction", PacketInEntityAction::class.java)
        registerPacketBukkit("PacketPlayInFlying", PacketInFlying::class.java)
        registerPacketBukkit("PacketPlayInFlying\$PacketPlayInLook", arrayOf("PacketPlayInLook"), PacketInLook::class.java)
        registerPacketBukkit("PacketPlayInFlying\$PacketPlayInPosition", arrayOf("PacketPlayInPosition"), PacketInPosition::class.java)
        registerPacketBukkit("PacketPlayInFlying\$PacketPlayInPositionLook", arrayOf("PacketPlayInPositionLook"), PacketInPositionLook::class.java)
        registerPacketBukkit("PacketPlayInHeldItemSlot", PacketInHeldItemSlot::class.java)
        registerPacketBukkit("PacketPlayInKeepAlive", PacketInKeepAlive::class.java)
        registerPacketBukkit("PacketPlayInResourcePackStatus", PacketInResourcePackStatus::class.java)
        registerPacketBukkit("PacketPlayInSetCreativeSlot", PacketInSetCreativeSlot::class.java)
        registerPacketBukkit("PacketPlayInSettings", PacketInSettings::class.java)
        registerPacketBukkit("PacketPlayInSpectate", PacketInSpectate::class.java)
        registerPacketBukkit("PacketPlayInSteerVehicle", PacketInSteerVehicle::class.java)
        registerPacketBukkit("PacketPlayInTabComplete", PacketInTabComplete::class.java)
        registerPacketBukkit("PacketPlayInTransaction", PacketInTransaction::class.java)
        registerPacketBukkit("PacketPlayInUpdateSign", PacketInUpdateSign::class.java)
        registerPacketBukkit("PacketPlayInUseEntity", PacketInUseEntity::class.java)
        registerPacketBukkit("PacketPlayInWindowClick", PacketInWindowClick::class.java)

        /**
         * Packet Direction : Server -> Client
         */

        // Status Start
        registerPacketBukkit("PacketStatusOutServerInfo", PacketOutStatusServerInfo::class.java)
        registerPacketBukkit("PacketStatusOutPong", PacketOutStatusPong::class.java)

        // Login Start
        registerPacketBukkit("PacketLoginOutEncryptionBegin", PacketOutLoginEncryptionRequest::class.java)
        registerPacketBukkit("PacketLoginOutSetCompression", PacketOutLoginSetCompression::class.java)
        registerPacketBukkit("PacketLoginOutSuccess", PacketOutLoginSuccess::class.java)

        // Play Start
        registerPacketBukkit("PacketPlayOutAbilities", PacketOutAbilities::class.java)
        registerPacketBukkit("PacketPlayOutAnimation", PacketOutAnimation::class.java)
        registerPacketBukkit("PacketPlayOutBlockAction", PacketOutBlockAction::class.java)
        registerPacketBukkit("PacketPlayOutBlockBreakAnimation", PacketOutBlockBreakAnimation::class.java)
        registerPacketBukkit("PacketPlayOutChat", PacketOutChat::class.java)
        registerPacketBukkit("PacketPlayOutCloseWindow", PacketOutCloseWindow::class.java)
        registerPacketBukkit("PacketPlayOutCustomPayload", PacketOutPayload::class.java)
        registerPacketBukkit("PacketPlayOutEntityDestroy", PacketOutEntityDestroy::class.java)
        registerPacketBukkit("PacketPlayOutEntityEquipment", PacketOutEntityEquipment::class.java)
        registerPacketBukkit("PacketPlayOutHeldItemSlot", PacketOutHeldItemSlot::class.java)
        registerPacketBukkit("PacketPlayOutKeepAlive", PacketOutKeepAlive::class.java)
        registerPacketBukkit("PacketPlayOutKickDisconnect", PacketOutKickDisconnect::class.java)
        registerPacketBukkit("PacketPlayOutLogin", PacketOutJoinGame::class.java)
        registerPacketBukkit("PacketPlayOutPlayerInfo", PacketOutPlayerInfo::class.java)
        registerPacketBukkit("PacketPlayOutPlayerListHeaderFooter", PacketOutListHeaderFooter::class.java)
        registerPacketBukkit("PacketPlayOutPosition", PacketOutPosition::class.java)
        registerPacketBukkit("PacketPlayOutRespawn", PacketOutRespawn::class.java)
        registerPacketBukkit("PacketPlayOutServerDifficulty", PacketOutServerDifficulty::class.java)
        registerPacketBukkit("PacketPlayOutSetSlot", PacketOutSetSlot::class.java)
        registerPacketBukkit("PacketPlayOutTitle", PacketOutTitle::class.java)
        registerPacketBukkit("PacketPlayOutWorldParticles", PacketOutParticles::class.java)
    }

    @JvmStatic
    @JvmName("sendPacket")
    fun sendPacket(receiver: Player, packet: Any)
            { sendPacket.invoke(MinecraftPlayerMembers.CONNECTION.get(receiver), packet) } // Server -> Client

    @JvmStatic
    @JvmName("processPacket")
    fun processPacket(sender: Player, packet: Any)
            { processPacket.invoke(packet, MinecraftPlayerMembers.CONNECTION.get(sender)) } // Client -> Server

    @JvmStatic
    @JvmName("createPacket")
    @Throws(IllegalArgumentException::class)
    fun createPacket(clazz: Class<*>): Any
            = if(MinecraftReflection.getPacketClass().isAssignableFrom(clazz)) clazz.newInstance() else throw IllegalArgumentException("无效的数据包 $clazz 类.")

    @JvmStatic
    @JvmName("createBufferPacket")
    fun createBufferPacket(wrapped: PacketBukkit): Any
            = converter.getGenericValue(wrapped)

    @JvmStatic
    @JvmName("createBufferPacket")
    @Throws(UnsupportedOperationException::class)
    fun createBufferPacket(packet: Any): PacketBukkit
            = converter.getSpecificValue(packet)

    @JvmStatic
    @JvmName("createBufferPacketSafe")
    fun createBufferPacketSafe(packet: Any): PacketBukkit?
            = if(isRegistered(packet::class.java)) converter.getSpecificValue(packet) else null

    @JvmStatic
    @JvmName("createPacketDataSerializer")
    fun createPacketDataSerializer(buffer: ByteBuf = Unpooled.buffer()): Any
            = packetDataSerializerConstructor.newInstance(buffer)

    @JvmStatic
    @JvmName("createPacketBukkit")
    @Throws(UnsupportedOperationException::class)
    fun createPacketBukkit(clazz: Class<*>): PacketBukkit
            = lookupBukkit[clazz]?.newInstance() ?: throw UnsupportedOperationException("未对 NMS 数据包 $clazz 类添加包装类支持.")

    @JvmStatic
    @JvmName("createPacketBukkitSafe")
    fun createPacketBukkitSafe(clazz: Class<*>): PacketBukkit?
            = lookupBukkit[clazz]?.newInstance()

    @JvmStatic
    @JvmName("registerPacketBukkit")
    @Throws(IllegalArgumentException::class)
    fun registerPacketBukkit(clazzName: String, value: Class<out PacketBukkit>): Boolean
            = registerInternal(MinecraftReflection.getMinecraftClassOrNull(clazzName), value)

    @JvmStatic
    @JvmName("registerPacketBukkit")
    fun registerPacketBukkit(clazzName: String, aliases: Array<String>, value: Class<out PacketBukkit>): Boolean
            = registerInternal(MinecraftReflection.getMinecraftClassOrNull(clazzName, *aliases), value)

    @JvmStatic
    @JvmName("registerPacketBukkit")
    @Throws(IllegalArgumentException::class)
    fun registerPacketBukkit(clazzName: String, legacyAdapter: PacketLegacyAdapter<*, *>): Boolean
            = registerInternalLegacy(MinecraftReflection.getMinecraftClassOrNull(clazzName), legacyAdapter)

    @JvmStatic
    @JvmName("isRegistered")
    fun isRegistered(clazz: Class<*>): Boolean
            = lookupBukkit.containsKey(clazz)

    @JvmStatic
    @JvmName("isRegisteredWrapped")
    fun <T: Packet> isRegisteredWrapped(clazz: Class<out T>): Boolean
            = lookupBukkit.entries.firstOrNull { it.value == clazz } != null

    @JvmStatic
    @JvmName("getPacketConverter")
    private fun getPacketConverter(): ConverterEquivalentIgnoreNull<PacketBukkit> {
        return object: ConverterEquivalentIgnoreNull<PacketBukkit> {
            override fun getGenericValue(specific: PacketBukkit): Any {
                val handle = createPacket(specific.type)
                val packetBuffer = PacketBuffer()
                specific.write(packetBuffer)
                packetRead.invoke(handle, createPacketDataSerializer(packetBuffer.getByteBuf()))
                packetBuffer.release()
                return handle
            }
            override fun getSpecificValue(generic: Any): PacketBukkit {
                val wrapped = createPacketBukkit(generic::class.java)
                val packetDataSerializer = createPacketDataSerializer()
                val packetBuffer = PacketBuffer(packetDataSerializerBuffer.get(packetDataSerializer) as ByteBuf)
                packetWrite.invoke(generic, packetDataSerializer)
                wrapped.read(packetBuffer)
                packetBuffer.release()
                return wrapped
            }
            override fun getSpecificType(): Class<PacketBukkit>
                    = PacketBukkit::class.java
        }
    }

    @JvmStatic
    @JvmName("registerInternal")
    private fun registerInternal(clazz: Class<*>?, value: Class<out PacketBukkit>): Boolean {
        if(PacketBukkitLegacy::class.java.isAssignableFrom(value))
            throw IllegalArgumentException("数据包 $value 具有数据包遗产接口, 请使用 #registerPacketBukkit(String, PacketLegacyAdapter) 注册.")
        if(clazz == null || lookupBukkit.containsKey(clazz))
            throw IllegalArgumentException("未知的 NMS 数据包 ${clazz?.name} 类或已经被注册.")
        if(!value.canonicalName.contains("com.minecraft.moonlake.api.packet", true)) try {
            value.getConstructor()
        } catch(e: Exception) {
            throw IllegalArgumentException("数据包 $value 未存在无参构造函数, 注册失败.")
        }
        return lookupBukkit.put(clazz, value) == null
    }

    @JvmStatic
    @JvmName("registerInternalLegacy")
    private fun registerInternalLegacy(clazz: Class<*>?, legacyAdapter: PacketLegacyAdapter<*, *>): Boolean {
        if(clazz == null || lookupBukkit.containsKey(clazz))
            throw IllegalArgumentException("未知的 NMS 数据包 ${clazz?.name} 类或已经被注册.")
        val value = legacyAdapter.result
        if(!value.canonicalName.contains("com.minecraft.moonlake.api.packet", true)) try {
            value.getConstructor()
        } catch(e: Exception) {
            throw IllegalArgumentException("数据包 $value 未存在无参构造函数, 注册失败.")
        }
        return lookupBukkit.put(clazz, value) == null
    }
}
