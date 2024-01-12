package ru.trkpo.crm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.trkpo.common.data.entity.*;
import ru.trkpo.common.service.client.ClientService;
import ru.trkpo.common.service.phoneNumber.PhoneNumberService;
import ru.trkpo.common.service.tariff.TariffService;
import ru.trkpo.crm.data.client.ClientInfo;
import ru.trkpo.crm.data.tariff.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CRMService {

    @Value("${crm-service.date-time-format}")
    private String dateFormat;

    @Value("${crm-service.incoming-call-code}")
    private String incomingCallCode;

    private final ClientService clientService;
    private final TariffService tariffService;
    private final PhoneNumberService phoneNumberService;

    public ClientInfo getClientInfo(String phoneNumber) {
        Client client = clientService.findByPhoneNumber(phoneNumber);
        ClientDetails clientDetails = client.getClientDetails();
        PhoneNumber phone = client.getPhoneNumber();

        String fio = client.getLastName() + " " + client.getFirstName() + " " + client.getPatronymic();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        return ClientInfo.builder()
                .fio(fio)
                .phoneNumber(phone.getPhoneNumber())
                .numberPersonalAccount(clientDetails.getNumberPersonalAccount())
                .contractDate(clientDetails.getContractDate().format(formatter))
                .region(clientDetails.getRegion())
                .passport(clientDetails.getPassport())
                .birthDate(client.getBirthday().format(formatter))
                .email(clientDetails.getEmail())
                .build();
    }

    @Transactional
    public String changeClientInfo(String phoneNumber, ClientInfo clientInfo) {
        Client client = clientService.findByPhoneNumber(phoneNumber);
        PhoneNumber phone = client.getPhoneNumber();
        ClientDetails clientDetails = client.getClientDetails();
        String[] fio = clientInfo.getFio().split(" ");
        if (fio.length < 2 || fio.length > 3)
            throw new RuntimeException("Invalid fio");
        client.setFirstName(fio[1]);
        client.setLastName(fio[0]);
        client.setPatronymic(fio.length == 3 ? fio[2] : null);

        if (
                !phoneNumber.equals(clientInfo.getPhoneNumber()) &&
                clientService.isPhoneNumberExists(clientInfo.getPhoneNumber())
        )
            throw new RuntimeException("Changeable phone number already exists");
        phone.setPhoneNumber(clientInfo.getPhoneNumber());
        clientDetails.setNumberPersonalAccount(clientInfo.getNumberPersonalAccount());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
        clientDetails.setContractDate(LocalDate.parse(clientInfo.getContractDate(), formatter));
        clientDetails.setRegion(clientInfo.getRegion());
        clientDetails.setPassport(clientInfo.getPassport());
        client.setBirthday(LocalDate.parse(clientInfo.getBirthDate(), formatter));
        clientDetails.setEmail(clientInfo.getEmail());

        clientService.saveClient(client);
        return "Client info successfully changed";
    }

    @Transactional
    public TariffsResponse getAllTariffs() {
        List<Tariff> allTariffs = tariffService.getAllTariffs();
        List<TariffDTO> responseTariffs = new LinkedList<>();
        for (Tariff tariff : allTariffs) {
            responseTariffs.add(buildTariffDTO(tariff));
        }
        return new TariffsResponse(responseTariffs);
    }

    @Transactional
    public ClientTariffResponse getClientTariff(String phoneNumber) {
        Tariff tariff = phoneNumberService.findActiveTariff(phoneNumber);
        return new ClientTariffResponse(buildTariffDTO(tariff));
    }

    private TariffDTO buildTariffDTO(Tariff tariff) {
        List<TariffConfig> tariffDetails = tariff.getTariffConfigList();
        TariffDTO tariffDTO;
        if (tariffDetails == null || tariffDetails.isEmpty()) {
            tariffDTO = TariffDTO.builder()
                    .title(tariff.getTitle())
                    .description(tariff.getDescription())
                    .telephonyPackage(null)
                    .internetPackage(null)
                    .build();
        } else {
            tariffDTO = TariffDTO.builder()
                    .title(tariff.getTitle())
                    .description(tariff.getDescription())
                    .telephonyPackage(extractTelephonyPackInfo(tariffDetails.get(0)))
                    .internetPackage(extractInternetPackInfo(tariffDetails.get(0)))
                    .build();
        }
        return tariffDTO;
    }

    private InternetPackageDTO extractInternetPackInfo(TariffConfig tariffConfig) {
        InternetPackage pack = tariffConfig.getInternetPackage();
        if (pack == null)
            return null;

        return InternetPackageDTO.builder()
                .packOfMB(pack.getPackageOfMb())
                .packCost(pack.getPackageCost().doubleValue())
                .packCostPerMB(pack.getPackageCostPerMb())
                .extraPackCost(pack.getExtraPackageCost().doubleValue())
                .extraPackCostPerMB(pack.getExtraPackageCostPerMb())
                .build();
    }

    private TelephonyPackageDTO extractTelephonyPackInfo(TariffConfig tariffConfig) {
        TelephonyPackage pack = tariffConfig.getTelephonyPackage();
        if (pack == null)
            return null;

        return TelephonyPackageDTO.builder()
                .incomingCall(pack.getCallType() == null ? null : pack.getCallType().getId().equals(incomingCallCode))
                .packOfMinutes(pack.getPackageOfMinutes())
                .packCost(pack.getPackageCost().doubleValue())
                .packCostPerMinute(pack.getPackageCostPerMinute())
                .extraPackCost(pack.getExtraPackageCost().doubleValue())
                .extraPackCostPerMinute(pack.getExtraPackageCostPerMinute())
                .build();
    }
}