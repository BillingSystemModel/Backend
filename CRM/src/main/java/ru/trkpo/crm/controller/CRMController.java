package ru.trkpo.crm.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.trkpo.common.data.dto.TarifficationReportDTO;
import ru.trkpo.common.messageBroker.ResponseStatus;
import ru.trkpo.common.messageBroker.ServiceResponse;
import ru.trkpo.common.service.callsReport.CallsReportService;
import ru.trkpo.common.service.phoneNumber.PhoneNumberService;
import ru.trkpo.crm.TarifficationMessanger;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class CRMController {

    private final TarifficationMessanger tarifficationMessanger;
    private final PhoneNumberService phoneNumberService;
    private final CallsReportService callsReportService;

    @PatchMapping("/tarifficate")
    public ResponseEntity<Boolean> tarifficate() {
        ServiceResponse response = tarifficationMessanger.requestTariffication();
        if (response.getResponseStatus().equals(ResponseStatus.SUCCESS))
            return ResponseEntity.ok(true);
        else
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, response.getMessage());
    }

    @PatchMapping("/pay")
    public ResponseEntity<BigDecimal> pay(@RequestParam String phoneNumber, @RequestParam double moneyAdd) {
        BigDecimal money = BigDecimal.valueOf(moneyAdd);
        return ResponseEntity.ok(phoneNumberService.updateBalance(phoneNumber, money));
    }

    @PatchMapping("/changeTariff")
    public ResponseEntity<String> changeTariff(@RequestParam String phoneNumber, @RequestParam String tariffCode) {
        return ResponseEntity.ok(phoneNumberService.changeTariff(phoneNumber, tariffCode));
    }

    @GetMapping("/report")
    public ResponseEntity<TarifficationReportDTO> getReport(@RequestParam String phoneNumber,
                                                            @RequestParam LocalDateTime dateTimeStart,
                                                            @RequestParam LocalDateTime dateTimeEnd) {
        Optional<TarifficationReportDTO> callsReport = callsReportService.getCallsReport(
                phoneNumber,
                dateTimeStart,
                dateTimeEnd);
        if (callsReport.isPresent()) {
            TarifficationReportDTO report = callsReport.get();
            report.setPhoneNumber(phoneNumber);
        }
        return callsReport.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.ok(null));
    }
}