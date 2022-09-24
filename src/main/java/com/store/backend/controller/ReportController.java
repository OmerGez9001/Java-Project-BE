package com.store.backend.controller;

import com.store.backend.data.model.report.RegisterLog;
import com.store.backend.data.model.report.RegisterType;
import com.store.backend.service.ItemLogService;
import com.store.backend.data.dto.SellsPerCategoryReport;
import com.store.backend.data.dto.SellsPerItemReport;
import com.store.backend.data.dto.SellsPerShopReport;
import com.store.backend.service.RegisterLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {
    private final ItemLogService itemLogService;

    private final RegisterLogService registerLogService;

    @GetMapping("/shop")
    public ResponseEntity<List<SellsPerShopReport>> getSellsPerShop() {
        return ResponseEntity.ok(itemLogService.getSellsPerShop());
    }

    @GetMapping("/category")
    public ResponseEntity<List<SellsPerCategoryReport>> getSellsPerCategory() {
        return ResponseEntity.ok(itemLogService.getSellsPerCategory());
    }

    @GetMapping("/item")
    public ResponseEntity<List<SellsPerItemReport>> getSellsPerItem() {
        return ResponseEntity.ok(itemLogService.getSellsPerItem());
    }

    @GetMapping("/customer")
    public ResponseEntity<List<RegisterLog>> getAllCustomerLog() {
        return ResponseEntity.ok(registerLogService.getAllByRegisterType(RegisterType.CUSTOMER));
    }

    @GetMapping("/worker")
    public ResponseEntity<List<RegisterLog>> getAllWorkersLog() {
        return ResponseEntity.ok(registerLogService.getAllByRegisterType(RegisterType.WORKER));
    }

}
