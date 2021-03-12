package tech.claudioed.chat.infra;

import io.vertx.core.json.JsonObject;

import java.net.URI;

public class JwtConfig {

  private final JsonObject jwt;

  public JwtConfig(JsonObject jwtConfig) {
    this.jwt = jwtConfig.getJsonObject("jwt");
  }
  public String issuer(){
    return this.jwt.getString("issuer");
  }

  public URI jwks(){
    var issuerUri = URI.create(issuer());
    var jwks = String.format("%s://%s:%d%s", issuerUri.getScheme(), issuerUri.getHost(), issuerUri.getPort(),
        issuerUri.getPath() + "/protocol/openid-connect/certs");
    return URI.create(jwks);
  }

  public JsonObject getJwt() {
    return jwt;
  }

}
