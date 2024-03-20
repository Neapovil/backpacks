package com.github.neapovil.backpacks.command;

import com.github.neapovil.backpacks.Backpacks;

public abstract class AbstractCommand
{
    protected Backpacks plugin = Backpacks.instance();

    public abstract void register();
}
