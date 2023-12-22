package ru.trkpo.common.service.tariff;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import ru.trkpo.common.data.CDRPlus;
import ru.trkpo.common.data.entity.Tariff;
import ru.trkpo.common.data.entity.TariffConfig;
import ru.trkpo.common.data.entity.TelephonyPackage;
import ru.trkpo.common.exception.NoDataFoundException;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class TariffServiceImpl implements TariffService {

    private final TariffRepository tariffRepository;
    private final TransactionTemplate transactionTemplate;

    @Override
    public Tariff findByCode(String code) {
        return transactionTemplate.execute(status -> tariffRepository.findByIdEquals(code)
                .orElseThrow(() -> new NoDataFoundException("There is no such tariff")));
    }

    @Override
    public BigDecimal applyTariff(CDRPlus cdrPlus) {
        Tariff tariff = transactionTemplate.execute(status ->
                tariffRepository.findByIdEquals(cdrPlus.getCallTypeCode())
                        .orElseThrow(() -> new NoDataFoundException("There is no such tariff")));

        List<TariffConfig> tariffConfigList = tariff.getTariffConfigList();
        BigDecimal totalCost = BigDecimal.ZERO;
        for (TariffConfig config : tariffConfigList) {
            TelephonyPackage telePackage = config.getTelephonyPackage();
            double packageCost = telePackage.getPackageCost().doubleValue();
            int packageOfMinutes = telePackage.getPackageOfMinutes();

            long callTime = cdrPlus.getDuration().toMinutes();
            if (telePackage.getPackageCostPerMinute()) {
                totalCost = callTime > packageOfMinutes ?
                        totalCost.add(new BigDecimal(packageCost * packageOfMinutes)) :
                        totalCost.add(new BigDecimal(packageCost * callTime));
            } else {
                totalCost = totalCost.add(new BigDecimal(packageCost));
            }

            callTime -= packageOfMinutes;
            if (callTime > 0) {
                double extraPackageCost = telePackage.getExtraPackageCost().doubleValue();
                totalCost = telePackage.getExtraPackageCostPerMinute() ?
                        totalCost.add(new BigDecimal(extraPackageCost * callTime)) :
                        totalCost.add(new BigDecimal(extraPackageCost));
            }
        }
        return totalCost;
    }
}