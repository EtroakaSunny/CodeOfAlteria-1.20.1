package net.dorikku.codeofalteria.item;


import net.dorikku.codeofalteria.CodeOfAlteriaMod;
import net.dorikku.codeofalteria.item.custom.DayTicketItem;
import net.dorikku.codeofalteria.item.custom.FuelItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;


public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CodeOfAlteriaMod.MOD_ID);


    public static final RegistryObject<Item> VALID_TICKET = ITEMS.register("valid_ticket", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> USED_TICKET = ITEMS.register("used_ticket", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> DAY_TICKET = ITEMS.register("day_ticket", () -> new DayTicketItem(new Item.Properties()
            .stacksTo(1)
            ));
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

}
