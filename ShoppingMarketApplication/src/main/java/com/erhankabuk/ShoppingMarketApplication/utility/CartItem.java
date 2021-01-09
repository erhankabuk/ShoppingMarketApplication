package com.erhankabuk.ShoppingMarketApplication.utility;
import com.erhankabuk.ShoppingMarketApplication.dao.StoreDAO;
import com.erhankabuk.ShoppingMarketApplication.repo.BusinessIntegrityException;
import com.erhankabuk.ShoppingMarketApplication.repo.StoreRepository;
import com.erhankabuk.ShoppingMarketApplication.service.StoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Component
public class CartItem {

    @Autowired
    CartUtility cartUtility;
    @Autowired
    StoreDAO storeDAO;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    StoreService storeService;

    static String itemName;
    static String userCommand;
    static int itemQuantityInCart;
    static BigDecimal wallet;
    public Map<String, Integer> cartItem= new HashMap<>();

    public Map<String, Integer> getCartItem() throws BusinessIntegrityException {
        wallet = cartUtility.inputWalletPrice();
        boolean duringShopping = true;
        while(duringShopping) {
            cartUtility.userWallet();
            storeRepository.storeList();
            cartUtility.userCommands();
            userCommand=cartUtility.inputUserCommand();
            if(userCommand.equalsIgnoreCase("ADD")){
                addMethod();
                cartUtility.checkoutCartList();
                cartUtility.cartTotalPrice();
                continue;
            }else if(userCommand.equalsIgnoreCase("DELETE")){
                if (!(cartItem.size()>0)){
                    cartUtility.emptyCartWarning();
                    continue;
                }
                deleteMethod();
                cartUtility.checkoutCartList();
                cartUtility.cartTotalPrice();
                cartUtility.remainingMoney();
                continue;

            }else if(userCommand.equalsIgnoreCase("EDIT")){
                if (!(cartItem.size()>0)){
                    cartUtility.emptyCartWarning();
                    continue;
                }
                editMethod();
                cartUtility.checkoutCartList();
                cartUtility.cartTotalPrice();
                continue;
            }else if(userCommand.equalsIgnoreCase("CHECKOUT")){
                cartUtility.checkoutCartList();
                cartUtility.cartTotalPrice();
                cartUtility.remainingMoney();
                continue;
            }else if(userCommand.equalsIgnoreCase("DONE")){
                if (!(cartItem.size()>0)){
                    cartUtility.emptyCartWarning();
                    continue;
                }
                doneMethod();
                System.exit(0);
            }else if(userCommand.equalsIgnoreCase("QUIT")){
                System.out.println("...Quit from shopping without buy anything...\n ");
                System.exit(0);
            }else{
                System.out.println("\n...Invalid command.Retry...\n");
                continue;
            }
        }
        return getCartItem();
    }

    public void addMethod()throws BusinessIntegrityException{
        System.out.println("...Please enter item name...");
        itemName=cartUtility.inputItemNameFromUser();
        while (Objects.equals(cartUtility.checkItemExistedInCart(), false)){
        System.out.println("\n...Please enter item quantity...");
        itemQuantityInCart=cartUtility.inputItemQuantityFromUser();
        if (itemQuantityInCart==0){
            System.out.println("...Added quantity for "+itemName+" can not be 0...Retry...\n ");
            break;
        }else if (itemQuantityInCart<0){
            break;
        }
        System.out.println("\n");
        if(cartUtility.cartAmount()>=0) {
            cartItem.put(itemName,itemQuantityInCart);
            if(cartUtility.remainingMoney().compareTo(BigDecimal.ZERO)<0) {
                cartItem.remove(itemName);
                System.out.println("..." + itemQuantityInCart+ " " + itemName + " can not added to cart...\n");
            }else {
                System.out.println("..." + itemQuantityInCart + " " + itemName + " are added to cart...\n");
            }
        }
        break;
        }
    }

    public void editMethod()throws BusinessIntegrityException {
        boolean editting = true;
        while (editting){
        System.out.println("...Please Enter Item Name for Editing...\n");
        String editedItemName= cartUtility.inputItemNameFromUser();
        if(!cartItem.containsKey(editedItemName)){
            System.out.println("... "+editedItemName+ " doesn't existed in cart. Please Retry...\n");
            break;
        }
        System.out.println("...Please Enter Quantity for Editing "+ editedItemName+" ...\n");
        int editedItemQuantity =cartUtility.inputItemQuantityFromUser();
        if (editedItemQuantity==0){
            cartItem.remove(editedItemName);
            System.out.println("..." + editedItemName + " is deleted from cart...\n");
            cartUtility.remainingMoney();
        }else if((editedItemQuantity>storeDAO.findByItem(editedItemName).getQuantity())){
            System.out.println("...User can not edit quantity of "+ editedItemName
                    + " more than "+ storeDAO.findByItem(editedItemName).getQuantity() + "...\n...Please Retry...\n");
            cartUtility.remainingMoney();
        }else if(cartItem.containsKey(editedItemName)
                &&(editedItemQuantity<=storeDAO.findByItem(editedItemName).getQuantity())){
           int itemQuantityBeforeEditing= cartItem.get(editedItemName);
            cartItem.replace(editedItemName,editedItemQuantity);
            if(cartUtility.remainingMoney().compareTo(BigDecimal.ZERO) <0){
                System.out.println("... "+ editedItemName + " can not be editted...\n");
                cartItem.replace(editedItemName,itemQuantityBeforeEditing);
                break;
            }
            System.out.println("...Quantity of " + editedItemName + " is edited as "
                    + editedItemQuantity + " ...\n");
        }else {System.out.println("Something went wrong with editting...\n");
        }break;
        }
    }

    public void deleteMethod() throws BusinessIntegrityException{
        System.out.println("...Please Enter Item Name for deleting...\n");
        String deletedItemName =cartUtility.inputItemNameFromUser();
        cartItem.remove(deletedItemName);
        System.out.println("..."+deletedItemName+ " deleted...\n");
    }

    public void doneMethod() throws BusinessIntegrityException{
        cartUtility.checkoutCartList();
        cartUtility.cartTotalPrice();
        cartUtility.remainingMoney();
        cartUtility.userWallet();

        for (String itemInCart : cartItem.keySet()) {
            int updatedQuantityFromCartToStore = (storeDAO.findByItem(itemInCart).getQuantity()-cartItem.get(itemInCart));
            if(updatedQuantityFromCartToStore==0){
                storeService.deleteItemFromDatabase(itemInCart);
            }else{storeService.updateItemFromCartToStore(itemInCart, updatedQuantityFromCartToStore);}
        }
        storeRepository.storeList();
        System.out.println("...Shopping is done...\n");
    }

}
