package xyz.spedcord.common.sql;

import com.mysql.cj.jdbc.MysqlDataSource;
import org.intellij.lang.annotations.Language;
import xyz.spedcord.common.config.Config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class MySqlService {

    private ExecutorService executorService;
    private MysqlDataSource dataSource;
    private Connection connection;

    public MySqlService(Config config) throws SQLException {
        this(
                config.get("host"),
                config.get("user"),
                config.get("pass"),
                config.get("db"),
                Integer.parseInt(config.get("port"))
        );
    }

    public MySqlService(String host, String user, String pass, String db, int port) throws SQLException {
        executorService = Executors.newSingleThreadExecutor();

        dataSource = new MysqlDataSource();
        dataSource.setServerName(host);
        dataSource.setUser(user);
        dataSource.setPassword(pass);
        dataSource.setDatabaseName(db);
        dataSource.setPort(port);
        dataSource.setServerTimezone("UTC");

        connection = dataSource.getConnection();
    }

    public PreparedStatement prepareStatement(@Language(value = "SQL") String sql) throws SQLException {
        return connection.prepareStatement(sql);
    }

    public void updateAsync(@Language(value = "SQL") String sql, Consumer<Boolean> callback) {
        executorService.submit(() -> {
            try {
                callback.accept(update(sql));
            } catch (SQLException e) {
                e.printStackTrace();
                callback.accept(false);
            }
        });
    }

    public boolean update(@Language(value = "SQL") String sql) throws SQLException {
        return prepareStatement(sql).execute();
    }

    public void executeAsync(@Language(value = "SQL") String sql, Consumer<ResultSet> callback) {
        executorService.submit(() -> {
            try {
                callback.accept(execute(sql));
            } catch (SQLException e) {
                e.printStackTrace();
                callback.accept(null);
            }
        });
    }

    public ResultSet execute(@Language(value = "SQL") String sql) throws SQLException {
        return prepareStatement(sql).executeQuery();
    }

    public Connection getConnection() {
        return connection;
    }

}
