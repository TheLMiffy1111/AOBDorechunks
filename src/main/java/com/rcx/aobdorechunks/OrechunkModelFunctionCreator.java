package com.rcx.aobdorechunks;

import java.util.Collections;
import java.util.Set;
import java.util.function.Function;

import org.apache.commons.lang3.tuple.Pair;

import com.rcx.aobdorechunks.ModuleOreChunks.FirstOreInfos;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import thelm.jaopca.api.items.IItemFormSettings;
import thelm.jaopca.api.items.IItemModelFunctionCreator;
import thelm.jaopca.api.items.IMaterialFormItem;
import thelm.jaopca.utils.MiscHelper;

public class OrechunkModelFunctionCreator implements IItemModelFunctionCreator {

	public static final OrechunkModelFunctionCreator INSTANCE = new OrechunkModelFunctionCreator();

	@Override
	public Pair<Function<ItemStack, ModelResourceLocation>, Set<ModelResourceLocation>> create(IMaterialFormItem item, IItemFormSettings settings) {
		ResourceLocation baseModelLocation = getBaseModelLocation(item);
		ModelResourceLocation modelLocation = new ModelResourceLocation(baseModelLocation, "inventory");
		return Pair.of(s->modelLocation, Collections.singleton(modelLocation));
	}

	public ResourceLocation getBaseModelLocation(IMaterialFormItem materialFormItem) {
		String stoneType = "stone";
		FirstOreInfos info = ModuleOreChunks.oreInformation.get(materialFormItem.getMaterial());
		stoneType = info.stoneType;
		Item item = materialFormItem.toItem();
		ResourceLocation location = item.getRegistryName();
		ResourceLocation location1 = new ResourceLocation(location.getResourceDomain(), "blockstates/"+location.getResourcePath()+".json");
		ResourceLocation location2 = new ResourceLocation(location.getResourceDomain(), "models/item/"+location.getResourcePath()+".json");
		if(MiscHelper.INSTANCE.hasResource(location1) || MiscHelper.INSTANCE.hasResource(location2)) {
			return location;
		}
		else {
			return new ResourceLocation("jaopcaoc:orechunk_" + stoneType);
		}
	}
}
