package com.rcx.aobdorechunks;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;

import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;
import thelm.jaopca.api.JAOPCAApi;
import thelm.jaopca.api.config.IDynamicSpecConfig;
import thelm.jaopca.api.forms.IForm;
import thelm.jaopca.api.forms.IFormRequest;
import thelm.jaopca.api.helpers.IMiscHelper;
import thelm.jaopca.api.items.IItemFormType;
import thelm.jaopca.api.items.IItemInfo;
import thelm.jaopca.api.materials.IMaterial;
import thelm.jaopca.api.materials.MaterialType;
import thelm.jaopca.api.modules.IModule;
import thelm.jaopca.api.modules.IModuleData;
import thelm.jaopca.api.modules.JAOPCAModule;
import thelm.jaopca.items.ItemFormType;
import thelm.jaopca.utils.ApiImpl;
import thelm.jaopca.utils.MiscHelper;

@JAOPCAModule
public class ModuleOreChunks implements IModule {

	public static String processingUnit;
	public static HashMap<String, OreInfos> dropMap = new HashMap<>();
	public static HashMap<IMaterial, FirstOreInfos> oreInformation = new HashMap<>();

	private final IForm oreChunkForm = ApiImpl.INSTANCE.newForm(this, "ore_chunk", ItemFormType.INSTANCE).
			setMaterialTypes(MaterialType.INGOT).setSecondaryName("oreChunk").
			setSettings(ItemFormType.INSTANCE.getNewSettings().
					setItemModelFunctionCreator(OrechunkModelFunctionCreator.INSTANCE));

	@Override
	public String getName() {
		return "orechunks";
	}

	@Override
	public List<IFormRequest> getFormRequests() {
		return Collections.singletonList(oreChunkForm.toRequest());
	}

	@Override
	public void defineModuleConfig(IModuleData moduleData, IDynamicSpecConfig config) {
		processingUnit = config.getDefinedString("processingUnit", "ore", "The ore dictionary prefix for ore chunks, set this to \"dust\" to prevent ore multiplication of ore chunks");
	}

	@Override
	public void defineMaterialConfig(IModuleData moduleData, Map<IMaterial, IDynamicSpecConfig> configs) {
		for(IMaterial material : oreChunkForm.getMaterials()) {
			IDynamicSpecConfig config = configs.get(material);
			String stoneType = "stone";
			if (material.getName().equals("Eximite") || material.getName().equals("Meutoite")) {
				stoneType = "end";
			} else if (material.getName().equals("Cobalt") || material.getName().equals("Ardite") || material.getName().equals("Ignatius") || material.getName().equals("ShadowIron") || material.getName().equals("Lemurite") || material.getName().equals("Midasium") || material.getName().equals("Vyroxeres") || material.getName().equals("Ceruclase") || material.getName().equals("Alduorite") || material.getName().equals("Kalendrite") || material.getName().equals("Vulcanite") || material.getName().equals("Sanguinite")) {
				stoneType = "nether";
			}
			config.getDefinedString("orechunks.stoneType", stoneType, "The stone type for this ore, only has a visual effect");
			oreInformation.put(material, new FirstOreInfos(stoneType,
			config.getDefinedInt("orechunks.dropCount", 1, 1, 100, "The amount of ore chunks dropped by this ore"),
			config.getDefinedInt("orechunks.minXPDrop", 1, 0, 1000, "The minimum amount of XP dropped by this ore"),
			config.getDefinedInt("orechunks.maxXPDrop", 4, 0, 1000, "The maximum amount of XP dropped by this ore")));
		}
	}

	@Override
	public void onMaterialComputeComplete(IModuleData moduleData) {
		for(IMaterial material : oreChunkForm.getMaterials()) {
			Item oreItem = ItemFormType.INSTANCE.getMaterialFormInfo(oreChunkForm, material).asItem();
			OreDictionary.registerOre(processingUnit + material.getName(), oreItem);
			for(String alternativeName : material.getAlternativeNames()) {
				OreDictionary.registerOre(processingUnit + alternativeName, oreItem);
			}
		}
	}

	@Override
	public void onInit(IModuleData moduleData, FMLInitializationEvent event) {
		JAOPCAApi api = ApiImpl.INSTANCE;
		IMiscHelper miscHelper = MiscHelper.INSTANCE;
		IItemFormType itemFormType = ItemFormType.INSTANCE;
		for(IMaterial material : oreChunkForm.getMaterials()) {
			String oreOredict = miscHelper.getOredictName("ore", material.getName());
			IItemInfo oreChunkInfo = itemFormType.getMaterialFormInfo(oreChunkForm, material);
			String oreChunkOredict = miscHelper.getOredictName("oreChunk", material.getName());
			String materialOredict = miscHelper.getOredictName(material.getType().getFormName(), material.getName());

			FirstOreInfos info = oreInformation.get(material);
			OreInfos infos = new OreInfos(oreChunkInfo.asItem(), info.count, info.minXP, info.maxXP);
			dropMap.put(oreOredict, infos);

			api.registerSmeltingRecipe(
					miscHelper.getRecipeKey("orechunks.ore_chunk_to_material", material.getName()),
					oreChunkOredict, materialOredict, 1, 1F);
		}
	}

	@Override
	public Map<String, String> getLegacyRemaps() {
		ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
		builder.put("orechunk", "ore_chunk");
		return builder.build();
	}

	public class FirstOreInfos {
		public String stoneType;
		public int count;
		public int minXP;
		public int maxXP;

		public FirstOreInfos(String stone, int baseCount, int dropXPMin, int dropXPMax) {
			stoneType = stone;
			count = baseCount;
			minXP = dropXPMin;
			maxXP = dropXPMax;
		}
	}

	public class OreInfos {
		public Item chunkItem;
		public int count;
		public int minXP;
		public int maxXP;

		public OreInfos(Item oreChunkItem, int baseCount, int dropXPMin, int dropXPMax) {
			chunkItem = oreChunkItem;
			count = baseCount;
			minXP = dropXPMin;
			maxXP = dropXPMax;
		}
	}
}
