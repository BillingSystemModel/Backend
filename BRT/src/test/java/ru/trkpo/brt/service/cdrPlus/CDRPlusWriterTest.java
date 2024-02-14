package ru.trkpo.brt.service.cdrPlus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.trkpo.common.data.CDRPlus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
public class CDRPlusWriterTest {

    @Mock
    private CDRPlusSerializer cdrPlusSerializerMock;

    @InjectMocks
    private CDRPlusWriterImpl underTestWriter;

    private static final String CDR_PLUS_FILE_PATH = "../files/cdr-plus.txt";

    @BeforeEach
    void setUp() {
        setField(underTestWriter, "cdrPlusFilePath", CDR_PLUS_FILE_PATH);
    }

    @Test
    void testInitShouldCleanCDRFile() {
        // Arrange
        File cdrPlusFile = new File(CDR_PLUS_FILE_PATH);
        // Act
        // Assert
        assertDoesNotThrow(() -> underTestWriter.init());
        assertThat(cdrPlusFile).exists().isEmpty();
        verify(cdrPlusSerializerMock, never()).serialize(any(CDRPlus.class));
    }

    @Test
    void testWriteShouldCDRPlusRecord() throws FileNotFoundException {
        // Arrange
        File cdrPlusFile = new File(CDR_PLUS_FILE_PATH);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        Scanner reader = new Scanner(new FileReader(cdrPlusFile));
        String testCallTypeCode = "01";
        String testPhoneNumber = "71112223344";
        LocalDateTime testStartDateTime = LocalDateTime.of(2024, 1, 1, 1, 0);
        LocalDateTime testEndDateTime = LocalDateTime.of(2024, 1, 1, 1, 30);
        Duration testDuration = Duration.ofMinutes(44);
        String testTariffCode = "03";
        String resultCDRPlusString = testCallTypeCode + " "
                + testPhoneNumber + " "
                + testStartDateTime.format(formatter) + " "
                + testEndDateTime.format(formatter) + " "
                + testDuration + " "
                + testTariffCode + '\n';
        CDRPlus record = new CDRPlus(testCallTypeCode, testPhoneNumber, testStartDateTime, testEndDateTime, testDuration, testTariffCode);
        // Act
        when(cdrPlusSerializerMock.serialize(Mockito.any(CDRPlus.class))).thenReturn(resultCDRPlusString);
        // Assert
        assertDoesNotThrow(() -> {
            underTestWriter.init();
            underTestWriter.write(record);
        });
        String result = reader.nextLine();
        assertThat(cdrPlusFile).exists().isNotEmpty();
        assertThat(result.split(" ")).hasSize(8);
        assertThat(result).isEqualTo(resultCDRPlusString.substring(0, resultCDRPlusString.length() - 1));
        verify(cdrPlusSerializerMock, times(1)).serialize(any(CDRPlus.class));
    }

}
