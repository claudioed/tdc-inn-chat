package tech.claudioed.chat.infra;

public class OpenAPIConfig {

  private final String path;

  public OpenAPIConfig(String cfg) {
    path = cfg;
  }

  public String getPath() {
    return path;
  }

}
