package ru.trkpo.hrs.service.cdrPlus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.trkpo.common.data.CDRPlus;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;
import org.springframework.expression.ParseException;

@ExtendWith(MockitoExtension.class)
public class CDRPlusDeserializerTest {

    @Mock
    private BufferedReader readerMock;

    @InjectMocks
    private final CDRPlusDeserializer underTestDeserializer = new CDRPlusDeserializer();

    private static final String DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss";

    @BeforeEach
    void setUpClass() {
        setField(underTestDeserializer, "dateTimeFormat", DATE_TIME_FORMAT);
    }

    @Test
    void testDeserializeShouldReturnCDRPlus() throws IOException {
        // Arrange
        String CDRPlusString = "02, 73333333333, 2024/01/06 23:04:51, 2024/01/06 23:49:07, 44, 03";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        LocalDateTime testStartDateTime = LocalDateTime.parse("2024/01/06 23:04:51", formatter);
        LocalDateTime testEndDateTime = LocalDateTime.parse("2024/01/06 23:49:07", formatter);
        String testPhoneNumber = "73333333333";
        String testCallTypeCode = "02";
        String testTariffCode = "03";

        // Act
        when(readerMock.readLine()).thenReturn(CDRPlusString);
        Optional<CDRPlus> resultCDRPlus = underTestDeserializer.deserialize(readerMock);

        // Assert
        assertThat(resultCDRPlus.isPresent()).isTrue();
        assertThat(resultCDRPlus.get().getCallTypeCode()).isEqualTo(testCallTypeCode);
        assertThat(resultCDRPlus.get().getPhoneNumber()).isEqualTo(testPhoneNumber);
        assertThat(resultCDRPlus.get().getStartDateTime()).isEqualTo(testStartDateTime);
        assertThat(resultCDRPlus.get().getEndDateTime()).isEqualTo(testEndDateTime);
        assertThat(resultCDRPlus.get().getTariffCode()).isEqualTo(testTariffCode);
    }

    @Test
    void testDeserializeShouldThrowParseException() throws IOException {
        // Arrange
        String CDRPlusString = "02, 73333333333, 2024/01/06 23:04:51, 2024/01/06 23:49:07, 44, 03, extra_information";
        // Act
        when(readerMock.readLine()).thenReturn(CDRPlusString);
        // Assert
        assertThrows(ParseException.class, () -> {underTestDeserializer.deserialize(readerMock);});
    }


    @Test
    void testDeserializeShouldReturnEmptyOptional() throws IOException {
        // Act
        when(readerMock.readLine()).thenReturn(null);
        Optional<CDRPlus> resultCDR = underTestDeserializer.deserialize(readerMock);
        // Assert
        assertThat(resultCDR.isEmpty()).isTrue();
    }
}
