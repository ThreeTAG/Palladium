package net.threetag.palladium.mixin.client;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.threetag.palladium.client.renderer.entity.ExtendedEntityRenderState;
import net.threetag.palladium.logic.context.DataContextType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.Map;

@Mixin(EntityRenderState.class)
public class EntityRenderStateMixin implements ExtendedEntityRenderState {

    @Unique
    private final Map<DataContextType<?>, Object> palladium$values = new HashMap<>();

    @Override
    public <T> void palladium$addData(DataContextType<T> type, T value) {
        this.palladium$values.put(type, value);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T palladium$getData(DataContextType<T> type) {
        return (T) this.palladium$values.get(type);
    }

    @Override
    public boolean palladium$hasData(DataContextType<?> type) {
        return this.palladium$values.containsKey(type);
    }

    @Override
    public void palladium$resetData() {
        this.palladium$values.clear();
    }
}
