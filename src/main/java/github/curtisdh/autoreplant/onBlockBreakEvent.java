package github.curtisdh.autoreplant;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Collection;
import java.util.Map;

public class onBlockBreakEvent implements Listener
{
    private Map<String, BlockData> blockDataMap;
    private Map<String, Boolean> toolDataMap;

    @EventHandler
    void BreakEvent(BlockBreakEvent event)
    {
        AutoReplant.PrintWithClassName(this, event.getBlock().toString());
        Block block = event.getBlock();
        Material blockType = block.getType();
        Player player = event.getPlayer();
        for (Map.Entry<String, BlockData> blockDataEntries : blockDataMap.entrySet())
        {
            if (Material.valueOf(blockDataEntries.getKey()) == blockType)
            {
                if (blockDataEntries.getValue().HoeRequired)
                {
                    if (IsValidTool(player, toolDataMap))
                    {
                        Replant(block,event);
                    }
                    // If the player didn't use a hoe, do nothing
                    return;
                }
                //No hoe required
                Replant(block,event);
            }
        }

    }

    private void Replant(Block block, BlockBreakEvent event)
    {
        org.bukkit.block.data.BlockData bdata = block.getBlockData();
        Ageable age = (Ageable) bdata;
        Collection<ItemStack> drops = block.getDrops();
        World world = block.getWorld();
        Location location = block.getLocation();
        if (age.getAge() == age.getMaximumAge())
        {
            for(ItemStack item : drops)
            {
                world.dropItemNaturally(location,item);
            }
            age.setAge(0);
            block.setBlockData(age);
            event.setCancelled(true);
        }
        if(age.getAge() < age.getMaximumAge())
        {
            event.setCancelled(true);
        }
        return;
    }

    private boolean IsValidTool(Player player, Map<String, Boolean> toolDataMap)
    {
        Integer itemSlotID = player.getInventory().getHeldItemSlot();
        Material heldItem = player.getInventory().getItem(itemSlotID).getType();
        for (Map.Entry<String, Boolean> toolDataEntries : toolDataMap.entrySet())
        {
            if(heldItem == null)
            {
                return false;
            }
            if (Material.valueOf(toolDataEntries.getKey()) == heldItem)
            {
                if (toolDataEntries.getValue() == true)
                {
                    return true;
                }
            }
        }
        return false;
    }

    public void SetBlockDataMap(Map<String, BlockData> map)
    {
        blockDataMap = map;
    }

    public void SetToolData(Map<String, Boolean> map)
    {
        toolDataMap = map;
    }
}
