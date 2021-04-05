package tfar.craftablestructures.datagen;

import net.minecraft.data.DataGenerator;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;
import tfar.craftablestructures.datagen.data.ModRecipeProvider;

public class DatagenMain {

    public static void start(GatherDataEvent e) {
        DataGenerator generator = e.getGenerator();
        if (e.includeServer()) {
            generator.addProvider(new ModRecipeProvider(generator));
        }
    }
}
