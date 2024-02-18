package ru.trkpo.hrs.service.cdrPlus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.UrlResource;
import ru.trkpo.common.data.CDRPlus;
import ru.trkpo.common.service.Deserializer;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.getField;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
public class CDRPlusProviderTest {
    @Mock
    private Deserializer<CDRPlus> cdrPlusDeserializerMock;

    @InjectMocks
    private CDRPlusProviderImpl underTestProvider;

    private static final String FILE_PATH = "..\\files\\cdrProvider.txt";
    private static final String DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss";

    @BeforeEach
    void setUpClass() {
        setField(underTestProvider, "sourceURL", FILE_PATH);
    }

    @Test
    void testInitShouldMakeReader() throws IOException {
        // Arrange
        // Act
        try (MockedConstruction<UrlResource> mockUrlResource = mockConstruction(UrlResource.class, (mock, context) -> {
            when(mock.getInputStream()).thenReturn(new FileInputStream(FILE_PATH));
        })) {
            underTestProvider.init();
        }
        // Assert
        assertThat(getField(underTestProvider, "reader")).isNotNull();
    }

    @Test
    void testGetCDRPlusShouldReturnCDRPlusList() throws IOException {
        // Arrange
        String callTypeCode = "02";
        String phoneNumber = "71112223344";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        LocalDateTime startDateTime1 = LocalDateTime.parse("2024/01/06 23:04:51", formatter);
        LocalDateTime endDateTime1 = LocalDateTime.parse("2024/01/06 23:49:07", formatter);
        LocalDateTime startDateTime2 = LocalDateTime.parse("2024/01/06 23:50:53", formatter);
        LocalDateTime endDateTime2 = LocalDateTime.parse("2024/01/06 23:57:02", formatter);
        CDRPlus record1 = new CDRPlus(callTypeCode, phoneNumber, startDateTime1, endDateTime1, Duration.between(startDateTime1,endDateTime1),"03");
        CDRPlus record2 = new CDRPlus(callTypeCode, phoneNumber, startDateTime2, endDateTime2, Duration.between(startDateTime2,endDateTime2),"03");
        List<CDRPlus> testList = new ArrayList<>();
        testList.add(record1);
        testList.add(record2);
        // Act
        try (MockedConstruction<UrlResource> mockUrlResource = mockConstruction(UrlResource.class, (mock, context) -> {
            when(mock.getInputStream()).thenReturn(new FileInputStream(FILE_PATH));
        })) {
            underTestProvider.init();
        }
        when(cdrPlusDeserializerMock.deserialize(any(BufferedReader.class))).thenReturn(Optional.of(record1))
                .thenReturn(Optional.of(record2))
                .thenReturn(Optional.empty());
        List<CDRPlus> resultCDRList = underTestProvider.getCDRPlus();
        // Assert
        assertThat(resultCDRList).isEqualTo(testList);
    }

    @Test
    void testGetCDRPlusCorrectlyWorkWithUncheckedException() throws IOException {
        // Arrange
        // Act
        try (MockedConstruction<UrlResource> mockUrlResource = mockConstruction(UrlResource.class, (mock, context) -> {
            when(mock.getInputStream()).thenReturn(new FileInputStream(FILE_PATH));
        })) {
            underTestProvider.init();
        }
        when(cdrPlusDeserializerMock.deserialize(any(BufferedReader.class))).thenThrow(IOException.class);
        // Assert
        assertThrows(UncheckedIOException.class, () -> {
            underTestProvider.getCDRPlus();
        });
    }
}
