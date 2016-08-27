package com.ysw.adsense.service;

import com.ysw.adsense.domain.AdQR;


public interface AdQRService {

    AdQR create(AdQR qr);
    
    AdQR findByQrTicket(String ticket);
}
