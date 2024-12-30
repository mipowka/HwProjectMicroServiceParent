package org.example.mainmicroservicebtcip.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "BTCApi", url = "localhost:8082/btc")
public interface BTCApiClientFeign {

    @GetMapping()
    String btc();
}
