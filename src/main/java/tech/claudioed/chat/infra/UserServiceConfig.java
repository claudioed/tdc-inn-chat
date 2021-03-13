package tech.claudioed.chat.infra;

import io.vertx.core.json.JsonObject;

public class UserServiceConfig {

  private final String host;

  private final Integer port;

  public UserServiceConfig(JsonObject datasourceConfig) {
    this.host = datasourceConfig.getString("host", "localhost");
    this.port = datasourceConfig.getInteger("port", 7777);
  }

  public String getHost() {
    return host;
  }

  public Integer getPort() {
    return port;
  }

}
