package tfar.craftablestructures;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import tfar.craftablestructures.datagen.DatagenMain;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CraftableStructures.MODID)
public class CraftableStructures
{
    // Directly reference a log4j logger.

    public static final String MODID = "craftablestructures";
    public CraftableStructures() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addGenericListener(Item.class,this::items);
        bus.addListener(DatagenMain::start);
    }

    public static final Item MINI_STRUCTURE = new StructureItem<>(new Item.Properties().group(ItemGroup.DECORATIONS));

    private void items(RegistryEvent.Register<Item> e) {
        e.getRegistry().register(MINI_STRUCTURE.setRegistryName("mini_structure"));
    }
}
