package tech.claudioed.chat;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.micrometer.MicrometerMetricsOptions;
import io.vertx.micrometer.VertxPrometheusOptions;
import io.vertx.tracing.opentracing.OpenTracingOptions;

public class Application {

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx(new VertxOptions().setMetricsOptions(
      new MicrometerMetricsOptions()
        .setPrometheusOptions(new VertxPrometheusOptions().setEnabled(true))
        .setEnabled(true))
      .setTracingOptions(new OpenTracingOptions()));
    vertx.deployVerticle(new MainVerticle());
    vertx.deployVerticle(new UserDataVerticle());
  }

}
