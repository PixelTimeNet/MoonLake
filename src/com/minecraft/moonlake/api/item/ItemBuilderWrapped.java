package com.minecraft.moonlake.api.item;

import com.minecraft.moonlake.api.item.potion.PotionEffectCustom;
import com.minecraft.moonlake.api.item.potion.PotionEffectType;
import com.minecraft.moonlake.api.item.potion.PotionType;
import com.minecraft.moonlake.validate.Validate;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Map;

/**
 * Created by MoonLake on 2016/9/13.
 */
class ItemBuilderWrapped implements ItemBuilder {

    private final ItemLibrary itemLibrary;
    private ItemStack itemStack;

    public ItemBuilderWrapped(ItemStack itemStack) {

        Validate.notNull(itemStack, "The itemstack object is null.");

        this.itemStack = itemStack;
        this.itemLibrary = ItemLibraryFactory.get().item();
    }

    public ItemBuilderWrapped(Material material) {

        this(ItemLibraryFactorys.craft().create(material));
    }

    public ItemBuilderWrapped(Material material, int data) {

        this(ItemLibraryFactorys.craft().create(material, data));
    }

    public ItemBuilderWrapped(Material material, int data, int amount) {

        this(ItemLibraryFactorys.craft().create(material, data, amount));
    }

    public ItemBuilderWrapped(Material material, int data, int amount, String displayName) {

        this(ItemLibraryFactorys.craft().create(material, data, amount, displayName));
    }

    public ItemBuilderWrapped(Material material, int data, int amount, String displayName, String... lore) {

        this(ItemLibraryFactorys.craft().create(material, data, amount, displayName, lore));
    }

    public ItemBuilderWrapped(PotionType potion) {

        this(potion.getMaterial());
    }

    public ItemBuilderWrapped(PotionType potion, int data) {

        this(potion.getMaterial(), data);
    }

    public ItemBuilderWrapped(PotionType potion, int data, int amount) {

        this(potion.getMaterial(), data, amount);
    }

    public ItemBuilderWrapped(PotionType potion, int data, int amount, String displayName) {

        this(potion.getMaterial(), data, amount, displayName);
    }

    public ItemBuilderWrapped(PotionType potion, int data, int amount, String displayName, String... lore) {

        this(potion.getMaterial(), data, amount, displayName, lore);
    }

    protected void update(ItemStack itemStack) {

        this.itemStack = itemStack;
    }
    
    protected ItemStack get() {
        
        return this.itemStack;
    }
    
    protected ItemLibrary library() {
        
        return this.itemLibrary;
    }

    @Override
    public ItemBuilder setDisplayName(String displayName) {
        
        update(library().setDisplayName(get(), displayName));
        
        return this;
    }

    @Override
    public ItemBuilder setAmount(int amount) {

        update(library().setAmount(get(), amount));

        return this;
    }

    @Override
    public ItemBuilder setDurability(int durability) {

        update(library().setDurability(get(), durability));

        return this;
    }

    @Override
    public ItemBuilder resetDurability() {

        update(library().resetDurability(get()));

        return this;
    }

    @Override
    public ItemBuilder addDurability(int durability) {

        update(library().addDurability(get(), durability));

        return this;
    }

    @Override
    public ItemBuilder takeDurability(int durability) {

        update(library().takeDurability(get(), durability));

        return this;
    }

    @Override
    public ItemBuilder setLore(String... lore) {

        update(library().setLore(get(), lore));

        return this;
    }

    @Override
    public ItemBuilder setLore(Collection<? extends String> lore) {

        update(library().setLore(get(), lore));

        return this;
    }

    @Override
    public ItemBuilder addLore(String... lore) {

        update(library().addLore(get(), lore));

        return this;
    }

    @Override
    public ItemBuilder addLore(Collection<? extends String> lore) {

        update(library().addLore(get(), lore));

        return this;
    }

    @Override
    public ItemBuilder clearLore() {

        update(library().clearLore(get()));

        return this;
    }

    @Override
    public ItemBuilder addEnchantment(Enchantment enchantment, int level) {

        update(library().addEnchantment(get(), enchantment, level));

        return this;
    }

