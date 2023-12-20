package net.karashokleo.spellplus;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.ModifyItemAttributeModifiersCallback;
import net.karashokleo.spellplus.item.AllItems;
import net.karashokleo.spellplus.item.ExtraModifier;
import net.karashokleo.spellplus.spell.CustomSpellHandles;
import net.minecraft.util.Identifier;

public class SpellPlus implements ModInitializer
{
    public static final String MOD_ID = "spell-plus";

    @Override
    public void onInitialize()
    {
        AllItems.register();
        CustomSpellHandles.register();
        ModifyItemAttributeModifiersCallback.EVENT.register(ExtraModifier::addModifiers);
    }

    public static Identifier modLoc(String id)
    {
        return new Identifier(MOD_ID, id);
    }
}
