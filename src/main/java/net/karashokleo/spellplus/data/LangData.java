package net.karashokleo.spellplus.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.karashokleo.spellplus.item.AllItems;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class LangData extends FabricLanguageProvider
{
    public LangData(FabricDataOutput output)
    {
        super(output);
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder)
    {
        translationBuilder.add(AllItems.GROUP_KEY, "Spell Plus");

        translationBuilder.add("text.spell-plus.success", "Successful use of essence!");
        translationBuilder.add("text.spell-plus.failure", "Failed to use essence!");
        translationBuilder.add("text.spell-plus.use", "When in Main Hand, press the right button to apply effect on the item in Off Hand.");
        translationBuilder.add("text.spell-plus.effect", "Effect:");
        translationBuilder.add("text.spell-plus.upgrade", "Upgrade %s to %s");
        translationBuilder.add("text.spell-plus.threshold", "Threshold: %s");

        translationBuilder.add("text.spell-plus.mainhand", "Slot: Main Hand");
        translationBuilder.add("text.spell-plus.offhand", "Slot: Off Hand");
        translationBuilder.add("text.spell-plus.head", "Slot: Head");
        translationBuilder.add("text.spell-plus.chest", "Slot: Chest");
        translationBuilder.add("text.spell-plus.legs", "Slot: Legs");
        translationBuilder.add("text.spell-plus.feet", "Slot: Feet");
        translationBuilder.add("text.spell-plus.modifier","Modifier: ");

        AllItems.ALL.forEach(item ->
                translationBuilder.add(item, getDefaultName(item)));
    }

    private String getDefaultName(Item item)
    {
        return getDefaultName(Registries.ITEM.getId(item));
    }

    private String getDefaultName(Identifier id)
    {
        String[] words = id.getPath().split("_");
        StringBuilder sb = new StringBuilder();
        for (String word : words)
            if (!word.isEmpty())
                sb.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase())
                        .append(" ");
        return sb.toString().trim();
    }
}
