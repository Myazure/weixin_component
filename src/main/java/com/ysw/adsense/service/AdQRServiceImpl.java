package com.ysw.adsense.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ysw.adsense.domain.AdQR;
import com.ysw.adsense.repository.AdQRRepository;


@Service
public class AdQRServiceImpl implements AdQRService {


    private final AdQRRepository qrRepository;

    @Autowired
    public AdQRServiceImpl(AdQRRepository qrRepository) {
        this.qrRepository = qrRepository;
    }
    
    @Override
    public AdQR create(AdQR qr) {
        return qrRepository.save(qr);
    }

	@Override
	public AdQR findByQrTicket(String ticket) {
		return qrRepository.findByQrTicket(ticket);
	}
}
