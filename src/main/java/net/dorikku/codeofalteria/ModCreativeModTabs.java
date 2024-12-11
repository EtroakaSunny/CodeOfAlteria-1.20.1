package net.dorikku.codeofalteria;

import net.dorikku.codeofalteria.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CodeOfAlteriaMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> ALTERIA_TAB = CREATIVE_MODE_TABS.register("alteria_tab", () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.VALID_TICKET.get()))
            .title(Component.translatable("creativetab.codeofalteria_tab"))
            .displayItems(((itemDisplayParameters, output) -> {
                output.accept(new ItemStack(ModItems.VALID_TICKET.get()));
                output.accept(new ItemStack(ModItems.USED_TICKET.get()));
                output.accept(new ItemStack(ModItems.DAY_TICKET.get()));
            }))
            .build());


    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }

}
