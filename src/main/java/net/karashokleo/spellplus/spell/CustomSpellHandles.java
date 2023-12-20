package net.karashokleo.spellplus.spell;

import net.karashokleo.spellplus.SpellPlus;
import net.spell_engine.api.spell.CustomSpellHandler;

public class CustomSpellHandles
{
    public static void register()
    {
        CustomSpellHandler.register(SpellPlus.modLoc("custom_spell"), (data) -> {
            return true;
        });
    }
}
