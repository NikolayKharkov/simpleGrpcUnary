package client;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.UUID;

import com.google.protobuf.ByteString;
import io.grpc.StatusRuntimeException;
import org.apache.log4j.Logger;
import ru.business_types.Process;
import ru.business_types.ProcessStatus;
import ru.custom_types.ProtoBigDecimal;
import ru.custom_types.ProtoUUID;

public class GrpcClient {
    private static Logger logger = Logger.getLogger(GrpcClient.class);
    private static final String PROPERTIES_FILE_PATH = "grpcClient/src/main/resources/application.properties";
    private static final String SEVER_PORT = "client.port";
    private static final String SEVER_HOST = "client.host";

    public static void main(String[] args) {
        ProcessClient processClient = null;
        try (InputStream input = new FileInputStream(PROPERTIES_FILE_PATH)) {
            Properties prop = new Properties();
            prop.load(input);
            int serverPort = Integer.parseInt((String) prop.get(SEVER_PORT));
            String SEVERHost = (String) prop.get(SEVER_HOST);
            processClient = new ProcessClient(SEVERHost, serverPort);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        java.math.BigDecimal bigDecimal = new java.math.BigDecimal("1234.56789");
        ProtoUUID uuid = ProtoUUID.newBuilder().setValue(UUID.randomUUID().toString()).build();
        ProtoBigDecimal sum = ProtoBigDecimal.newBuilder()
                .setScale(bigDecimal.scale())
                .setPrecision(bigDecimal.precision())
                .setValue(ByteString.copyFrom(bigDecimal.unscaledValue().toByteArray()))
                .build();
        Process process = Process.newBuilder().setProcessName("Test").setSum(sum).setRequestId(uuid).build();
        logger.info("Отправляем запрос: " + process);
        try {
            ProcessStatus processStatus = processClient.getProcessStatus(process);
            logger.info("Получили ответ: " + processStatus);
        } catch (StatusRuntimeException e) {
            logger.error("Ошибка при отправке: " + e);
        }
    }
}
