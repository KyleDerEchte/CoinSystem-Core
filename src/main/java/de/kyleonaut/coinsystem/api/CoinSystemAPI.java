package de.kyleonaut.coinsystem.api;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.kyleonaut.coinsystem.CoinSystemPlugin;
import de.kyleonaut.coinsystem.api.exception.UserNotFoundException;
import de.kyleonaut.coinsystem.entity.User;
import de.kyleonaut.coinsystem.repository.CoinSystemRepository;
import lombok.RequiredArgsConstructor;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author kyleonaut
 * @version 1.0.0
 * created at 01.11.2021
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class CoinSystemAPI {
    private final CoinSystemRepository coinSystemRepository;

    public static CoinSystemAPI getInstance() {
        return CoinSystemPlugin.getPlugin(CoinSystemPlugin.class).getInjector().getInstance(CoinSystemAPI.class);
    }

    /**
     * Add coins to the Users balance
     *
     * @param uuid   The uuid of the User
     * @param amount The amount to be added
     */
    public void addCoins(UUID uuid, long amount) {
        coinSystemRepository.getCoins(uuid).whenComplete((coins, throwable) -> {
            if (coins == null) {
                try {
                    throw new UserNotFoundException();
                } catch (UserNotFoundException e) {
                    e.printStackTrace();
                }
                return;
            }
            coins = coins + amount;
            coinSystemRepository.setCoins(uuid, coins);
        });
    }

    /**
     * Remove coins from the Users balance
     *
     * @param uuid   The uuid of the User
     * @param amount The amount to be added
     */
    public void removeCoins(UUID uuid, long amount) {
        coinSystemRepository.getCoins(uuid).whenComplete((coins, throwable) -> {
            if (coins == null) {
                try {
                    throw new UserNotFoundException();
                } catch (UserNotFoundException e) {
                    e.printStackTrace();
                }
                return;
            }
            coins = coins - amount;
            coinSystemRepository.setCoins(uuid, coins);
        });
    }

    /**
     * Set the balance of the User
     *
     * @param uuid   The uuid of the User
     * @param amount The amount to be added
     */
    public void setCoins(UUID uuid, long amount) {
        coinSystemRepository.setCoins(uuid, amount);
    }

    /**
     * Create a new User
     *
     * @param user The User object to persist
     */
    public void replaceUser(User user) {
        coinSystemRepository.createUser(user);
    }

    /**
     * Get a User
     *
     * @param uuid The uuid of the User
     */
    public CompletableFuture<User> getUser(UUID uuid) {
        return coinSystemRepository.findUserByUUIDAsync(uuid);
    }

    /**
     * Get Coins of an User
     *
     * @param uuid The uuid of the User
     * @return A CompletableFuture with the coins or null if the User couldn't be found
     */
    public CompletableFuture<Long> getCoins(UUID uuid) {
        return coinSystemRepository.getCoins(uuid);
    }

}
