package com.store.backend.repository.sql;

import com.store.backend.data.dto.SubjectWithCount;
import com.store.backend.data.model.report.ItemLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemLogRepository extends JpaRepository<ItemLog, String> {

    @Query(value = "select shop_id as subject ,count(subquery) as count from (select shop_id,1 as transactions from transaction_history th group by (shop_id,transaction_id )) as subquery group by (shop_id)\n", nativeQuery = true)
    List<SubjectWithCount> fetchSellsPerShop();


    @Query(value = "select category as subject,count(1) as count from public.transaction_history group by (category)", nativeQuery = true)
    List<SubjectWithCount> fetchSellsPerCategory();

    @Query(value = "select item_id as subject,count(1) as count from public.transaction_history group by (item_id)", nativeQuery = true)
    List<SubjectWithCount> fetchSellsPerItem();
}
