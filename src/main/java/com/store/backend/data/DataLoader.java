package com.store.backend.data;

import com.store.backend.service.ItemLogService;
import com.store.backend.data.dto.SellsPerShopReport;
import com.store.backend.data.dto.TransactionDetails;
import com.store.backend.data.dto.ItemTransactionRequest;
import com.store.backend.exception.ItemNotEnoughQuantity;
import com.store.backend.exception.ItemNotExistsInShop;
import com.store.backend.data.model.customer.VipCustomer;
import com.store.backend.data.model.shop.Item;
import com.store.backend.data.model.shop.ItemQuantity;
import com.store.backend.data.model.shop.ItemQuantityKey;
import com.store.backend.data.model.shop.Shop;
import com.store.backend.data.model.worker.Job;
import com.store.backend.data.model.worker.Worker;
import com.store.backend.repository.*;
import com.store.backend.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final WorkerRepository workerRepository;
    private final ShopRepository shopRepository;
    private final ItemRepository itemRepository;
    private final ItemQuantityRepository itemQuantityRepository;
    private final TransactionService transactionService;
    private final CustomerRepository customerRepository;

    private final ItemLogService itemLogService;


    @Override
    public void run(String... args) throws ItemNotExistsInShop, ItemNotEnoughQuantity {
        createShops();
        createWorkers();
        createItems();
        createItemsQuantity();
        createCustomers();
        buy();
        sell();
        log();
    }

    private void log() {
        List<SellsPerShopReport> salesPerShops = itemLogService.getSellsPerShop();
    }

    private void createWorkers() {
        Worker worker = Worker.builder()
                .username("bleicher")
                .password("bleicher")
                .workerId("555")
                .id("208632752")
                .fullName("Itay Bleicher")
                .phoneNumber("0527222646")
                .accountNumber("12345")
                .job(Job.SHIFT_SUPERVISOR)
                .shop(shopRepository.findById(1L).get()).build();
        Worker worker2 = Worker.builder()
                .username("bleicher2")
                .password("bleicher2")
                .workerId("555")
                .id("208632752")
                .fullName("Itay Bleicher")
                .phoneNumber("0527222646")
                .accountNumber("12345")
                .job(Job.SHIFT_SUPERVISOR)
                .shop(shopRepository.findById(1L).get()).build();
        workerRepository.saveAll(Arrays.asList(worker, worker2));
    }

    private void createShops() {
        for (int i = 0; i < 10; i++) {
            Shop shop = Shop.builder().shopName(UUID.randomUUID().toString()).build();
            shopRepository.save(shop);
        }
    }

    private void createItems() {
        for (int i = 0; i < 10; i++) {
            Item item = Item.builder()
                    .name(UUID.randomUUID().toString())
                    .description(UUID.randomUUID().toString())
                    .price(ThreadLocalRandom.current().nextDouble(1, 100)).build();
            itemRepository.save(item);
        }

    }

    private void createItemsQuantity() {
        for (long i = 1L; i <= 10L; i++) {
            for (long j = 1L; j <= 10; j++) {
                ItemQuantity itemQuantity = ItemQuantity.builder()
                        .id(ItemQuantityKey.builder().itemId(j).shopId(i).build())
                        .shop(shopRepository.findById(i).get())
                        .item(itemRepository.findById(j).get())
                        .quantity(1000)
                        .build();
                this.itemQuantityRepository.save(itemQuantity);
            }
        }
    }

    private void createCustomers() {
        VipCustomer vipCustomer = new VipCustomer();
        vipCustomer.setFullName("Bleicherrr");
        vipCustomer.setId("Bleicher");
        vipCustomer.setPhoneNumber("0527222646");
        this.customerRepository.save(vipCustomer);

    }

    private void buy() throws ItemNotExistsInShop, ItemNotEnoughQuantity {
        for (long j = 1; j <= 100; j++) {
            TransactionDetails transactionDetails = new TransactionDetails();
            transactionDetails.setItems(Arrays.asList(new ItemTransactionRequest(ThreadLocalRandom.current().nextInt(1, 10), ThreadLocalRandom.current().nextInt(0, 10)), new ItemTransactionRequest(2L, 3)));
            transactionDetails.setShopId(ThreadLocalRandom.current().nextLong(1, 10));
            transactionDetails.setCustomerUsername("Bleicher");
            transactionService.buy(transactionDetails);
        }


    }

    private void sell() throws ItemNotExistsInShop, ItemNotEnoughQuantity {
        TransactionDetails transactionDetails = new TransactionDetails();
        transactionDetails.setItems(Arrays.asList(new ItemTransactionRequest(3L, 3), new ItemTransactionRequest(2L, 3)));
        transactionDetails.setShopId(1L);
        transactionDetails.setCustomerUsername("Bleicher");
        double v = transactionService.sell(transactionDetails);
    }

}