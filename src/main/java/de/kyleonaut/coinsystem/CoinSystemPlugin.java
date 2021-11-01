package de.kyleonaut.coinsystem;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import de.kyleonaut.coinsystem.api.CoinSystemAPI;
import de.kyleonaut.coinsystem.database.Database;
import de.kyleonaut.coinsystem.entity.User;
import de.kyleonaut.coinsystem.repository.CoinSystemRepository;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author kyleonaut
 * @version 1.0.0
 * created at 01.11.2021
 */
public class CoinSystemPlugin extends JavaPlugin implements Listener {

    @Getter
    private Injector injector;

    @Inject
    private CoinSystemRepository coinSystemRepository;

    @Inject
    private Database database;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        final CoinSystemModule coinSystemModule = new CoinSystemModule(this);
        this.injector = Guice.createInjector(coinSystemModule);
        this.injector.injectMembers(this);
        database.connect();
        coinSystemRepository.init();

        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        database.disconnect();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        CoinSystemAPI.getInstance().getUser(player.getUniqueId()).whenComplete((user, throwable) -> {
            if (user == null) {
                CoinSystemAPI.getInstance().replaceUser(new User(player.getName(), player.getUniqueId(), 0));
                return;
            }
            CoinSystemAPI.getInstance().replaceUser(new User(player.getName(), user.getUuid(), user.getAmount()));
        });
    }
}
