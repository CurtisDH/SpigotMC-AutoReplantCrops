package github.curtisdh.autoreplant;

import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class AutoReplant extends JavaPlugin
{
    public static AutoReplant Instance;
    private onBlockBreakEvent blockBreakEvent;

    @Override
    public void onEnable()
    {
        // Plugin startup logic
        PrintWithClassName(this, "Starting...");
        blockBreakEvent = new onBlockBreakEvent();
        Instance = this;
        LoadConfig();
        SetupCommands();
        EventsSetup();
        PrintWithClassName(this, "Done...");
    }

    @Override
    public void onDisable()
    {
        // Plugin shutdown logic
    }

    private void SetupCommands()
    {
        PrintWithClassName(this,"Setting up commands...");
        getCommand("reloadConfig").setExecutor(new ReloadCommand());
    }

    private void EventsSetup()
    {
        PrintWithClassName(this,"Setting up Events...");
        getServer().getPluginManager().registerEvents(blockBreakEvent,this);
    }

    public void LoadConfig()
    {
        PrintWithClassName(this, "Loading Config...");
        saveDefaultConfig();
        Map<String, BlockData> blockData = new HashMap<>();
        Map<String,Boolean> toolData = new HashMap<>();
        for (String key : getConfig().getConfigurationSection("settings.blocks").getKeys(false))
        {
            BlockData data;
            Object autoReplantObj = getConfig().getConfigurationSection("settings.blocks."+key)
                    .getValues(true).get("autoReplant");
            Object hoeRequiredObj = getConfig().getConfigurationSection("settings.blocks."+key)
                    .getValues(true).get("hoeRequired");

            boolean autoReplantBool = autoReplantObj.toString().equalsIgnoreCase("true");
            boolean hoeRequiredBool = hoeRequiredObj.toString().equalsIgnoreCase("true");
            key = key.toUpperCase(Locale.ROOT);
            data = new BlockData(autoReplantBool,hoeRequiredBool);
            PrintWithClassName(this,"Loading Config:"+key+" "
                    +"AutoReplant:"+data.AutoReplant+" HoeRequired:"+data.HoeRequired);
            blockData.put(key,data);
        }
        for (String key : getConfig().getConfigurationSection("settings.tools").getKeys(false))
        {
            Object validObj = getConfig().getConfigurationSection("settings.tools."+key)
                    .getValues(true).get("valid");

            boolean validBool = validObj.toString().equalsIgnoreCase("true");
            key = key.toUpperCase(Locale.ROOT);
            toolData.put(key,validBool);

        }
        blockBreakEvent.SetBlockDataMap(blockData);
        blockBreakEvent.SetToolData(toolData);
        saveConfig();
        PrintWithClassName(this, "-Loaded Config-");
    }

    public static void PrintWithClassName(Object ClassObject, String str)
    {
        String response = ClassObject.getClass().getName() + "::" + str;
        System.out.println(response);
    }
}
