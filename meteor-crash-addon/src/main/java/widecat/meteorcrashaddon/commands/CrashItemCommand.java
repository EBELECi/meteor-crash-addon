package widecat.meteorcrashaddon.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import meteordevelopment.meteorclient.commands.Command;
import net.minecraft.command.CommandSource;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.CreativeInventoryActionC2SPacket;

public class CrashItemCommand extends Command {
    public CrashItemCommand() {
        super("crash-item", "Gives you a crash item.", "citem");
    }

    @Override
    public void build(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            // BASİT VERSİYON - NBT OLMADAN
            ItemStack CrashFireball = new ItemStack(Items.FIREWORK_STAR, 64);
            CreativeInventoryActionC2SPacket balls = new CreativeInventoryActionC2SPacket(0, CrashFireball);
            mc.getNetworkHandler().sendPacket(balls);

            return SINGLE_SUCCESS;
        });
    }
}