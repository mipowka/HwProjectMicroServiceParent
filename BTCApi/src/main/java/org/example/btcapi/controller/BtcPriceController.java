package org.example.btcapi.controller;

import lombok.RequiredArgsConstructor;
import org.example.btcapi.service.BtcPriceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BtcPriceController {

    private final BtcPriceService btcPriceService;

    @GetMapping("/btc")
    public String getPrice() {
        return btcPriceService.getDataFromUrl();
    }
}
