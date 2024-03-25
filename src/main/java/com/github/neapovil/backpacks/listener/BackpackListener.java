package com.github.neapovil.backpacks.listener;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;

import com.destroystokyo.paper.event.server.ServerTickEndEvent;
import com.destroystokyo.paper.event.server.ServerTickStartEvent;
import com.github.neapovil.backpacks.Backpacks;
import com.github.neapovil.backpacks.gson.BackpackInventoryGson;
import com.github.neapovil.backpacks.inventory.BackpackInventory;

public final class BackpackListener implements Listener
{
    private final Backpacks plugin = Backpacks.instance();
    private final Map<UUID, BackpackInventory> backpacks = new HashMap<>();

    @EventHandler
    private void onPlayerInteractItem(PlayerInteractEvent event)
    {
        if (!event.hasItem())
        {
            return;
        }

        if (!event.getAction().isRightClick())
        {
            return;
        }

        if (!event.getItem().getItemMeta().getPersistentDataContainer().has(plugin.BACKPACK_KEY))
        {
            return;
        }

        final BackpackInventoryGson backpackinventorygson = event.getItem().getItemMeta().getPersistentDataContainer().get(plugin.BACKPACK_KEY,
                plugin.backpackDataType);

        plugin.backpacksResource().find(backpackinventorygson.backpackId).ifPresent(backpack -> {
            if (backpack.update != backpackinventorygson.backpackUpdate)
            {
                backpack.apply(event.getItem());
            }
            final BackpackInventory backpackinventory = this.backpacks.computeIfAbsent(event.getPlayer().getUniqueId(),
                    (k) -> new BackpackInventory(plugin, event.getItem(), backpack));
            backpackinventory.deserialize();
            event.getPlayer().openInventory(backpackinventory.getInventory());
        });
    }

    @EventHandler
    private void onServerTickStart(ServerTickStartEvent event)
    {
        this.backpacks.values().forEach(i -> i.serialize());
    }

    @EventHandler
    private void onServerTickEnd(ServerTickEndEvent event)
    {
        this.backpacks.values().removeIf(i -> i.getInventory().getViewers().isEmpty());
    }

    @EventHandler
    private void onPlayerDropItem(PlayerDropItemEvent event)
    {
        if (!event.getItemDrop().getItemStack().getItemMeta().getPersistentDataContainer().has(plugin.BACKPACK_KEY))
        {
            return;
        }

        if (this.backpacks.containsKey(event.getPlayer().getUniqueId()))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onInventoryClick(InventoryClickEvent event)
    {
        if (!(event.getInventory().getHolder() instanceof BackpackInventory))
        {
            return;
        }

        if (event.getCurrentItem() == null)
        {
            return;
        }

        if (event.getCurrentItem().getItemMeta().getPersistentDataContainer().has(plugin.BACKPACK_KEY))
        {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onInventoryOpen(InventoryOpenEvent event)
    {
        this.updateBackpacks(event.getInventory());
    }

    @EventHandler
    private void onPlayerJoin(PlayerJoinEvent event)
    {
        this.updateBackpacks(event.getPlayer().getInventory());
    }

    private void updateBackpacks(Inventory inventory)
    {
        inventory.all(Material.CLOCK)
                .values()
                .stream()
                .filter(i -> i.getItemMeta().getPersistentDataContainer().has(plugin.BACKPACK_KEY))
                .forEach(i -> {
                    final BackpackInventoryGson backpackinventorygson = i.getItemMeta().getPersistentDataContainer().get(plugin.BACKPACK_KEY,
                            plugin.backpackDataType);
                    plugin.backpacksResource().find(backpackinventorygson.backpackId).ifPresent(backpack -> {
                        if (backpack.update != backpackinventorygson.backpackUpdate)
                        {
                            backpack.apply(i);
                        }
                    });
                });
    }
}
