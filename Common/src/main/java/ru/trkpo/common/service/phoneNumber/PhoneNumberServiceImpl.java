package ru.trkpo.common.service.phoneNumber;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;
import ru.trkpo.common.entity.PhoneNumber;

@Service
public class PhoneNumberServiceImpl implements PhoneNumberService {

    private final PhoneNumberRepository phoneNumberRepository;
    private final TransactionTemplate transactionTemplate;

    public PhoneNumberServiceImpl(PhoneNumberRepository phoneNumberRepository, TransactionTemplate transactionTemplate) {
        this.phoneNumberRepository = phoneNumberRepository;
        this.transactionTemplate = transactionTemplate;
    }

    @Override
    public PhoneNumber findRandom() {
        return transactionTemplate.execute(status -> {
            long clientNum = phoneNumberRepository.count();
            int pageNumber = (int)(Math.random() * clientNum);
            Page<PhoneNumber> page = phoneNumberRepository.findAll(PageRequest.of(pageNumber, 1));
            if (! page.hasContent())
                return null;
            return page.getContent().get(0);
        });
    }
}
