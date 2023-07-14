package client;

import com.google.protobuf.ByteString;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import ru.business_types.Process;
import ru.business_types.ProcessStatus;
import ru.custom_types.ProtoBigDecimal;
import ru.custom_types.ProtoUUID;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClientTest {
    private ProcessClient processClient;
    @BeforeAll
    public void setUp() {
        processClient = new ProcessClient("localhost", 8080);
    }

    @Test
    public void getDefaultResponse() {
        BigDecimal bigDecimal = new BigDecimal("1000.00");
        ProtoUUID uuid = ProtoUUID.newBuilder().setValue(UUID.randomUUID().toString()).build();
        ProtoBigDecimal sum = ProtoBigDecimal.newBuilder()
                .setScale(bigDecimal.scale())
                .setPrecision(bigDecimal.precision())
                .setValue(ByteString.copyFrom(bigDecimal.unscaledValue().toByteArray()))
                .build();
        Process process = Process.newBuilder().setProcessName("process-name").setSum(sum).setRequestId(uuid).build();
        ProcessStatus response = processClient.getProcessStatus(process);
        assertEquals(response, ProcessStatus.getDefaultInstance());
    }
}

