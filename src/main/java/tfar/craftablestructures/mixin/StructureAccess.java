package tfar.craftablestructures.mixin;

import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Structure.class)
public interface StructureAccess<C extends IFeatureConfig>  {
    @Invoker("createStructureStart")
    StructureStart<C> $createStructureStart(int p_236387_1_, int p_236387_2_, MutableBoundingBox p_236387_3_, int refCount, long seed);
}
