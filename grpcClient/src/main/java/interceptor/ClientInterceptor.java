package interceptor;

import io.grpc.*;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ClientInterceptor implements io.grpc.ClientInterceptor {
    @Override
    public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(MethodDescriptor<ReqT, RespT> methodDescriptor, CallOptions callOptions, Channel channel) {
        Deadline deadline = callOptions.getDeadline();
        if (Objects.isNull(deadline)) {
            deadline = Deadline.after(5, TimeUnit.SECONDS);
        }
        return channel.newCall(methodDescriptor,  callOptions.withDeadline(deadline));
    }

}
