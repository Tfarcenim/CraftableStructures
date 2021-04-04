package tfar.craftablestructures;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CraftableStructures.MODID)
public class CraftableStructures
{
    // Directly reference a log4j logger.

    public static final String MODID = "craftablestructures";
    public CraftableStructures() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addGenericListener(Item.class,this::items);
    }

    private void items(RegistryEvent.Register<Item> e) {
        e.getRegistry().register(new StructureItem<>(new Item.Properties().group(ItemGroup.DECORATIONS)).setRegistryName("mini_structure"));
    }
}
