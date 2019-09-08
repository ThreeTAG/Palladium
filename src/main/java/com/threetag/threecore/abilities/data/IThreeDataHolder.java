package com.threetag.threecore.abilities.data;

/**
 * Created by Nictogen on 2019-06-08.
 */
public interface IThreeDataHolder
{
	void sync(EnumSync sync);

	void setDirty();
}
