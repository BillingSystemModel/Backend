package ru.trkpo.crm.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import ru.trkpo.common.messageBroker.ResponseStatus;
import ru.trkpo.common.messageBroker.ServiceResponse;
import ru.trkpo.crm.TarifficationMessanger;

@RestController
@RequestMapping("/test")
@AllArgsConstructor
public class TestController {

    private final TarifficationMessanger tarifficationMessanger;

    @PatchMapping("/tarifficate")
    public ResponseEntity<Boolean> tarifficate() {
        ServiceResponse response = tarifficationMessanger.requestTariffication();
        if (response.getResponseStatus().equals(ResponseStatus.SUCCESS))
            return ResponseEntity.ok(true);
        else
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, response.getMessage());
    }
}
