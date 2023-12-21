package ru.trkpo.common.service.tariff;

import ru.trkpo.common.data.CDRPlus;
import ru.trkpo.common.data.entity.Tariff;

import java.math.BigDecimal;

public interface TariffService {
    Tariff findByCode(String code);

    BigDecimal applyTariff(CDRPlus cdrPlus);
}
