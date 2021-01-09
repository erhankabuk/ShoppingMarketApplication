package com.erhankabuk.ShoppingMarketApplication.utility;

import com.erhankabuk.ShoppingMarketApplication.dao.StoreDAO;
import com.erhankabuk.ShoppingMarketApplication.repo.BusinessIntegrityException;
import com.erhankabuk.ShoppingMarketApplication.repo.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.NumberUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.Scanner;

import static com.erhankabuk.ShoppingMarketApplication.utility.CartItem.*;

@Component
public class CartUtility {
    @Autowired
    StoreDAO storeDAO;
    @Autowired
    CartItem cartItemClass;
    @Autowired
    StoreRepository storeRepository;
    Scanner scanner = new Scanner(System.in);

    public String stringInput() throws BusinessIntegrityException{
        String input =scanner.nextLine();
        try{if(!input.isEmpty()){
            return input;
        }else {return inputItemNameFromUser();}
        } catch (BusinessIntegrityException e) {
            System.out.println("...Empty value...Retry...\n");
            return inputItemNameFromUser();
        }
    }

    public int integerInput() throws BusinessIntegrityException{
        String input = scanner.nextLine();
        try {int convertedInput=Integer.parseInt(input);
            if(convertedInput>=0){
            return convertedInput;
            }else{
                System.out.println("...Must be positive integer... Retry...\n");
            return inputItemQuantityFromUser();
            }
        } catch (Exception e) {
            System.out.println("...Invalid value... Retry...\n");
        }return inputItemQuantityFromUser();
    }

    public BigDecimal inputWalletPrice()throws BusinessIntegrityException {
        System.out.println("...Please enter wallet price for shopping...\n");
        String input = scanner.nextLine();
        try{
            if (!(NumberUtils.parseNumber(input, BigDecimal.class).compareTo(BigDecimal.ZERO)>0)) {
                System.out.println("...Invalid value... Retry...\n");
                return inputWalletPrice();
            }else return NumberUtils.parseNumber(input, BigDecimal.class);
        } catch (Exception e) {
            System.out.println("...Invalid value... Retry...\n");
        } return inputWalletPrice();
    }

    public String inputUserCommand()throws BusinessIntegrityException{
        String input = stringInput();
        return input;
    }

    public String inputItemNameFromUser() throws BusinessIntegrityException {
        String input = stringInput();
        if(storeRepository.isItemExist(input)){
            String inputFromStore =storeDAO.findByItem(input).getItem();
            return inputFromStore;
        }else {System.out.println("...Item doesn't existed or invalid command.Retry...\n");
            return inputItemNameFromUser();
        }
    }

    public int inputItemQuantityFromUser() throws BusinessIntegrityException{
        return integerInput();
    }

    public void emptyCartWarning() throws BusinessIntegrityException{
        System.out.println("...There is any Item in User's Cart...\n");
    }

    public void userCommands() throws BusinessIntegrityException{
        System.out.println("\nChoose command below\n"
                + "... ADD      => Add any item to cart...\n"
                + "... DELETE   => Delete any item from cart...\n"
                + "... EDIT     => Edit quantity of any item from cart...\n"
                + "... CHECKOUT => Checkout all items and quantity from cart...\n"
                + "... DONE     => Finish to shopping...\n"
                + "... QUIT     => Quit from shopping without buy anything...\n");
    }

    public Boolean checkItemExistedInCart()throws BusinessIntegrityException{
        if (cartItemClass.cartItem.containsKey(itemName)) {
            System.out.println("..." + itemName + " is already added to cart...\n"
                    +"...For adding new quantity, please enter EDIT...\n ");
            return true;
        }else return false;
    }
    public BigDecimal getCartTotal() throws BusinessIntegrityException{
        BigDecimal itemPriceInStore;
        BigDecimal itemTotal;
        BigDecimal midTotal = BigDecimal.ZERO;
        BigDecimal cartTotal = BigDecimal.ZERO;
        for (String itemInCart : cartItemClass.cartItem.keySet()) {
            itemPriceInStore = storeDAO.findByItem(itemInCart).getPrice();
            itemTotal = itemPriceInStore.multiply(BigDecimal.valueOf(cartItemClass.cartItem.get(itemInCart)));
            cartTotal =itemTotal.add(midTotal);
            midTotal=cartTotal;
        }
        BigDecimal intCartTotal=cartTotal;
        return intCartTotal;
    }

    public void cartTotalPrice() throws BusinessIntegrityException{
        System.out.println("\n...Total Price : " + getCartTotal() + " Euros...\n");
    }

    public BigDecimal remainingMoney() throws BusinessIntegrityException{
        BigDecimal remainingMoney = wallet.subtract(getCartTotal());
        if(!(remainingMoney.compareTo(BigDecimal.ZERO)>=0)){
            System.out.println("...There isn't enough money in User wallet.Retry...\n");
        }else{
            System.out.println("...Remaining money "+ remainingMoney.setScale(2,RoundingMode.CEILING) + " Euros...\n" );
        }
        return remainingMoney;
    }

    public int cartAmount() throws BusinessIntegrityException {
        int addedItemQuantityFromStore = storeDAO.findByItem(itemName).getQuantity();
        if(itemQuantityInCart>addedItemQuantityFromStore){
            System.out.println("...User can not add quantity of "+ itemName
                    + " more than "+ storeDAO.findByItem(itemName).getQuantity() + "...\n");
            return itemQuantityInCart=-1;
        }else if(cartItemClass.cartItem.size()<=0){
            System.out.println("...Cart is empty...\n");
        }
        return itemQuantityInCart;
    }
    public void checkoutCartList() throws BusinessIntegrityException{
        if(!(cartItemClass.cartItem.size()>0)){
            emptyCartWarning();
        }
        System.out.println("...Cart List...");
        for (String itemInCart : cartItemClass.cartItem.keySet()) {
            BigDecimal addedItemPrice=storeDAO.findByItem(itemInCart).getPrice();
            BigDecimal itemTotalPrice = addedItemPrice.multiply(BigDecimal.valueOf(cartItemClass.cartItem.get(itemInCart)));
            System.out.println("... " + cartItemClass.cartItem.get(itemInCart) + " " + itemInCart
                    + " => Total Price " + itemTotalPrice + " Euros...");
        }
    }
    public void userWallet() throws BusinessIntegrityException{
        System.out.println("...User has " + wallet.setScale(2, RoundingMode.CEILING) +" Euros in wallet...\n");
    }

}
