package com.example.examplemod;


import io.undertow.Undertow;
import io.undertow.servlet.api.DeploymentInfo;
import org.jboss.resteasy.plugins.server.undertow.UndertowJaxrsServer;
import org.jboss.resteasy.spi.ResteasyDeployment;

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

        Undertow.Builder builder = Undertow.builder().addHttpListener(8081, "127.0.0.1");
        server.start(builder);

    }
}
