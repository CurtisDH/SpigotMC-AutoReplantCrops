package github.curtisdh.autoreplant;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor
{
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        AutoReplant.PrintWithClassName(this,"Reloading Config");
        AutoReplant.Instance.reloadConfig();
        AutoReplant.Instance.LoadConfig();
        sender.sendMessage(ChatColor.GREEN+"Successfully reloaded config");
        return true;
    }
}
