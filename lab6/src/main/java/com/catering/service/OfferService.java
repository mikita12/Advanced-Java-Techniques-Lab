package com.catering.service;

import com.catering.entity.Offer;
import com.catering.repository.OfferRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OfferService {

    private final OfferRepository offerRepository;

    public OfferService(OfferRepository offerRepository) {
        this.offerRepository = offerRepository;
    }

    public Offer save(Offer offer) {
        return offerRepository.save(offer);
    }

    public List<Offer> findAll() {
        return offerRepository.findAll();
    }

    public Optional<Offer> findById(Long id) {
        return offerRepository.findById(id);
    }
}
