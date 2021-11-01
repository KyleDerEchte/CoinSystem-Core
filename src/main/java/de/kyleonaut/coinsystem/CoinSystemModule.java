package de.kyleonaut.coinsystem;

import com.google.inject.AbstractModule;

/**
 * @author kyleonaut
 * @version 1.0.0
 * created at 01.11.2021
 */
public class CoinSystemModule extends AbstractModule {
    private final CoinSystemPlugin plugin;

    public CoinSystemModule(CoinSystemPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    protected void configure() {
        bind(CoinSystemPlugin.class).toInstance(plugin);
    }
}
