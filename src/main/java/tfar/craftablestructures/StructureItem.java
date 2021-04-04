package tfar.craftablestructures;

import tfar.craftablestructures.mixin.StructureAccess;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SChunkDataPacket;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.StructureFeatures;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StructureItem  <C extends IFeatureConfig> extends Item{
    public StructureItem(Properties properties) {
        super(properties);
    }

    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();
        if (!world.isRemote && !player.getCooldownTracker().hasCooldown(this)) {
            ItemStack stack = context.getItem();
            StructureFeature<C,?> structureFeature = getStructureFeatureFromNbt(stack);
            ServerWorld serverWorld = (ServerWorld)world;
            DynamicRegistries dynamicRegistries = world.getServer().getDynamicRegistries();
            ChunkGenerator chunkGenerator = serverWorld.getChunkProvider().generator;
            BlockPos pos = context.getPos();
            ChunkPos chunkPos = new ChunkPos(pos);

            int refCount = 1;
            //structureFeature.field_236268_b_.createStructureStart(pos.x, pos.z, MutableBoundingBox.getNewBoundingBox(), refCount, seed);
            StructureStart<C> start = ((StructureAccess<C>)structureFeature.field_236268_b_).$createStructureStart(chunkPos.x, chunkPos.z,
                    MutableBoundingBox.getNewBoundingBox(), refCount, serverWorld.getSeed());
            start.func_230364_a_(dynamicRegistries, chunkGenerator, serverWorld.getStructureTemplateManager(), chunkPos.x, chunkPos.z, serverWorld.getBiome(pos), structureFeature.config);
            List<StructurePiece> pieces = start.getComponents();

            Set<ChunkPos> updateList = new HashSet<>();

            for (StructurePiece piece : pieces) {

                MutableBoundingBox bb = piece.getBoundingBox();
                 piece.func_230383_a_(serverWorld,serverWorld.getStructureManager(),chunkGenerator,world.rand,piece.getBoundingBox(),chunkPos,pos);

                 ChunkPos chunk1 = new ChunkPos(new BlockPos(bb.minX,bb.minY,bb.minZ));

                 ChunkPos chunk2 = new ChunkPos(new BlockPos(bb.maxX,bb.maxY,bb.maxZ));

                 for (int x = chunk1.x;x <= chunk2.x;x++ ) {
                     for (int z = chunk1.z;z <= chunk2.z;z++ ) {
                         updateList.add(new ChunkPos(x,z));
                     }
                 }

                 for (ChunkPos chunkToClear : updateList) {

                     serverWorld.getChunkProvider().chunkManager
                             .getTrackingPlayers(chunkToClear, false)
                             .forEach(s -> s.connection.sendPacket(new SChunkDataPacket(world.getChunk(chunkToClear.x,chunkToClear.z), 65535)));
                 }

            }
            player.getCooldownTracker().setCooldown(this,20);
        }
        return super.onItemUse(context);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        super.addInformation(stack, worldIn, tooltip, flagIn);
        if (stack.hasTag()) {
            tooltip.add(new StringTextComponent(stack.getTag().getString("configured_structure_feature")));
        }
        else {
            tooltip.add(new StringTextComponent("mineshaft"));
        }
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        super.fillItemGroup(group, items);
        for (StructureFeature<?,?> structureFeature : WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE) {
            ItemStack stack = new ItemStack(this);
            ResourceLocation rl = WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE.getKey(structureFeature);
            CompoundNBT nbt = new CompoundNBT();
            nbt.putString("configured_structure_feature",rl.toString());
            stack.setTag(nbt);
            items.add(stack);
        }
    }

    public StructureFeature<C,?> getStructureFeatureFromNbt(ItemStack stack) {
        if (stack.hasTag() && stack.getTag().contains("configured_structure_feature", Constants.NBT.TAG_STRING)) {
            return (StructureFeature<C, ?>) WorldGenRegistries.CONFIGURED_STRUCTURE_FEATURE
                    .getOptional(new ResourceLocation(stack.getTag().getString("configured_structure_feature"))).orElse(StructureFeatures.MINESHAFT);
        }
        return (StructureFeature<C, ?>) StructureFeatures.MINESHAFT;
    }
}
