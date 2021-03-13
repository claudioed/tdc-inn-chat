package tech.claudioed.chat.infra;

import io.vertx.core.json.JsonObject;

import java.net.URI;

public class JwtConfig {

  private final String issuer;

  public JwtConfig(JsonObject jwtConfig) {
    this.issuer = jwtConfig.getString("issuer");
  }
  public String issuer(){
    return this.issuer;
  }

  public String jwks(){
    var issuerUri = URI.create(issuer());
    return String.format("%s://%s%s", issuerUri.getScheme(), issuerUri.getHost(),
        issuerUri.getPath() + "/protocol/openid-connect/certs");
  }

}
