module com.machnos.api.gateway.server {
    requires jdk.unsupported; // Undertow needs sun.misc.Unsafe
    requires org.apache.logging.log4j;
    requires org.bouncycastle.pkix;
    requires org.bouncycastle.provider;
    requires org.yaml.snakeyaml;
    requires pac4j.core;
    requires pac4j.http;
    requires undertow.core;
    requires undertow.pac4j;
    requires xnio.api;
    exports com.machnos.api.gateway.server.configuration to org.yaml.snakeyaml;
}