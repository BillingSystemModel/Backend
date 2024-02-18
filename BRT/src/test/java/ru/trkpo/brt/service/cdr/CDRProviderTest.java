package ru.trkpo.brt.service.cdr;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedConstruction;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.UrlResource;
import ru.trkpo.common.data.CDR;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
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

import ru.trkpo.common.service.Deserializer;

@ExtendWith(MockitoExtension.class)
public class CDRProviderTest {

    @Mock
    private Deserializer<CDR> cdrDeserializerMock;

    @InjectMocks
    private CDRProviderImpl underTestProvider;

    private static final String FILE_PATH = "..\\files\\cdrProvider.txt";
    private static final String DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss";

    @BeforeAll
    static void setUp() {

    }

    @BeforeEach
    void setUpClass() {
        setField(underTestProvider, "filePath", FILE_PATH);
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

    // todo: обозвать тест
    @Test
    void testGetCDRsListShould() throws IOException {
        // Arrange
        String callTypeCode = "02";
        String phoneNumber = "71112223344";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        LocalDateTime startDateTime1 = LocalDateTime.parse("2024/01/06 23:04:51", formatter);
        LocalDateTime endDateTime1 = LocalDateTime.parse("2024/01/06 23:49:07", formatter);
        LocalDateTime startDateTime2 = LocalDateTime.parse("2024/01/06 23:50:53", formatter);
        LocalDateTime endDateTime2 = LocalDateTime.parse("2024/01/06 23:57:02", formatter);
        CDR record1 = new CDR(callTypeCode, phoneNumber, startDateTime1, endDateTime1);
        CDR record2 = new CDR(callTypeCode, phoneNumber, startDateTime2, endDateTime2);
        List<CDR> testList = new ArrayList<>();
        testList.add(record1);
        testList.add(record2);
        // Act
        try (MockedConstruction<UrlResource> mockUrlResource = mockConstruction(UrlResource.class, (mock, context) -> {
            when(mock.getInputStream()).thenReturn(new FileInputStream(FILE_PATH));
        })) {
            underTestProvider.init();
        }
        when(cdrDeserializerMock.deserialize(any(BufferedReader.class))).thenReturn(Optional.of(record1))
                .thenReturn(Optional.of(record2))
                .thenReturn(Optional.empty());
        List<CDR> resultCDRList = underTestProvider.getCDRsList();
        // Assert
        assertThat(resultCDRList).isEqualTo(testList);
    }

    @Test
    void testGetCDRsCorrectlyWorkWithUncheckedException() throws IOException {
        // Arrange
        // Act
        try (MockedConstruction<UrlResource> mockUrlResource = mockConstruction(UrlResource.class, (mock, context) -> {
            when(mock.getInputStream()).thenReturn(new FileInputStream(FILE_PATH));
        })) {
            underTestProvider.init();
        }
        when(cdrDeserializerMock.deserialize(any(BufferedReader.class))).thenThrow(IOException.class);
        // Assert
        assertThrows(UncheckedIOException.class, () -> {
            underTestProvider.getCDRsList();
        });
    }
}
