package ru.trkpo.brt.service.tarifficationReport;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.expression.ParseException;
import ru.trkpo.common.data.dto.CallDataDTO;

import java.io.BufferedReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
public class CallDataDeserializerTest {

    @Mock
    private BufferedReader readerMock;

    @InjectMocks
    private static final CallDataDeserializer underTestDeserializer = new CallDataDeserializer();

    private static final String DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss";

    @BeforeAll
    static void setUpClass() {
        setField(underTestDeserializer, "dateTimeFormat", DATE_TIME_FORMAT);
    }

    @Test
    void testDeserializeShouldReturnCallDataString() throws IOException {
        // Arrange
        String callDataString = "02, 2024/01/06 23:04:51, 2024/01/06 23:49:07, 44, 33";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        LocalDateTime testStartDateTime = LocalDateTime.parse("2024/01/06 23:04:51", formatter);
        LocalDateTime testEndDateTime = LocalDateTime.parse("2024/01/06 23:49:07", formatter);
        String testCallTypeCode = "02";
        Duration testDuration = Duration.ofMinutes(44);
        BigDecimal testCost = BigDecimal.valueOf(Double.parseDouble("33"));
        // Act
        when(readerMock.readLine()).thenReturn(callDataString);
        Optional<CallDataDTO> resultCallData = underTestDeserializer.deserialize(readerMock);
        // Assert
        assertThat(resultCallData.isPresent()).isTrue();
        assertThat(resultCallData.get().getCallTypeCode()).isEqualTo(testCallTypeCode);
        assertThat(resultCallData.get().getStartDateTime()).isEqualTo(testStartDateTime);
        assertThat(resultCallData.get().getEndDateTime()).isEqualTo(testEndDateTime);
        assertThat(resultCallData.get().getDuration()).isEqualTo(testDuration);
        assertThat(resultCallData.get().getCost()).isEqualTo(testCost);
    }

    @Test
    void testDeserializeShouldThrowParseException() throws IOException {
        // Arrange
        String CallDataString = "02, 2024/01/06 23:04:51, 2024/01/06 23:49:07, 44, 03, extra_information";
        // Act
        when(readerMock.readLine()).thenReturn(CallDataString);
        // Assert
        assertThrows(ParseException.class, () -> {underTestDeserializer.deserialize(readerMock);});
    }

    @Test
    void testDeserializeShouldReturnEmptyOptional() throws IOException {
        // Act
        when(readerMock.readLine()).thenReturn(null);
        Optional<CallDataDTO> resultCallData = underTestDeserializer.deserialize(readerMock);
        // Assert
        assertThat(resultCallData.isEmpty()).isTrue();
    }
}
