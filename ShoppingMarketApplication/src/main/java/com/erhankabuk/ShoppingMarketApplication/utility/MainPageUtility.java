package com.erhankabuk.ShoppingMarketApplication.utility;

import com.erhankabuk.ShoppingMarketApplication.dao.StoreDAO;
import com.erhankabuk.ShoppingMarketApplication.repo.BusinessIntegrityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainPageUtility {

    @Autowired
    StoreDAO storeDAO;
    @Autowired
    StoreUtility storeUtility;
    @Autowired
    CartItem cartItem;
    @Autowired
    StoreItem storeItem;

    public void startShopping()throws BusinessIntegrityException {
        System.out.println("...For admin enter 'ADMIN'; for user enter 'USER'...\n");
        String input = storeUtility.stringInput();
            if(input.equalsIgnoreCase("ADMIN")){
                System.out.println("...Welcome Admin Page...\n");
                storeItem.getStoreItem();
            }else if(input.equalsIgnoreCase("USER")){
                if (!(storeDAO.findAll().size() >0)){
                    System.out.println("...Store List is empty.Admin must add item in store...\n");
                    startShopping();
                }
                System.out.println("...Welcome User Page...\n");
                cartItem.getCartItem();
            }else{
                System.out.println("...Invalid command. Retry...\n");
                startShopping();
            }
    }
}
