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

package com.minecraft.moonlake.impl.anvil

import com.minecraft.moonlake.api.anvil.*
import com.minecraft.moonlake.api.event.MoonLakeListener
import com.minecraft.moonlake.api.exception.MoonLakeException
import com.minecraft.moonlake.api.getMoonLake
import com.minecraft.moonlake.api.notNull
import com.minecraft.moonlake.api.reflect.accessor.AccessorField
import com.minecraft.moonlake.api.reflect.accessor.Accessors
import com.minecraft.moonlake.api.registerEvent
import com.minecraft.moonlake.api.utility.MinecraftConverters
import com.minecraft.moonlake.api.utility.MinecraftReflection
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

open class AnvilWindowBase(plugin: Plugin) : AnvilWindowAbstract(plugin) {

    protected var handle: Any? = null
    private var _allowMove: Boolean = true
    private var listener: MoonLakeListener? = null

    override var isAllowMove: Boolean
        get() =_allowMove
        set(value) { _allowMove = value }

    override val isOpened: Boolean
        get() = handle != null

    override fun open(player: Player) {
        if(isOpened)
            throw MoonLakeException("铁砧窗口已经处于打开状态.")
        registerListener()
    }

    override fun getItem(anvilWindowSlot: AnvilWindowSlot): ItemStack {
        if(!isOpened)
            throw MoonLakeException("铁砧窗口尚未处于打开状态.")
        return getInventory().getItem(anvilWindowSlot.value())
    }

    override fun setItem(anvilWindowSlot: AnvilWindowSlot, itemStack: ItemStack) {
        if(!isOpened)
            throw MoonLakeException("铁砧窗口尚未处于打开状态.")
        getInventory().setItem(anvilWindowSlot.value(), itemStack)
    }

    override fun clear() {
        if(isOpened)
            getInventory().clear()
    }

    override fun release() {
        super.release()
        releaseListener()
        handle = null
    }

    open protected fun registerListener() {
        this.listener = object: MoonLakeListener {
            @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
            fun onClick(event: InventoryClickEvent) {
                if((event.inventory?.type != InventoryType.ANVIL || event.inventory != getInventory()) && !isAllowMove) {
                    event.isCancelled = true
                    event.result = Event.Result.DENY
                } else {
                    val clickSlot = AnvilWindowSlot.fromSlot(event.slot)
                    var clickEvent: AnvilWindowClickEvent? = null
                    if(clickSlot != null && clickHandler != null) {
                        val clickItemStack = event.currentItem ?: ItemStack(Material.AIR)
                        clickEvent = AnvilWindowClickEvent(this@AnvilWindowBase, getContainerAnvilPlayer(), clickSlot, clickItemStack)
                        try {
                            clickHandler.notNull().execute(clickEvent)
                        } catch(e: Exception) {
                            handleException(e)
                        }
                    }
                    if((clickEvent != null && clickEvent.isCancelled) || !isAllowMove) {
                        event.isCancelled = true
                        event.result = Event.Result.DENY
                    }
                }
            }
        }
        this.listener?.registerEvent(plugin)
    }

    open protected fun releaseListener() {
        if(listener != null)
            InventoryClickEvent.getHandlerList().unregister(listener)
        listener = null
    }

    open protected fun getInventory(): Inventory {
        throw UnsupportedOperationException()
    }

    open protected fun handleException(e: Exception?)
            = e?.printStackTrace()

    /** implement */

    private val containerAnvilEntityHuman: AccessorField by lazy {
        Accessors.getAccessorField(MinecraftReflection.getMinecraftClass("ContainerAnvil"), MinecraftReflection.getEntityHumanClass(), true) }

    protected fun getContainerAnvilPlayerHandle(): Any
            = containerAnvilEntityHuman.get(handle) as Any

    protected fun getContainerAnvilPlayer(): Player {
        val entityHuman = containerAnvilEntityHuman.get(handle)
        val converter = MinecraftConverters.getEntity(Player::class.java)
        return converter.getSpecific(entityHuman) as Player
    }

    protected inline fun <reified T: AnvilWindowEvent> callAnvilEvent(handler: AnvilWindowEventHandler<T>?, inputEventValue: String? = null): T? {
        return if(!getMoonLake().isEnabled) null else when(handler) {
            null -> null
            else -> {
                val player = getContainerAnvilPlayer()
                val anvilWindow = this@AnvilWindowBase
                var event: T? = when {
                    T::class.java == AnvilWindowOpenEvent::class.java -> AnvilWindowOpenEvent(anvilWindow, player)
                    T::class.java == AnvilWindowCloseEvent::class.java -> AnvilWindowCloseEvent(anvilWindow, player)
                    T::class.java == AnvilWindowInputEvent::class.java -> AnvilWindowInputEvent(anvilWindow, player, inputEventValue)
                    else -> null
                } as T?

                if(event != null) try {
                    handler.execute(event)
                } catch(e: Exception) {
                    handleException(e)
                    event = null // if exception
                }
                event
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if(other === this)
            return true
        if(other is AnvilWindowBase)
            return handle == other.handle
        return false
    }

    override fun hashCode(): Int {
        return handle?.hashCode() ?: 0
    }
}
