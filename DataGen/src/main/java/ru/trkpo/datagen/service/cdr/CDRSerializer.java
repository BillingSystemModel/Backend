package ru.trkpo.datagen.service.cdr;

import org.springframework.stereotype.Service;
import ru.trkpo.common.data.CDR;
import ru.trkpo.common.service.Serializer;

@Service
public class CDRSerializer implements Serializer<CDR> {

    @Override
    public String serialize(CDR item) {
        return item.getCallTypeCode() +
                ", " +
                item.getPhoneNumber() +
                ", " +
                item.getStartDateTime() +
                ", " +
                item.getEndDateTime() +
                '\n';
    }
}
