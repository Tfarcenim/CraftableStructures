package tfar.craftablestructures.mixin;

import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Chunk.class)
public class ChunkMixin {
    @ModifyArg(method = "getTopBlockY",at = @At(value = "INVOKE",target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"))
    private Object fixHeightmap(Object old) {
        if (old == Heightmap.Type.OCEAN_FLOOR_WG) {
            return Heightmap.Type.OCEAN_FLOOR;
        }
        return old;
    }
}
