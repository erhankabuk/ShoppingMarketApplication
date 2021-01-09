package com.erhankabuk.ShoppingMarketApplication.service;

import com.erhankabuk.ShoppingMarketApplication.ShoppingMarketApplication;
import com.erhankabuk.ShoppingMarketApplication.repo.BusinessIntegrityException;
import com.erhankabuk.ShoppingMarketApplication.repo.StoreRepository;
import com.erhankabuk.ShoppingMarketApplication.utility.StoreUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
@Component
public class StoreService {

    @Autowired
    StoreRepository storeRepository;
    @Autowired
    ShoppingMarketApplication shoppingMarketApplication;
    @Autowired
    StoreUtility storeUtility;

    public void addItemtoDatabase(String inputItemFromAdmin,BigDecimal inputPriceByAdmin, int inputQuantityByAdmin) throws BusinessIntegrityException {
        storeRepository.addAllData(inputItemFromAdmin,inputPriceByAdmin,inputQuantityByAdmin);
    }

    public void deleteItemFromDatabase(String input) throws BusinessIntegrityException {
        storeRepository.deleteAllData(input);
    }

    public void updateItemFromCartToStore(String inputItemFromUser,int inputQuantityFromUser)throws BusinessIntegrityException {
        storeRepository.updatedItemFromCartToStore(inputItemFromUser, inputQuantityFromUser);
    }

}






