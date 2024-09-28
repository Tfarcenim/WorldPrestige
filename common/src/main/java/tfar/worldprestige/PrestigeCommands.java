package tfar.worldprestige;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import tfar.worldprestige.network.client.S2CPrestigeScreenPacket;
import tfar.worldprestige.platform.Services;
import tfar.worldprestige.world.PrestigeData;

public class PrestigeCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal(WorldPrestige.MOD_ID)
                .then(Commands.literal("set_boss_ready").requires(commandSourceStack -> commandSourceStack.hasPermission(Commands.LEVEL_ADMINS))
                        .then(Commands.argument("boss_ready", BoolArgumentType.bool())
                                .executes(PrestigeCommands::setBossReady))
                )
                .then(Commands.literal("activate_prestige")
                        .requires(commandSourceStack -> commandSourceStack.hasPermission(Commands.LEVEL_ADMINS))
                        .executes(PrestigeCommands::activatePrestige)
                )
        );
    }

    static int setBossReady(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        CommandSourceStack source = ctx.getSource();
        MinecraftServer server = source.getServer();
        ServerPlayer player = source.getPlayerOrException();
        PrestigeData prestigeData = PrestigeData.getOrCreateDefaultInstance(server);
        boolean bossReady = BoolArgumentType.getBool(ctx, "boss_ready");
        prestigeData.setBossReady(bossReady);
        if (bossReady) {
            player.sendSystemMessage(Component.literal("The final boss is ready to summon...").withStyle(ChatFormatting.ITALIC));
            Services.PLATFORM.postEvent(player);
        }
        return 1;
    }

    static int activatePrestige(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        CommandSourceStack source = ctx.getSource();
        MinecraftServer server = source.getServer();
        ServerPlayer player = source.getPlayerOrException();
        PrestigeData prestigeData = PrestigeData.getOrCreateDefaultInstance(server);
        prestigeData.setReady(true);
        prestigeData.setFightActive(false);
        Services.PLATFORM.sendToClient(new S2CPrestigeScreenPacket(), player);
        return 1;
    }
}
