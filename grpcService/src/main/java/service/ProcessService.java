package service;

import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;
import ru.business_types.Process;
import ru.business_types.ProcessStatus;
import ru.process_service.ProcessServiceGrpc;

import java.math.BigDecimal;
import java.util.Base64;


public class ProcessService extends ProcessServiceGrpc.ProcessServiceImplBase {
    private final int ZERO = 0;
    private final int OK_STATUS = ZERO;
    private final int BAD_STATUS = 1;
    private final BigDecimal BIG_DECIMAL_ZERO = new BigDecimal(ZERO);

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
        return bigDecimal.compareTo(BIG_DECIMAL_ZERO) < ZERO;
    }

    private BigDecimal getBigDecimalFromProcess(Process process) {
        java.math.MathContext mc = new java.math.MathContext(process.getSum().getPrecision());
        return new java.math.BigDecimal(
                new java.math.BigInteger(process.getSum().toByteArray()),
                process.getSum().getScale(),
                mc);
    }

}
