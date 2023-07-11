package client;


import interceptor.ClientInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.grpc.stub.MetadataUtils;
import ru.business_types.Process;
import ru.business_types.ProcessStatus;
import ru.process_service.ProcessServiceGrpc;

public class ProcessClient {
    private ProcessServiceGrpc.ProcessServiceBlockingStub processServiceBlockingStub;

    public ProcessClient(String serverHost, int serverPort) {
        this.processServiceBlockingStub = createChannel(serverHost, serverPort);
    }

    public ProcessStatus getProcessStatus(Process process) {
        return this.processServiceBlockingStub.getProcessStatus(process);
    }

    private ProcessServiceGrpc.ProcessServiceBlockingStub createChannel(String serverHost, int serverPost) {
        Metadata metadata = new Metadata();
        metadata.put(Metadata.Key.of("user-token", Metadata.ASCII_STRING_MARSHALLER), "user-token");
        ManagedChannel managedChannel = ManagedChannelBuilder
                .forAddress(serverHost, serverPost)
                .usePlaintext()
                .intercept(MetadataUtils.newAttachHeadersInterceptor(metadata), new ClientInterceptor())
                .build();
        return ProcessServiceGrpc.newBlockingStub(managedChannel);
    }
}
