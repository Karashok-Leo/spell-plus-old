package net.karashokleo.spellplus.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.recipe.book.RecipeCategory;
import net.spell_power.api.MagicSchool;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class RecipeData extends FabricRecipeProvider
{
    public static final Map<Pair<Integer, MagicSchool>, Item> INGREDIENTS = new HashMap<>();
    public static final Map<Triple<Integer, MagicSchool, EquipmentSlot>, Item> ESSENCES = new HashMap<>();
    public static final Map<EquipmentSlot, String[]> PATTERNS = Map.of(
            EquipmentSlot.MAINHAND, new String[]{" ##", "###", "## "},
            EquipmentSlot.OFFHAND, new String[]{"## ", "###", " ##"},
            EquipmentSlot.HEAD, new String[]{"###", "# #", "   "},
            EquipmentSlot.CHEST, new String[]{"# #", "###", "###"},
            EquipmentSlot.LEGS, new String[]{"###", "# #", "# #"},
            EquipmentSlot.FEET, new String[]{"   ", "# #", "# #"}
    );

    public RecipeData(FabricDataOutput output)
    {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> exporter)
    {
        ESSENCES.forEach((triple, item) ->
        {
            Item ingredient = INGREDIENTS.get(Pair.of(triple.getLeft(), triple.getMiddle()));
            ShapedRecipeJsonBuilder
                    .create(RecipeCategory.MISC, item)
                    .pattern(PATTERNS.get(triple.getRight())[0])
                    .pattern(PATTERNS.get(triple.getRight())[1])
                    .pattern(PATTERNS.get(triple.getRight())[2])
                    .input('#', ingredient)
                    .criterion(
                            FabricRecipeProvider.hasItem(ingredient),
                            FabricRecipeProvider.conditionsFromItem(ingredient)
                    )
                    .offerTo(exporter);
        });
    }
}
