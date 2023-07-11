package server;

import inceptors.MyServerInterceptor;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import service.ProcessService;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class GrpcServer {
    private static final String PROPERTIES_FILE_PATH = "grpcService/src/main/resources/application.properties";
    private static final String SERVICE_PORT = "service.port";
    public static void main(String[] args) throws IOException, InterruptedException {
        int port = getServicePort();
        Server server = ServerBuilder
                .forPort(port)
                .intercept(new MyServerInterceptor())
                .addService(new ProcessService())
                .build();
        server.start();
        server.awaitTermination();

    }

    private static Integer getServicePort() {
        Integer result = null;
        try (InputStream input = new FileInputStream(PROPERTIES_FILE_PATH)) {
            Properties prop = new Properties();
            prop.load(input);
            result = Integer.parseInt((String) prop.get(SERVICE_PORT));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
