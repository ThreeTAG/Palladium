package net.threetag.threecore.scripts.accessors;

import net.minecraft.util.math.BlockRayTraceResult;

/**
 * Created by Nictogen on 2020-06-23.
 */
public class BlockRayTraceResultAccessor extends ScriptAccessor<BlockRayTraceResult>
{
	protected BlockRayTraceResultAccessor(BlockRayTraceResult value)
	{
		super(value);
	}

	public Vec3dAccessor getHitVec() {
		return new Vec3dAccessor(this.value.getHitVec());
	}

	public String getType(){
		return this.value.getType().toString();
	}

}
