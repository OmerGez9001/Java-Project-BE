package com.store.backend.controller;

import com.store.backend.component.WordDocumentExporter;
import com.store.backend.data.model.report.RegisterLog;
import com.store.backend.service.ItemLogService;
import com.store.backend.data.dto.SellsPerCategoryReport;
import com.store.backend.data.dto.SellsPerItemReport;
import com.store.backend.data.dto.SellsPerShopReport;
import com.store.backend.service.RegisterLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {
    private final ItemLogService itemLogService;

    private final RegisterLogService registerLogService;

    @GetMapping("/shop")
    public List<SellsPerShopReport> getSellsPerShop() {
        return itemLogService.getSellsPerShop();
    }

    @GetMapping("/category")
    public List<SellsPerCategoryReport> getSellsPerCategory() {
        return itemLogService.getSellsPerCategory();
    }

    @GetMapping("/item")
    public List<SellsPerItemReport> getSellsPerItem() {
        return itemLogService.getSellsPerItem();
    }

    @GetMapping("/register")
    public List<RegisterLog> getAllRegistersLog() {
        return registerLogService.getAll();
    }

}
