package tech.claudioed.chat.infra;

import io.vertx.core.json.JsonObject;

public class OpenAPIConfig {

  private final String path;

  public OpenAPIConfig(JsonObject config) {
    path = config.getString("openAPI");
  }

  public String getPath() {
    return path;
  }

}
