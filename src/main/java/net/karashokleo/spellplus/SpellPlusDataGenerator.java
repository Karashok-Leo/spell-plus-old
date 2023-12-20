package net.karashokleo.spellplus;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.karashokleo.spellplus.data.LangData;
import net.karashokleo.spellplus.data.ModelData;
import net.karashokleo.spellplus.data.RecipeData;

public class SpellPlusDataGenerator implements DataGeneratorEntrypoint
{
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator)
    {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(ModelData::new);
        pack.addProvider(LangData::new);
        pack.addProvider(RecipeData::new);
    }
}
