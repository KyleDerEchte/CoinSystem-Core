package de.kyleonaut.coinsystem.repository;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.kyleonaut.coinsystem.database.Database;
import de.kyleonaut.coinsystem.entity.User;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author kyleonaut
 * @version 1.0.0
 * created at 01.11.2021
 */
@Singleton
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class CoinSystemRepositoryImpl implements CoinSystemRepository {
    private final Database database;

    @Override
    public User findUserByUUIDSync(UUID uuid) {
        try {
            @Cleanup final PreparedStatement ps = database.getConnection().prepareStatement(
                    "SELECT * FROM coinsystem.users WHERE users.uuid = ?;"
            );
            ps.setString(1, uuid.toString());
            @Cleanup final ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new User(rs.getString("name"), uuid, rs.getLong("amount"));
            }
            return null;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public CompletableFuture<User> findUserByUUIDAsync(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                @Cleanup final PreparedStatement ps = database.getConnection().prepareStatement(
                        "SELECT * FROM coinsystem.users WHERE users.uuid = ?;"
                );
                ps.setString(1, uuid.toString());
                @Cleanup final ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return new User(rs.getString("name"), uuid, rs.getLong("amount"));
                }
                return null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    @Override
    public void setCoins(UUID uuid, long amount) {
        CompletableFuture.runAsync(() -> {
            try {
                @Cleanup final PreparedStatement ps = database.getConnection().prepareStatement(
                        "UPDATE coinsystem.users SET users.amount = ? WHERE users.uuid = ?;"
                );
                ps.setLong(1, amount);
                ps.setString(2, uuid.toString());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public CompletableFuture<Long> getCoins(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                @Cleanup final PreparedStatement ps = database.getConnection().prepareStatement(
                        "SELECT amount FROM coinsystem.users WHERE users.uuid = ?;"
                );
                ps.setString(1, uuid.toString());
                @Cleanup final ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    return rs.getLong("amount");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
    }

    @Override
    public void createUser(User user) {
        CompletableFuture.runAsync(() -> {
            try {
                @Cleanup final PreparedStatement ps = database.getConnection().prepareStatement(
                        "REPLACE INTO coinsystem.users VALUES(?,?,?);"
                );
                ps.setString(1, user.getUuid().toString());
                ps.setString(2, user.getName());
                ps.setLong(3, user.getAmount());
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void init() {
        try {
            @Cleanup final PreparedStatement ps = database.getConnection().prepareStatement(
                    "CREATE TABLE IF NOT EXISTS users(uuid VARCHAR(36) PRIMARY KEY NOT NULL, name VARCHAR(50),amount BIGINT);"
            );
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
