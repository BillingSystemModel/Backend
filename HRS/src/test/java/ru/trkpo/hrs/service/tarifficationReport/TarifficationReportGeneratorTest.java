package ru.trkpo.hrs.service.tarifficationReport;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.trkpo.common.data.CDRPlus;
import ru.trkpo.common.data.dto.CallDataDTO;
import ru.trkpo.common.data.dto.TarifficationReportDTO;
import ru.trkpo.common.service.tariff.TariffService;
import ru.trkpo.hrs.service.CallData.CallDataSaver;
import ru.trkpo.hrs.service.cdrPlus.CDRPlusProvider;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TarifficationReportGeneratorTest {

    @Mock
    private CDRPlusProvider cdrPlusProviderMock;
    @Mock
    private TarifficationReportWriter reportWriterMock;
    @Mock
    private TariffService tariffServiceMock;
    @Mock
    private CallDataSaver callDataSaverMock;

    @InjectMocks
    private TarifficationReportGeneratorImpl underTestGenerator;

    @Test
    void testGenerateReportsShould() throws IOException {
        // Arrange
        List<CDRPlus> testCDRPlusList = new ArrayList<>();
        String testCallTypeCode = "01";
        String testPhoneNumber = "71112223344";
        LocalDateTime testStartDateTime1 = LocalDateTime.of(2024, 1, 1, 1, 0);
        LocalDateTime testStartDateTime2 = LocalDateTime.of(2024, 1, 1, 1, 0);
        LocalDateTime testEndDateTime1 = LocalDateTime.of(2024, 2, 1, 1, 30);
        LocalDateTime testEndDateTime2 = LocalDateTime.of(2024, 2, 1, 1, 30);
        Duration testDuration = Duration.ofMinutes(44);
        String testTariffCode = "03";
        CDRPlus record1 = new CDRPlus(testCallTypeCode, testPhoneNumber, testStartDateTime1, testEndDateTime1, testDuration, testTariffCode);
        CDRPlus record2 = new CDRPlus(testCallTypeCode, testPhoneNumber, testStartDateTime2, testEndDateTime2, testDuration, testTariffCode);
        testCDRPlusList.add(record1);
        testCDRPlusList.add(record2);
        // Act
        doNothing().when(cdrPlusProviderMock).init();
        doNothing().when(reportWriterMock).init();
        when(cdrPlusProviderMock.getCDRPlus()).thenReturn(testCDRPlusList);
        when(tariffServiceMock.applyTariff(any(CDRPlus.class))).thenReturn(BigDecimal.valueOf(123));
        doNothing().when(callDataSaverMock).saveCall(anyString(),any(CallDataDTO.class));
        doNothing().when(reportWriterMock).write(any(TarifficationReportDTO.class));

        // Assert
        assertDoesNotThrow(() -> {underTestGenerator.generateReports();});
    }

}
