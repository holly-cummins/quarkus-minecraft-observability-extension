package com.example.examplemod;


import io.undertow.Undertow;
import io.undertow.servlet.api.DeploymentInfo;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;
import java.net.InetAddress;

public class Listener {
    public Listener() {

        UndertowJaxrsServer server = new UndertowJaxrsServer();

        ResteasyDeployment deployment = new ResteasyDeployment();
        deployment.setApplicationClass(Application.class.getName());

        DeploymentInfo deploymentInfo = server.undertowDeployment(deployment, "/");
        deploymentInfo.setClassLoader(this.getClass().getClassLoader());
        deploymentInfo.setDeploymentName("Example");
        deploymentInfo.setContextPath("");

        server.deploy(deploymentInfo);

        try {
            Undertow.Builder builder = Undertow.builder()
                                               .addHttpListener(8081, InetAddress.getLocalHost()
                                                                                 .getHostAddress());
            server.start(builder);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
