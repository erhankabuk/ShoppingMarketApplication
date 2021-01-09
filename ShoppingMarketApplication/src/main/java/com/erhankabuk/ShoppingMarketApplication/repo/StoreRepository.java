package com.erhankabuk.ShoppingMarketApplication.repo;

import com.erhankabuk.ShoppingMarketApplication.dao.StoreDAO;
import com.erhankabuk.ShoppingMarketApplication.model.Store;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Repository
public class StoreRepository {

    @Autowired
    StoreDAO storeDAO;

    public boolean isItemExist(Object inputFromTerminal) throws BusinessIntegrityException {
        Store marketItem = storeDAO.findByItem(inputFromTerminal.toString());
        return Objects.nonNull(marketItem);
    }

    public boolean checkStoreList()throws BusinessIntegrityException{
        if (!(storeDAO.findAll().size() >0)){
            return false;
        }
        return true;
    }

    public void addAllData(String inputItemFromAdmin, BigDecimal inputPriceByAdmin, int inputQuantityByAdmin) throws BusinessIntegrityException {
        Store marketItem = new Store();
        marketItem.setItem(inputItemFromAdmin);
        marketItem.setPrice(inputPriceByAdmin);
        marketItem.setQuantity(inputQuantityByAdmin);
        storeDAO.save(marketItem);
    }

    public void deleteAllData(String inputFromAdmin) throws BusinessIntegrityException {
        Store marketItem = storeDAO.findByItem(inputFromAdmin);
        storeDAO.delete(marketItem);
    }

    public void storeList() throws BusinessIntegrityException {
        List<Store> itemList = storeDAO.findAll();
        if(!checkStoreList()){
            System.out.println("\n...Store List is empty...\n");
        }else {
            System.out.println("...Store List...");
            for (Store item : itemList) {
                System.out.println("..." + item.getQuantity() + " "
                        + item.getItem() + " existed in store "
                        + item.getPrice() + " Euros/per...");
            }
        }
    }

    public void updatedItemFromCartToStore(String itemNameFromUser, int updatedItemQuantity) throws BusinessIntegrityException{
        Store marketItem = storeDAO.findByItem(itemNameFromUser);
        marketItem.setQuantity(updatedItemQuantity);
        storeDAO.save(marketItem);
    }

}



























