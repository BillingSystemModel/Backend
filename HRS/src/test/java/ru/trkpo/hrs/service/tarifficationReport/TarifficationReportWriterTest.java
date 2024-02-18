package ru.trkpo.hrs.service.tarifficationReport;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.trkpo.common.data.dto.CallDataDTO;
import ru.trkpo.common.data.dto.TarifficationReportDTO;
import ru.trkpo.common.service.Serializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
public class TarifficationReportWriterTest {
    @Mock
    private Serializer<TarifficationReportDTO> reportSerializerMock;

    @InjectMocks
    private TarifficationReportWriterImpl underTestWriter;

    private static final String REPORT_FILE_PATH = "../files/cdr-plus.txt";

    @BeforeEach
    void setUp() {
        setField(underTestWriter, "reportFilePath", REPORT_FILE_PATH);
    }

    @Test
    void testInitShouldCleanReportFile() {
        // Arrange
        File reportFile = new File(REPORT_FILE_PATH);
        // Act
        // Assert
        assertDoesNotThrow(() -> underTestWriter.init());
        assertThat(reportFile).exists().isEmpty();
        verify(reportSerializerMock, never()).serialize(any(TarifficationReportDTO.class));
    }

    @Test
    void testWriteShouldCDRPlusRecord() throws FileNotFoundException {
        // Arrange
        File reportFile = new File(REPORT_FILE_PATH);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        Scanner reader = new Scanner(new FileReader(reportFile));
        String tariffCode = "02";
        String phoneNumber1 = "71112223344";
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

        String testString1 = "02, 71112223344, 50, 66.0, 2\n";
        String testString2 = "02, 2024/01/06 23:04:51, 2024/01/06 23:49:07, 44, 33\n";
        String testString3 = "02, 2024/01/06 23:50:53, 2024/01/06 23:57:02, 6, 33\n";
        String testString = testString1.concat(testString2).concat(testString3);
        // Act
        when(reportSerializerMock.serialize(any(TarifficationReportDTO.class))).thenReturn(testString);
        // Assert
        assertDoesNotThrow(() -> {
            underTestWriter.init();
            underTestWriter.write(report);
        });
        String result = reader.nextLine();
        assertThat(reportFile).exists().isNotEmpty();
        assertThat(result).isEqualTo(testString1.substring(0, testString1.length() - 1));
        result = reader.nextLine();
        assertThat(result).isEqualTo(testString2.substring(0, testString2.length() - 1));
        result = reader.nextLine();
        assertThat(result).isEqualTo(testString3.substring(0, testString3.length() - 1));
        verify(reportSerializerMock, times(1)).serialize(any(TarifficationReportDTO.class));
    }

}
