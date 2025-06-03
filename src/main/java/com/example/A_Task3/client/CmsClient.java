package com.example.A_Task3.client;

import com.example.A_Task3.client.dtos.TransactionRequest;
import com.example.A_Task3.client.dtos.TransactionResponse;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "cms-service", url = "http://localhost:8082")
public interface CmsClient {
    @PostMapping("/transactions")
    TransactionResponse createTransaction(@RequestBody TransactionRequest request);
}
