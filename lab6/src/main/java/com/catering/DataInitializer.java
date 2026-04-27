package com.catering;

import com.catering.entity.Client;
import com.catering.entity.Offer;
import com.catering.entity.OfferType;
import com.catering.repository.ClientRepository;
import com.catering.repository.OfferRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    private final OfferRepository offerRepository;
    private final ClientRepository clientRepository;

    public DataInitializer(OfferRepository offerRepository, ClientRepository clientRepository) {
        this.offerRepository = offerRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public void run(String... args) {
        if (offerRepository.count() == 0) {
            offerRepository.save(new Offer(OfferType.BREAKFAST, "Classic Breakfast", new BigDecimal("15.00")));
            offerRepository.save(new Offer(OfferType.BREAKFAST, "Vegan Breakfast",   new BigDecimal("18.00")));
            offerRepository.save(new Offer(OfferType.LUNCH,     "Chicken & Rice",     new BigDecimal("25.00")));
            offerRepository.save(new Offer(OfferType.LUNCH,     "Pasta Bolognese",    new BigDecimal("22.00")));
            offerRepository.save(new Offer(OfferType.DINNER,    "Grilled Salmon",     new BigDecimal("40.00")));
            offerRepository.save(new Offer(OfferType.DINNER,    "Beef Steak",         new BigDecimal("55.00")));
        }
        if (clientRepository.count() == 0) {
            clientRepository.save(new Client("Jan",  "Kowalski", "CLI001"));
            clientRepository.save(new Client("Anna", "Nowak",    "CLI002"));
        }
    }
}
