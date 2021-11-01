package de.kyleonaut.coinsystem.database;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import de.kyleonaut.coinsystem.CoinSystemPlugin;
import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author kyleonaut
 * @version 1.0.0
 * created at 01.11.2021
 */
@Singleton
public class Database {
    @Getter
    private Connection connection;
    private final String user;
    private final String password;
    private final String url;

    @Inject
    public Database(CoinSystemPlugin plugin) {
        this.user = plugin.getConfig().getString("user");
        this.password = plugin.getConfig().getString("password");
        this.url = plugin.getConfig().getString("url");
    }

    public void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, user, password);
            System.out.println("[CoinSystem] MySQL Verbindung hergestellt!");
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        if (this.connection != null) {
            try {
                this.connection.close();
                System.out.println("[CoinSystem] MySQL Verbindung getrennt!");
            } catch (SQLException throwable) {
                throwable.printStackTrace();
            }
        }
    }
}
