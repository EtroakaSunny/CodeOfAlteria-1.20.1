package net.dorikku.codeofalteria.item.custom;


import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.network.chat.Component;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.ZoneOffset;


public class DayTicketItem extends Item {

    public DayTicketItem(Properties properties) {
        super(properties);
    }



    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(Level pLevel, @NotNull Player pPlayer, @NotNull InteractionHand pUsedHand) {
        if (!pLevel.isClientSide) {
            ItemStack itemStack = pPlayer.getItemInHand(pUsedHand);
            CompoundTag tag = itemStack.getOrCreateTag();

            // Check if the "savedTime" and "savedDay" NBT entries exist
            if (!tag.contains("savedTime") || !tag.contains("savedDay")) {
                // Save the current server time in seconds since epoch and the current day
                long currentTime = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
                int currentDay = LocalDateTime.now().getDayOfMonth();
                tag.putLong("savedTime", currentTime);
                tag.putInt("savedDay", currentDay);

                // Notify the player that the time has been saved
                pPlayer.displayClientMessage(Component.translatable("message.day_ticket.saved_time"), true);
            } else {
                // Retrieve the saved time and day from NBT
                int savedDay = tag.getInt("savedDay");
                LocalDateTime now = LocalDateTime.now();
                long currentTime = now.toEpochSecond(ZoneOffset.UTC);
                int currentDay = now.getDayOfMonth();

                // Calculate 1 AM of the next calendar day in seconds since epoch
                long next1AMSeconds = getNext1AMSeconds(now);

                // Check if the ticket is still valid based on the saved day and time
                if (currentDay != savedDay || currentTime >= next1AMSeconds) {
                    // Mark as invalid and notify the player
                    pPlayer.displayClientMessage(Component.translatable("message.day_ticket.invalid"), true);
                    return InteractionResultHolder.fail(itemStack);
                } else {
                    // Calculate remaining time until 1 AM
                    long secondsUntilNext1AM = next1AMSeconds - currentTime;
                    long hours = secondsUntilNext1AM / 3600;
                    long minutes = (secondsUntilNext1AM % 3600) / 60;

                    // Notify the player of the remaining time
                    pPlayer.displayClientMessage(
                            Component.translatable("message.day_ticket.remaining_time", hours, minutes),
                            true
                    );
                }
            }
        }
        return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand));
    }

    private static long getNext1AMSeconds(LocalDateTime now) {
        LocalDateTime next1AM;

        if (now.getHour() >= 22) {
            // If it's 22:00 or later, calculate 1 AM of the day after tomorrow
            next1AM = now.plusDays(2).withHour(1).withMinute(0).withSecond(0).withNano(0);
        } else if (now.getHour() >= 1) {
            // If it's 1 AM or later, calculate 1 AM of the next day
            next1AM = now.plusDays(1).withHour(1).withMinute(0).withSecond(0).withNano(0);
        } else {
            // If it's before 1 AM, calculate 1 AM of today
            next1AM = now.withHour(1).withMinute(0).withSecond(0).withNano(0);
        }

        return next1AM.toEpochSecond(ZoneOffset.UTC);
    }

}
