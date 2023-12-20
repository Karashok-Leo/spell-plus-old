package net.karashokleo.spellplus.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.karashokleo.spellplus.SpellPlus;
import net.karashokleo.spellplus.data.RecipeData;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.spell_engine.api.item.trinket.SpellBookItem;
import net.spell_engine.api.item.trinket.SpellBooks;
import net.spell_power.api.MagicSchool;
import net.spell_power.api.attributes.EntityAttributes_SpellPower;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AllItems
{
    public static final List<MagicSchool> SCHOOLS = Arrays.stream(MagicSchool.values()).filter(magicSchool -> magicSchool.isMagical).toList();
    public static final String[] TIERS = {"primary_", "secondary_", "advanced_"};
    public static final List<Item> ALL = new ArrayList<>();

    public static final RegistryKey<ItemGroup> GROUP_KEY = RegistryKey.of(Registries.ITEM_GROUP.getKey(), SpellPlus.modLoc("spellplus_group"));

    public static void register()
    {
        SCHOOLS.forEach(school ->
        {
            registerEssence(0, school);
            registerEssence(1, school);
            registerEssence(2, school);
            SpellBookItem primary_book = registerSpellBook(0, school);
            SpellBookItem secondary_book = registerSpellBook(1, school);
            SpellBookItem advanced_book = registerSpellBook(2, school);
            registerUpgradeEssence(0, school, primary_book, secondary_book);
            registerUpgradeEssence(1, school, secondary_book, advanced_book);
            for (EquipmentSlot slot : EquipmentSlot.values())
            {
                registerEnchantedEssence(0, school, 10, slot);
                registerEnchantedEssence(1, school, 20, slot);
                registerEnchantedEssence(2, school, 30, slot);
            }
        });

        Registry.register(Registries.ITEM_GROUP, SpellPlus.modLoc("spellplus_group"),
                FabricItemGroup
                        .builder()
                        .icon(() -> new ItemStack(ALL.get(0)))
                        .displayName(Text.translatable("itemGroup.spellplus.group"))
                        .build()
        );

        ItemGroupEvents.modifyEntriesEvent(GROUP_KEY).register(entries ->
                ALL.forEach(item ->
                        entries.add(item.getDefaultStack())));
    }

    public static void registerItem(String id, Item item)
    {
        ALL.add(item);
        Registry.register(Registries.ITEM, SpellPlus.modLoc(id), item);
    }

    public static void registerEssence(int tier, MagicSchool school)
    {
        Identifier itemId = SpellPlus.modLoc(TIERS[tier] + school.spellName() + "_essence");
        Item item = new Item(new FabricItemSettings());
        ALL.add(item);
        RecipeData.INGREDIENTS.put(Pair.of(tier, school), item);
        Registry.register(Registries.ITEM, itemId, item);
    }

    public static SpellBookItem registerSpellBook(int tier, MagicSchool school)
    {
        Identifier poolId = SpellPlus.modLoc(TIERS[tier] + school.spellName());
        SpellBookItem book = SpellBooks.create(poolId);
        ALL.add(book);
        Registry.register(Registries.ITEM, SpellBooks.itemIdFor(poolId), book);
        return book;
    }

    public static void registerUpgradeEssence(int tier, MagicSchool school, Item originItem, Item upgradeItem)
    {
        Identifier itemId = SpellPlus.modLoc(TIERS[tier] + school.spellName() + "_upgrade_essence");
        UpgradeEssenceItem upgradeEssence = new UpgradeEssenceItem(new FabricItemSettings().maxCount(1), tier, school, originItem, upgradeItem, TIERS[tier] + school.spellName() + "_upgrade");
        ALL.add(upgradeEssence);
        Registry.register(Registries.ITEM, itemId, upgradeEssence);
    }

    public static void registerEnchantedEssence(int tier, MagicSchool school, double cap, EquipmentSlot slot)
    {
        Identifier itemId = SpellPlus.modLoc(TIERS[tier] + school.spellName() + "_" + slot.getName() + "_enchanted_essence");
        EntityAttribute attribute = EntityAttributes_SpellPower.POWER.get(school);
        EnchantedEssenceItem enchantedEssence = new EnchantedEssenceItem(new FabricItemSettings().maxCount(1), tier, school, new ExtraModifier(cap, slot, attribute));
        ALL.add(enchantedEssence);
        RecipeData.ESSENCES.put(Triple.of(tier, school, slot), enchantedEssence);
        Registry.register(Registries.ITEM, itemId, enchantedEssence);
    }
}
