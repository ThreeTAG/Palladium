package net.threetag.palladium.client.renderer.entity.state;

import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

import java.util.Map;

public class SwingAnchorRenderState extends EntityRenderState {

    public LivingEntity owner;
    public Identifier beamRendererId;
    public float partialTick;
    public float opacity = 1F;
    public boolean despawning = false;
    public Map<Object, Vec3> beamAnchors;

}
