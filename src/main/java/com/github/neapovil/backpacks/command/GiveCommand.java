package com.github.neapovil.backpacks.command;

import java.util.Optional;

import org.bukkit.entity.Player;

import com.github.neapovil.backpacks.resources.BackpacksResource.Backpack;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.LiteralArgument;
import dev.jorel.commandapi.arguments.StringArgument;

public final class GiveCommand extends AbstractCommand
{
    @Override
    public void register()
    {
        new CommandAPICommand("backpacks")
                .withPermission("backpacks.command")
                .withArguments(new LiteralArgument("give"))
                .withArguments(new EntitySelectorArgument.OnePlayer("player"))
                .withArguments(new StringArgument("name").replaceSuggestions(ArgumentSuggestions.strings(plugin.backpacksResource().commandSuggestions())))
                .executes((sender, args) -> {
                    final Player player = (Player) args.get("player");
                    final String name = (String) args.get("name");
                    final Optional<Backpack> optionalbackpack = plugin.backpacksResource().find(name);

                    optionalbackpack.ifPresentOrElse(backpack -> {
                        player.getInventory().addItem(backpack.itemStack());
                    }, () -> sender.sendRichMessage("<red>Backpack not found"));
                })
                .register();
    }
}
