package com.erhankabuk.ShoppingMarketApplication.utility;

import com.erhankabuk.ShoppingMarketApplication.ShoppingMarketApplication;
import com.erhankabuk.ShoppingMarketApplication.dao.StoreDAO;
import com.erhankabuk.ShoppingMarketApplication.repo.BusinessIntegrityException;
import com.erhankabuk.ShoppingMarketApplication.repo.StoreRepository;
import com.erhankabuk.ShoppingMarketApplication.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class StoreItem {
    @Autowired
    ShoppingMarketApplication shoppingMarketApplication;
    @Autowired
    StoreUtility storeUtility;
    @Autowired
    StoreService storeService;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    MainPageUtility mainPageUtility;
    @Autowired
    CartItem cartItem;

    public void getStoreItem() throws BusinessIntegrityException{
        boolean duringAdminIssue = true;
        while (duringAdminIssue){
            storeRepository.storeList();
            String adminCommand = storeUtility.adminCommandList();
                if (adminCommand.equalsIgnoreCase("ADD")){
                    if(!storeRepository.checkStoreList()){
                        System.out.println("\nStore List is empty.Please add first item...\n");
                    }
                    addItemToStore();
                    continue;
                }else if(adminCommand.equalsIgnoreCase("DELETE")){
                    if(!storeRepository.checkStoreList()){
                        continue;
                    }
                    deleteItemFromStore();
                    continue;
                }else if(adminCommand.equalsIgnoreCase("DONE")){
                    if(!storeRepository.checkStoreList()){
                        continue;
                    }
                    doneAdminPage();
                    break;
                }else if(adminCommand.equalsIgnoreCase("QUIT")){
                    storeRepository.storeList();
                    quitFromAdmin();
                }else {
                    System.out.println("\nInvalid command.Retry...\n");
                    continue;
                }
        }
    }

    public void addItemToStore() throws BusinessIntegrityException {
        try{
            storeService.addItemtoDatabase(storeUtility.inputItemNameFromTerminal()
                    ,storeUtility.inputItemPriceFromTerminal()
                    ,storeUtility.inputItemQuantityFromTerminal());
        } catch (BusinessIntegrityException e) {
            System.out.println("...Something went wrong. Retry.\n");
            addItemToStore();
        }
    }

    public void deleteItemFromStore()throws BusinessIntegrityException{
        System.out.println("...Please Enter Item Name for Deleting...\n");
        String input = storeUtility.stringInput();
        try{if(storeRepository.isItemExist(input)){

            System.out.println("\n... " + input + " is deleted from Store List...\n");
            storeService.deleteItemFromDatabase(input);
        }else {System.out.println("...Item didn't existed in store. Retry...\n");
            deleteItemFromStore();
        }} catch (BusinessIntegrityException e) {
            System.out.println("...Something went wrong. Retry.\n");
            deleteItemFromStore();
        }
    }

    public void doneAdminPage()throws BusinessIntegrityException{
        try {
            System.out.println("...Welcome to User Page...\n");
            cartItem.getCartItem();
        } catch (BusinessIntegrityException e) {
            System.out.println("\n...Something went wrong.Retry...\n");
        }
    }

    public void quitFromAdmin() throws BusinessIntegrityException{
        System.out.println("\n...Quit from store...\n");
        System.exit(0);
    }


}
