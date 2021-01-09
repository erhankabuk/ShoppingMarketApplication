package com.erhankabuk.ShoppingMarketApplication.dao;

import com.erhankabuk.ShoppingMarketApplication.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface StoreDAO extends JpaRepository<Store, Long> {
Store findByItem(String itemFromDB);




}
