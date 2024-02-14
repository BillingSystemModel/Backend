package ru.trkpo.brt.service.cdr;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.trkpo.common.data.CDR;

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
public class CDRDeserealizerTest {

    @Mock
    private BufferedReader readerMock;

    @InjectMocks
    private static final CDRDeserializer underTestDeserializer = new CDRDeserializer();

    private static final String DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss";

    @BeforeAll
    static void setUpClass() {
        setField(underTestDeserializer, "dateTimeFormat", DATE_TIME_FORMAT);
    }

    @Test
    void testDeserializeShouldReturnCDR() throws IOException {
        // Arrange
        String CDRString = "02, 71112223344, 2024/01/06 23:04:51, 2024/01/06 23:49:07";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        LocalDateTime testStartDateTime = LocalDateTime.parse("2024/01/06 23:04:51", formatter);
        LocalDateTime testEndDateTime = LocalDateTime.parse("2024/01/06 23:49:07", formatter);
        String testPhoneNumber = "71112223344";
        String testCallTypeCode = "02";
        // Act
        when(readerMock.readLine()).thenReturn(CDRString);
        Optional<CDR> resultCDR = underTestDeserializer.deserialize(readerMock);
        // Assert
        assertThat(resultCDR.isPresent()).isTrue();
        assertThat(resultCDR.get().getCallTypeCode()).isEqualTo(testCallTypeCode);
        assertThat(resultCDR.get().getPhoneNumber()).isEqualTo(testPhoneNumber);
        assertThat(resultCDR.get().getStartDateTime()).isEqualTo(testStartDateTime);
        assertThat(resultCDR.get().getEndDateTime()).isEqualTo(testEndDateTime);
    }

    @Test
    void testDeserializeShouldThrowParseException() throws IOException {
        // Arrange
        String CDRString = "02, 71112223344, 2024/01/06 23:04:51, 2024/01/06 23:49:07, extra_information";
        // Act
        when(readerMock.readLine()).thenReturn(CDRString);
        // Assert
        assertThrows(ParseException.class, () -> {underTestDeserializer.deserialize(readerMock);});
    }


    @Test
    void testDeserializeShouldReturnEmptyOptional() throws IOException {
        // Act
        when(readerMock.readLine()).thenReturn(null);
        Optional<CDR> resultCDR = underTestDeserializer.deserialize(readerMock);
        // Assert
        assertThat(resultCDR.isEmpty()).isTrue();
    }
}
