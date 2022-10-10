package com.store.backend.data;

import com.store.backend.data.dto.TransactionResult;
import com.store.backend.data.model.customer.NewCustomer;
import com.store.backend.external.ExternalProduct;
import com.store.backend.external.ExternalProductsApi;
import com.store.backend.repository.sql.*;
import com.store.backend.service.*;
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
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {
    private final WorkerService workerService;
    private final ShopService shopService;
    private final ItemService itemService;
    private final ItemQuantityRepository itemQuantityRepository;
    private final TransactionService transactionService;
    private final CustomerService customerService;
    private final ExternalProductsApi externalProductsApi;

    @Override
    public void run(String... args) throws ItemNotExistsInShop, ItemNotEnoughQuantity {
        List<ExternalProduct> externalProducts = externalProductsApi.fetchProducts();
        createShops();
        createWorkers();
        createItems(externalProducts);
        createItemsQuantity();
        createCustomers();
        buy();
        sell();
    }

    private void createWorkers() {
        Shop firstShop = shopService.getShop(1L).get();
        Shop secondShop = shopService.getShop(2L).get();

        Worker worker = Worker.builder()
                .workerId("555")
                .password("bleicher")
                .id("208632752")
                .fullName("Itay Bleicher")
                .phoneNumber("0527222646")
                .accountNumber("12345")
                .job(Job.CASHIER)
                .shop(firstShop).build();
        Worker worker2 = Worker.builder()
                .workerId("5556")
                .password("bleicher2")
                .id("208632752")
                .fullName("Itay Bleicher")
                .phoneNumber("0527222646")
                .accountNumber("12345")
                .job(Job.SELLER)
                .shop(secondShop).build();

        Worker worker3 = Worker.builder()
                .workerId("123")
                .password("123")
                .id("208632754")
                .fullName("Shon Bleicher")
                .phoneNumber("123123123132")
                .accountNumber("123123123")
                .job(Job.SHIFT_SUPERVISOR)
                .shop(secondShop).build();

        Worker worker4 = Worker.builder()
                .workerId("Avi")
                .password("Avi")
                .id("31624587")
                .fullName("Avi Levi")
                .phoneNumber("05472837462")
                .accountNumber("11112211")
                .job(Job.SELLER)
                .shop(firstShop).build();

        Worker worker5 = Worker.builder()
                .workerId("Dan")
                .password("Dan")
                .id("31624587")
                .fullName("Dan Cohen")
                .phoneNumber("0547976473")
                .accountNumber("11112222")
                .job(Job.CASHIER)
                .shop(secondShop).build();

        Worker worker6 = Worker.builder()
                .workerId("Ron")
                .password("Ron")
                .id("31624587")
                .fullName("Ron Yizhak")
                .phoneNumber("0547975555")
                .accountNumber("11112223")
                .job(Job.SHIFT_SUPERVISOR)
                .shop(secondShop).build();

        Arrays.asList(worker, worker2, worker3, worker4, worker5, worker6).forEach(workerService::createWorker);
    }

    private void createShops() {
        long counter = 0L;
        List<String> shopNames = new ArrayList<>();
        shopNames.add("Best Shop");
        shopNames.add("Definitely the Best Shop");
        shopNames.add("Worst Shop");
        shopNames.add("Dont buy here please");
        shopNames.add("No Money Shop");

        for (String shopName : shopNames) {
            Shop shop = Shop.builder().id(++counter).shopName(shopName).build();
            shopService.createShop(shop);
        }
    }

    private void createItems(List<ExternalProduct> externalProducts) {
        externalProducts
                .stream()
                .map(externalProduct ->
                        Item.builder()
                                .description(externalProduct.getDescription())
                                .id(Long.parseLong(externalProduct.getId()))
                                .price(externalProduct.getPrice())
                                .name(externalProduct.getTitle())
                                .category(externalProduct.getCategory()).build())
                .forEach(itemService::createItem);
    }

    private void createItemsQuantity() {
        for (long i = 1L; i <= shopService.shops().size(); i++) {
            Shop shop = shopService.getShop(i).get();
            for (long j = 1L; j <= itemService.items().size(); j++) {
                ItemQuantity itemQuantity = ItemQuantity.builder()
                        .id(ItemQuantityKey.builder().itemId(j).shopId(i).build())
                        .shop(shop)
                        .item(itemService.getItem(j))
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

        NewCustomer newCustomer = new NewCustomer();

        newCustomer.setFullName("Piunkim");
        newCustomer.setId("Bleicher2");
        newCustomer.setPhoneNumber("123123123");

        this.customerService.createCustomer(vipCustomer);
        this.customerService.createCustomer(newCustomer);
    }

    private void buy() throws ItemNotExistsInShop, ItemNotEnoughQuantity {
        for (long j = 1; j <= 100; j++) {
            TransactionDetails transactionDetails = new TransactionDetails();
            transactionDetails.setItems(Arrays.asList(new ItemTransactionRequest(ThreadLocalRandom.current().nextInt(1, Math.toIntExact(itemService.items().size())), ThreadLocalRandom.current().nextInt(1, 10)), new ItemTransactionRequest(2L, 3)));
            transactionDetails.setShopId(ThreadLocalRandom.current().nextLong(1, shopService.shops().size()));
            transactionDetails.setCustomerId("Bleicher");
            transactionService.buy(transactionDetails);
        }
    }

    private void sell() throws ItemNotExistsInShop, ItemNotEnoughQuantity {
        TransactionDetails transactionDetails = new TransactionDetails();
        transactionDetails.setItems(Arrays.asList(new ItemTransactionRequest(3L, 3), new ItemTransactionRequest(2L, 3)));
        transactionDetails.setShopId(1L);
        transactionDetails.setCustomerId("Bleicher");
        TransactionResult v = transactionService.sell(transactionDetails);
    }
}