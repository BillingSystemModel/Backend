package ru.trkpo.common.service.phoneNumber;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.trkpo.common.data.entity.PhoneNumber;
import ru.trkpo.common.data.entity.Tariff;
import ru.trkpo.common.exception.NoDataFoundException;

@Service
public class PhoneNumberServiceImpl implements PhoneNumberService {

    private final PhoneNumberRepository repository;
    private final TransactionTemplate transactionTemplate;

    public PhoneNumberServiceImpl(PhoneNumberRepository repository, TransactionTemplate transactionTemplate) {
        this.repository = repository;
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public PhoneNumber findByPhoneNumber(String phoneNumber) {
        return transactionTemplate.execute(status ->
                repository.findByPhoneNumber(phoneNumber).orElseThrow(
                        () -> new NoDataFoundException("No such client")
                )
        );
    }

    @Override
    public PhoneNumber findRandom() {
        return transactionTemplate.execute(status -> {
            long clientNum = repository.count();
            int pageNumber = (int)(Math.random() * clientNum);
            Page<PhoneNumber> page = repository.findAll(PageRequest.of(pageNumber, 1));
            if (! page.hasContent())
                return null;
            return page.getContent().get(0);
        });
    }

    @Override
    public Tariff findActiveTariff(String phoneNumber) {
        return transactionTemplate.execute(status -> {
            PhoneNumber phone = repository.findByPhoneNumber(phoneNumber).orElseThrow(
                    () -> new NoDataFoundException("No such client")
            );
            return phone.getTariff();
        });
    }
}
