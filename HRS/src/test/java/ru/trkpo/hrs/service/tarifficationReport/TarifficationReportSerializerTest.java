package ru.trkpo.hrs.service.tarifficationReport;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.trkpo.common.data.dto.CallDataDTO;
import ru.trkpo.common.data.dto.TarifficationReportDTO;
import ru.trkpo.common.service.Serializer;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TarifficationReportSerializerTest {

    @Mock
    private Serializer<CallDataDTO> callDataSerializerMock;

    @InjectMocks
    private TarifficationReportSerializer underTestSerializer;

    private static final String DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss";

    @Test
    void testSerializeShouldReturnStringReport() {
        // Arrange
        String tariffCode = "02";
        String phoneNumber1 = "71112223344";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        LocalDateTime startDateTime1 = LocalDateTime.parse("2024/01/06 23:04:51", formatter);
        LocalDateTime endDateTime1 = LocalDateTime.parse("2024/01/06 23:49:07", formatter);
        LocalDateTime startDateTime2 = LocalDateTime.parse("2024/01/06 23:50:53", formatter);
        LocalDateTime endDateTime2 = LocalDateTime.parse("2024/01/06 23:57:02", formatter);
        String callTypeCode1 = "01";
        String callTypeCode2 = "02";
        BigDecimal cost = BigDecimal.valueOf(Double.parseDouble("33"));
        CallDataDTO cd1 = new CallDataDTO(callTypeCode1,
                startDateTime1,
                endDateTime1,
                Duration.between(startDateTime1, endDateTime1),
                cost);
        CallDataDTO cd4 = new CallDataDTO(callTypeCode2,
                startDateTime2,
                endDateTime2,
                Duration.between(startDateTime2, endDateTime2),
                cost);
        List<CallDataDTO> calldata1 = new ArrayList<>();
        calldata1.add(cd1);
        calldata1.add(cd4);

        long totalMinutes = Duration.between(startDateTime1, endDateTime1).toMinutes() + Duration.between(startDateTime2, endDateTime2).toMinutes();
        BigDecimal totalCost = BigDecimal.valueOf(Double.parseDouble("66"));
        TarifficationReportDTO report = new TarifficationReportDTO(phoneNumber1, tariffCode, calldata1, totalMinutes, totalCost);

        String testString2 = "02, 2024/01/06 23:04:51, 2024/01/06 23:49:07, 44, 33\n";
        String testString3 = "02, 2024/01/06 23:50:53, 2024/01/06 23:57:02, 6, 33\n";
        String testString = "02, 71112223344, 50, 66.0, 2\n".concat(testString2).concat(testString3);
        // Act
        when(callDataSerializerMock.serialize(any(CallDataDTO.class))).thenReturn(testString2).thenReturn(testString3);
        String resultString = underTestSerializer.serialize(report);
        // Assert
        assertThat(resultString).isEqualTo(testString);
    }
}