    @Override
    public ItemBuilder addEnchantment(Map<Enchantment, Integer> enchantmentMap) {

        update(library().addEnchantment(get(), enchantmentMap));

        return this;
    }

    @Override
    public ItemBuilder addSafeEnchantment(Enchantment enchantment, int level) {

        update(library().addSafeEnchantment(get(), enchantment, level));

        return this;
    }

    @Override
    public ItemBuilder addSafeEnchantment(Map<Enchantment, Integer> enchantmentMap) {

        update(library().addSafeEnchantment(get(), enchantmentMap));

        return this;
    }

    @Override
    public ItemBuilder removeEnchantment(Enchantment enchantment) {

        update(library().removeEnchantment(get(), enchantment));

        return this;
    }

    @Override
    public ItemBuilder removeEnchantment(Collection<? extends Enchantment> enchantments) {

        update(library().removeEnchantment(get(), enchantments));

        return this;
    }

    @Override
    public ItemBuilder clearEnchantment() {

        update(library().clearEnchantment(get()));

        return this;
    }

    @Override
    public ItemBuilder addFlags(ItemFlag... flags) {

        update(library().addFlags(get(), flags));

        return this;
    }

    @Override
    public ItemBuilder addFlags(Collection<? extends ItemFlag> flags) {

        update(library().addFlags(get(), flags));

        return this;
    }

    @Override
    public ItemBuilder removeFlags(ItemFlag... flags) {

        update(library().removeFlags(get(), flags));

        return this;
    }

    @Override
    public ItemBuilder removeFlags(Collection<? extends ItemFlag> flags) {

        update(library().removeFlags(get(), flags));

        return this;
    }

    @Override
    public ItemBuilder clearFlags() {

        update(library().clearFlags(get()));

        return this;
    }

    @Override
    public ItemBuilder setLeatherColor(Color color) {

        update(library().setLeatherColor(get(), color));

        return this;
    }

    @Override
    public ItemBuilder setLeatherColor(int red, int green, int blue) {

        update(library().setLeatherColor(get(), red, green, blue));

        return this;
    }

    @Override
    public ItemBuilder setUnbreakable(boolean unbreakable) {

        update(library().setUnbreakable(get(), unbreakable));

        return this;
    }

    @Override
    public ItemBuilder setAttribute(AttributeModify.Type type, AttributeModify.Operation operation, double amount) {

        update(library().setAttribute(get(), type, operation, amount));

        return this;
    }

    @Override
    public ItemBuilder setAttribute(AttributeModify.Type type, AttributeModify.Slot slot, AttributeModify.Operation operation, double amount) {

        update(library().setAttribute(get(), type, slot, operation, amount));

        return this;
    }

    @Override
    public ItemBuilder setAttribute(AttributeModify attribute) {

        update(library().setAttribute(get(), attribute));

        return this;
    }

    @Override
    public ItemBuilder setCustomPotion(PotionEffectCustom... effect) {

        update(library().setCustomPotion(get(), effect));

        return this;
    }

    @Override
    public ItemBuilder setCustomPotion(Collection<? extends PotionEffectCustom> effect) {

        update(library().setCustomPotion(get(), effect));

        return this;
    }

    @Override
    public ItemBuilder setCustomPotion(PotionEffectType effectType, int amplifier, int duration) {

        update(library().setCustomPotion(get(), effectType, amplifier, duration));

        return this;
    }

    @Override
    public ItemBuilder setCustomPotion(PotionEffectType effectType, int amplifier, int duration, boolean ambient) {

        update(library().setCustomPotion(get(), effectType, amplifier, duration, ambient));

        return this;
    }

    @Override
    public ItemBuilder setCustomPotion(PotionEffectType effectType, int amplifier, int duration, boolean ambient, boolean showParticles) {

        update(library().setCustomPotion(get(), effectType, amplifier, duration, ambient, showParticles));

        return this;
    }

    @Override
    public ItemStack build() {

        return build(true);
    }

    @Override
    public ItemStack build(boolean safeObj) {

        return safeObj ? get().clone() : get();
    }
}