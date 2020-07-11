package net.threetag.threecore.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.EntityTypeTagsProvider;
import net.minecraft.entity.EntityType;
import net.threetag.threecore.entity.TCEntityTypes;
import net.threetag.threecore.tags.TCEntityTypeTags;

public class ThreeCoreEntityTypeTagsProvider extends EntityTypeTagsProvider {

    public ThreeCoreEntityTypeTagsProvider(DataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void registerTags() {
        this.func_240522_a_(TCEntityTypeTags.ARMOR_STANDS).func_240534_a_(EntityType.ARMOR_STAND).func_240534_a_(TCEntityTypes.SUIT_STAND.get());
    }

    @Override
    public String getName() {
        return "ThreeCore Entity Type Tags";
    }

}
