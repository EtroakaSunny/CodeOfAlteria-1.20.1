package net.dorikku.codeofalteria;

import com.mojang.logging.LogUtils;
import net.dorikku.codeofalteria.item.ModItems;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.awt.*;

import static net.minecraft.world.entity.EntityType.PLAYER;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CodeOfAlteriaMod.MOD_ID)
public class CodeOfAlteriaMod {
    public static final String MOD_ID = "codeofalteria";
    public static final Logger LOGGER = LogUtils.getLogger();

    public CodeOfAlteriaMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModCreativeModTabs.register(modEventBus);

        ModItems.register(modEventBus);

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {

    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }


    //Subscribe to the event when a user tries to enter the nether. Event happens when user steps into the portal
    @SubscribeEvent
    public void onPlayerChangeDim(EntityTravelToDimensionEvent event) {
        //Test if a player is trying
        if (event.getEntity() instanceof Player && event.getDimension() == Level.NETHER) {

            //Test if the user has a valid ticket
            if (((Player) event.getEntity()).getInventory().contains(ModItems.VALID_TICKET.get().getDefaultInstance())) {

                //If the user has a valid ticket, replace the ticket with a used ticket
                for (int i = 0; i < ((Player) event.getEntity()).getInventory().getContainerSize(); i++) {
                    if (((Player) event.getEntity()).getInventory().getItem(i).getItem() == ModItems.VALID_TICKET.get()) {
                        ((Player) event.getEntity()).getInventory().removeItem(i, 1);
                        ((Player) event.getEntity()).getInventory().add(ModItems.USED_TICKET.get().getDefaultInstance());
                        break;
                    }
                }
                event.setCanceled(false);

            } else {
                //If the user does not have a valid ticket, cancel the event
                event.setCanceled(true);
            }

        }
    }


    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

        }
    }
}