package ru.trkpo.common.service.clientDetails;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.trkpo.common.data.entity.Client;
import ru.trkpo.common.data.entity.ClientDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service
@AllArgsConstructor
public class ClientDetailsServiceImpl implements ClientDetailsService {

    @Override
    public ClientDetails createNewClientDetails(Client client, String encodedPassword) {
        return ClientDetails.builder()
                .id(null)
                .client(client)
                .numberPersonalAccount(generatePersonalAccountNumber())
                .email(null)
                .password(encodedPassword)
                .region(generateRegion())
                .passport(generatePassport())
                .contractDate(LocalDate.now())
                .contractNumber(generateContractNumber())
                .build();
    }

    private String generatePassport() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyMMddHHmm");
        return LocalDateTime.now().format(formatter);
    }

    private String generateRegion() {
        String[] streetNames = {"Main St", "Oak St", "Maple Ave", "Cedar Ln", "Pine Rd"};
        String[] cities = {"City1", "City2", "City3", "City4", "City5"};
        String[] states = {"State1", "State2", "State3", "State4", "State5"};
        String[] zipCodes = {"12345", "67890", "54321", "98765", "23456"};
        Random random = new Random();

        String street = streetNames[random.nextInt(streetNames.length)];
        String city = cities[random.nextInt(cities.length)];
        String state = states[random.nextInt(states.length)];
        String zipCode = zipCodes[random.nextInt(zipCodes.length)];
        return zipCode + ", " + state + ", " + city + " " + street;
    }

    private String generateContractNumber() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        return LocalDateTime.now().format(formatter) + '0';
    }

    private int generatePersonalAccountNumber() {
        return new Random().nextInt(Integer.MAX_VALUE);
    }
}
