package com.store.backend.service;

import com.store.backend.data.dto.SellsPerItemReport;
import com.store.backend.data.dto.SellsPerShopReport;
import com.store.backend.data.dto.SellsPerCategoryReport;
import com.store.backend.data.model.report.ItemLog;
import com.store.backend.repository.ItemLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemLogService {

    private final ItemLogRepository itemLogRepository;

    private final ShopService shopService;

    private final ItemService itemService;

    public void logItem(ItemLog itemLog) {
        itemLogRepository.save(itemLog);
    }

    public List<SellsPerShopReport> getSellsPerShop() {
        return itemLogRepository
                .fetchSellsPerShop()
                .stream()
                .map(x -> SellsPerShopReport.builder().shop(shopService.getShop(Long.valueOf(x.getSubject()))).sells(x.getCount()).build())
                .sorted(Comparator.comparing(SellsPerShopReport::getSells, Comparator.reverseOrder()).thenComparing(o -> o.getShop().getShopName()))
                .toList();
    }

    public List<SellsPerCategoryReport> getSellsPerCategory() {
        return itemLogRepository
                .fetchSellsPerCategory()
                .stream()
                .map(x -> SellsPerCategoryReport.builder().category(x.getSubject()).count(x.getCount()).build())
                .sorted(Comparator.comparing(SellsPerCategoryReport::getCount, Comparator.reverseOrder()).thenComparing(SellsPerCategoryReport::getCategory))

                .toList();
    }

    public List<SellsPerItemReport> getSellsPerItem() {
        return itemLogRepository
                .fetchSellsPerItem()
                .stream()
                .map(x -> SellsPerItemReport.builder().item(itemService.getItem(Long.valueOf(x.getSubject()))).count(x.getCount()).build())
                .sorted(Comparator.comparing(SellsPerItemReport::getCount, Comparator.reverseOrder()).thenComparing(x -> x.getItem().getName()))
                .toList();
    }

}
