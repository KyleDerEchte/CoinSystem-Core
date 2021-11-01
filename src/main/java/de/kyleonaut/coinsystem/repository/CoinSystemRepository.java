package de.kyleonaut.coinsystem.repository;

import com.google.inject.ImplementedBy;
import de.kyleonaut.coinsystem.entity.User;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author kyleonaut
 * @version 1.0.0
 * created at 01.11.2021
 */
@ImplementedBy(CoinSystemRepositoryImpl.class)
public interface CoinSystemRepository {
    /**
     * Get a User by the given UUID synchronously
     *
     * @param uuid The uuid of the User
     * @return Returns a User object or null if the user couldn't be found
     */
    User findUserByUUIDSync(UUID uuid);

    /**
     * Get a User by the given UUID asynchronously
     *
     * @param uuid The uuid of the User
     * @return Returns a User object or null if the user couldn't be found
     */
    CompletableFuture<User> findUserByUUIDAsync(UUID uuid);

    /**
     * Set the coins of a Users
     *
     * @param uuid The uuid of the User
     * @param amount The amount of coins to set
     */
    void setCoins(UUID uuid, long amount);

    /**
     * Get the coins of a Users
     *
     * @param uuid The uuid of the User
     * @return A CompletableFuture with the amount or null
     */
    CompletableFuture<Long> getCoins(UUID uuid);


    /**
     * Create a new User
     *
     * @param user The user Object to persist
     */
    void createUser(User user);

    /**
     * Initialize the SQL tables
     */
    void init();
}
