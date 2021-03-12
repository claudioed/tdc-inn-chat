package tech.claudioed.chat.infra;

import io.vertx.core.json.JsonObject;

public class PoliceOfficerConfig {

  private final String host;

  private final int port;

  public PoliceOfficerConfig(JsonObject datasourceConfig) {
    host = datasourceConfig.getString("host", "localhost");
    port = datasourceConfig.getInteger("port", 7777);
  }

  public String getHost() {
    return host;
  }

  public int getPort() {
    return port;
  }

}
