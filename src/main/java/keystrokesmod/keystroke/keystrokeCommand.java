package keystrokesmod.keystroke;

import keystrokesmod.Client;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;

public class keystrokeCommand extends CommandBase {
    public String getCommandName() {
        return "keystrokesmod";
    }

    public void processCommand(ICommandSender sender, String[] args) {
        Client.toggleKeyStrokeConfigGui();
    }

    public String getCommandUsage(ICommandSender sender) {
        return "/keystrokesmod";
    }

    public int getRequiredPermissionLevel() {
        return 0;
    }

    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    @Override
    public @NotNull String getName() {
        return "";
    }
    @Override
    public @NotNull String getUsage(@NotNull ICommandSender iCommandSender) {
        return "";
    }
    @Override
    public void execute(@NotNull MinecraftServer minecraftServer, @NotNull ICommandSender iCommandSender, String @NotNull [] strings) throws CommandException {

    }
}
