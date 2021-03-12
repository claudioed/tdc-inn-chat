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

  public URI jwks(){
    var issuerUri = URI.create(issuer());
    var jwks = String.format("%s://%s:%d%s", issuerUri.getScheme(), issuerUri.getHost(), issuerUri.getPort(),
        issuerUri.getPath() + "/protocol/openid-connect/certs");
    return URI.create(jwks);
  }

}
