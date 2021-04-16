module com.machnos.api.gateway.server {
    requires jdk.unsupported; // Undertow needs sun.misc.Unsafe
    requires org.bouncycastle.pkix;
    requires org.bouncycastle.provider;
    requires org.yaml.snakeyaml;
    requires org.apache.logging.log4j;
    requires undertow.core;
    requires xnio.api;
    exports com.machnos.api.gateway.server.configuration to org.yaml.snakeyaml;
}