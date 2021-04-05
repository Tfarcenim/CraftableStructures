package tfar.craftablestructures.datagen.data;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.StructureFeatures;
import tfar.craftablestructures.CraftableStructures;
import tfar.craftablestructures.datagen.ItemStackShapedRecipeBuilder;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(DataGenerator generatorIn) {
        super(generatorIn);
    }


    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        buildRecipeForStructure(Items.PRISMARINE,Items.GOLD_BLOCK, StructureFeatures.MONUMENT,consumer);
        buildRecipeForStructure(Items.NETHER_BRICKS,Items.GOLD_BLOCK, StructureFeatures.FORTRESS,consumer);
        buildRecipeForStructure(Items.SPRUCE_LOG,Items.CAULDRON, StructureFeatures.SWAMP_HUT,consumer);
        buildRecipeForStructure(Items.DARK_OAK_LOG,Items.BIRCH_PLANKS, StructureFeatures.PILLAGER_OUTPOST,consumer);
        buildRecipeForStructure(Items.STONE_BRICKS,Items.ENDER_EYE, StructureFeatures.STRONGHOLD,consumer);
        buildRecipeForStructure(Items.OBSIDIAN,Items.NETHERRACK, StructureFeatures.RUINED_PORTAL,consumer);
        buildRecipeForStructure(Items.COBBLESTONE,Items.OAK_PLANKS, StructureFeatures.VILLAGE_PLAINS,consumer);
        buildRecipeForStructure(Items.MOSSY_COBBLESTONE,Items.CHISELED_STONE_BRICKS, StructureFeatures.JUNGLE_PYRAMID,consumer);
        //note, requires overworld >= 60
        buildRecipeForStructure(Items.END_STONE,Items.CHORUS_FRUIT, StructureFeatures.END_CITY,consumer);

        buildRecipeForStructure(Items.DARK_OAK_LOG,Items.EMERALD, StructureFeatures.MANSION,consumer);
        buildRecipeForStructure(Items.POLISHED_BLACKSTONE_BRICKS,Items.BASALT, StructureFeatures.BASTION_REMNANT,consumer);

    }

    protected static void buildRecipeForStructure(IItemProvider ring, IItemProvider center, StructureFeature<?,?> structure, Consumer<IFinishedRecipe> consumer) {
        buildRecipeForStructure(ring, center, WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE.getKey(structure), consumer);
    }

    protected static void buildRecipeForStructure(IItemProvider ring, IItemProvider center, ResourceLocation structure, Consumer<IFinishedRecipe> consumer) {
        ItemStack mini = new ItemStack(CraftableStructures.MINI_STRUCTURE);
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("configured_structure_feature",structure.toString());
        mini.setTag(nbt);
        ringAroundItem(ring,center,mini,structure,consumer);
    }

    protected static void ringAroundItem(IItemProvider ringItem, IItemProvider centerItem, ItemStack result,ResourceLocation structure, Consumer<IFinishedRecipe> consumer) {
        ItemStackShapedRecipeBuilder.shapedRecipe(result)
                .key('a',ringItem).key('b',centerItem)
                .patternLine("aaa").patternLine("aba").patternLine("aaa").setGroup(CraftableStructures.MODID)
                .addCriterion("has_cobble", hasItem(Items.COBBLESTONE)).build(consumer,new ResourceLocation(CraftableStructures.MODID,structure.getPath()));
    }
}
