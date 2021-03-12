package tech.claudioed.chat.infra;

import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgConnectOptions;

public class DatasourceConfig {

  private final String host;
  private final int port;
  private final String database;
  private final String user;
  private final String password;

  public DatasourceConfig(JsonObject datasourceConfig) {
    host = datasourceConfig.getString("host", "localhost");
    port = datasourceConfig.getInteger("port", 5433);
    database = datasourceConfig.getString("database", "chat");
    user = datasourceConfig.getString("user", "admin");
    password = datasourceConfig.getString("password", "admin");
  }

  public String getHost() {
    return host;
  }

  public int getPort() {
    return port;
  }

  public String getDatabase() {
    return database;
  }

  public String getUser() {
    return user;
  }

  public String getPassword() {
    return password;
  }

  public PgConnectOptions toPgConnectOptions() {
    return new PgConnectOptions()
      .setHost(host)
      .setPort(port)
      .setDatabase(database)
      .setUser(user)
      .setPassword(password);
  }

  public String jdbcUrl() {
    return String.format("jdbc:postgresql://%s:%d/%s", host, port, database);
  }
}
