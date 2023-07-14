package ru.service;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import ru.business_types.Process;
import ru.business_types.ProcessStatus;
import ru.custom_types.ProtoBigDecimal;
import ru.process_service.ProcessServiceGrpc;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

@GrpcService
public class ProcessService extends ProcessServiceGrpc.ProcessServiceImplBase {
    private static final int OK_STATUS = 0;
    private static final int BAD_STATUS = 1;

    @Override
    public void getProcessStatus(Process request, StreamObserver<ProcessStatus> responseObserver) {
        BigDecimal bigDecimalFromProcess = getBigDecimalFromProcess(request);
        ProcessStatus result;
        if (isLowerThanZero(bigDecimalFromProcess)) {
            result = ProcessStatus.newBuilder()
                    .setStatusCode(BAD_STATUS)
                    .setDescription("Sum Lower than zero")
                    .build();
        } else {
            result = ProcessStatus.newBuilder()
                    .setStatusCode(OK_STATUS)
                    .build();
        }
       responseObserver.onNext(result);
       responseObserver.onCompleted();
    }


    private boolean isLowerThanZero(BigDecimal bigDecimal) {
        return bigDecimal.compareTo(BigDecimal.ZERO) < 0;
    }

    private BigDecimal getBigDecimalFromProcess(Process process) {
        ProtoBigDecimal sum = process.getSum();
        MathContext mc = new MathContext(sum.getPrecision());
        return new BigDecimal(
                new BigInteger(sum.toByteArray()),
                sum.getScale(),
                mc);
    }

}
