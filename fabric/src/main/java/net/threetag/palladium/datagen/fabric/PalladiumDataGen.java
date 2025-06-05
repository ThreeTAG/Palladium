package net.threetag.palladium.datagen.fabric;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class PalladiumDataGen implements DataGeneratorEntrypoint {

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();

        pack.addProvider(PalladiumLangProvider.English::new);
        pack.addProvider(PalladiumLangProvider.German::new);
        pack.addProvider(PalladiumLangProvider.Saxon::new);
        pack.addProvider(PalladiumModelProvider::new);
        pack.addProvider(DocumentationGenerator::new);
        pack.addProvider(PalladiumBlockTagProvider::new);
        pack.addProvider(AccessorySlotProvider::new);
        pack.addProvider(PalladiumRecipeProvider::new);
    }

}
