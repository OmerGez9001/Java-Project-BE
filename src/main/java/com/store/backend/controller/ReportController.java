package com.store.backend.controller;

import com.store.backend.component.WordDocumentExporter;
import com.store.backend.service.ItemLogService;
import com.store.backend.data.dto.SellsPerCategoryReport;
import com.store.backend.data.dto.SellsPerItemReport;
import com.store.backend.data.dto.SellsPerShopReport;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/report")
@RequiredArgsConstructor
public class ReportController {
    private final ItemLogService itemLogService;
    private final WordDocumentExporter exporter;

    @GetMapping("/shop")
    public List<SellsPerShopReport> getSellsPerShop() {
        return itemLogService.getSellsPerShop();
    }

    @GetMapping("/category")
    public List<SellsPerCategoryReport> getSellsPerCategory(@RequestParam String doc) {
        return itemLogService.getSellsPerCategory();
    }

    @GetMapping("/item")
    public List<SellsPerItemReport> getSellsPerItem() {
        return itemLogService.getSellsPerItem();
    }

    @GetMapping("/shop/doc")
    public void getSellsPerShopDoc() throws IOException {
        exporter.createWordDocForShops(itemLogService.getSellsPerShop());
    }

    @GetMapping("/category/doc")
    public void getSellsPerCategoryDoc() throws IOException {
        exporter.createWordDocForCategory(itemLogService.getSellsPerCategory());
    }

    @GetMapping("/item/doc")
    public void getSellsPerItemDoc() throws IOException {
        exporter.createWordDocForItems(itemLogService.getSellsPerItem());
    }
}
