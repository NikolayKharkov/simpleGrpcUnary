package ru.inceptors;

import com.google.common.base.Strings;
import io.grpc.*;
import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;

@GrpcGlobalServerInterceptor
public class MyServerInterceptor implements ServerInterceptor {
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> serverCall, Metadata metadata, ServerCallHandler<ReqT, RespT> serverCallHandler) {
        String userToken = metadata.get(Metadata.Key.of("user-token", Metadata.ASCII_STRING_MARSHALLER));
        if (Strings.isNullOrEmpty(userToken)) {
            Status status = Status.UNAUTHENTICATED.withDescription("User token not exist");
            serverCall.close(status, metadata);
        }
        return serverCallHandler.startCall(serverCall, metadata);
    }
}
