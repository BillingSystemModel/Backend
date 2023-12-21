package ru.trkpo.brt.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.trkpo.brt.messageBroker.TarifficationRequestMessanger;
import ru.trkpo.brt.service.cdr.CDRProvider;
import ru.trkpo.brt.service.cdrPlus.CDRPlusCreator;
import ru.trkpo.brt.service.cdrPlus.CDRPlusWriter;
import ru.trkpo.brt.service.tarifficationReport.TarifficationReportProvider;
import ru.trkpo.common.data.CDR;
import ru.trkpo.common.data.CDRPlus;
import ru.trkpo.common.messageBroker.ResponseStatus;
import ru.trkpo.common.messageBroker.ServiceResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TarifficationService {

    private final CDRProvider cdrProvider;
    private final CDRPlusCreator cdrPlusCreator;
    private final CDRPlusWriter cdrPlusWriter;
    private final TarifficationRequestMessanger tarifficationRequestMessanger;

    public ServiceResponse tarifficate() {
        try {
            cdrProvider.init();
            cdrPlusWriter.init();
        } catch (IOException e) {
            return new ServiceResponse(ResponseStatus.CONSUMER_ERROR, e.getMessage());
        }

        List<CDR> cdrList = cdrProvider.getCDRsList();
        for (CDR cdr : cdrList) {
            Optional<CDRPlus> cdrPlus = cdrPlusCreator.createRecord(cdr);
            cdrPlus.ifPresent(record -> {
                try {
                    cdrPlusWriter.write(record);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        return tarifficationRequestMessanger.requestTariffication();
    }
}
