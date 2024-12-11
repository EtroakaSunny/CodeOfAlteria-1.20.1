package net.dorikku.codeofalteria.event;

import net.dorikku.codeofalteria.item.ModItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.time.LocalDateTime;

public class DimensionChangeEventHandler {

    @SubscribeEvent
    public void onPlayerChangeDim(EntityTravelToDimensionEvent event) {
        // Check if the entity is a player
        if (event.getEntity() instanceof Player player) {
            // Check if the player is trying to enter the Nether
            if (event.getDimension().equals(Level.NETHER)) {
                boolean hasValidTicket = false;


                for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                    ItemStack stack = player.getInventory().getItem(i);

                    // Check if the item is a Day Ticket
                    if (stack.getItem() == ModItems.DAY_TICKET.get()) {
                        CompoundTag tag = stack.getOrCreateTag();

                        // Validate the Day Ticket
                        if (tag.contains("savedTime") && tag.contains("savedDay")) {
                            int savedDay = tag.getInt("savedDay");
                            LocalDateTime now = LocalDateTime.now();

                            // Calculate 1 AM of the next day
                            LocalDateTime next1AM;
                            if (now.getHour() >= 1) {
                                next1AM = now.plusDays(1).withHour(1).withMinute(0).withSecond(0).withNano(0);
                            } else {
                                next1AM = now.withHour(1).withMinute(0).withSecond(0).withNano(0);
                            }

                            // Check if the Day Ticket is still valid
                            if (savedDay == now.getDayOfMonth() && now.isBefore(next1AM)) {
                                hasValidTicket = true;
                                break; // Stop checking once a valid Day Ticket is found
                            }
                        }
                    }
                }

                if (!hasValidTicket) {
                    // Check for valid tickets in inventory and replace with used tickets
                    for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                        ItemStack stack = player.getInventory().getItem(i);
                        if (stack.getItem() == ModItems.VALID_TICKET.get()) {
                            hasValidTicket = true;
                            // Remove one valid ticket
                            player.getInventory().removeItem(i, 1);
                            // Add one used ticket
                            player.getInventory().add(ModItems.USED_TICKET.get().getDefaultInstance());
                            break;
                        }
                    }
                }

                // If no valid ticket was found, check for day tickets


                MobEffect heatResist = getHeatResist();
                // Cancel event if no valid or day ticket exists
                if (!hasValidTicket) {
                    player.displayClientMessage(Component.translatable("message.day_ticket.invalid"), true);
                    event.setCanceled(true);
                } else {
                    // Give Player heat resistance from Legendary Survival Overhaul
                    assert heatResist != null;
                    player.addEffect(new MobEffectInstance(heatResist, 60000, 0, false, false, true));
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerReturnDim(EntityTravelToDimensionEvent event) {
        // Check if the entity is a player
        if (event.getEntity() instanceof Player player) {
            // Check if the player is trying to enter the Overworld
            if (event.getDimension().equals(Level.OVERWORLD)) {
                MobEffect heatResist = getHeatResist();
                // Remove heat resistance when returning to the Overworld
                assert heatResist != null;
                player.removeEffect(heatResist);
            }
        }
    }

    private static MobEffect getHeatResist() {
        return ForgeRegistries.MOB_EFFECTS.getValue(new ResourceLocation("legendarysurvivaloverhaul", "heat_resist"));
    }
}
