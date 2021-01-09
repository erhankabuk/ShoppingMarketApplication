package com.erhankabuk.ShoppingMarketApplication.utility;
import com.erhankabuk.ShoppingMarketApplication.ShoppingMarketApplication;
import com.erhankabuk.ShoppingMarketApplication.repo.BusinessIntegrityException;
import com.erhankabuk.ShoppingMarketApplication.repo.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

@Component
public class StoreUtility {
    @Autowired
    ShoppingMarketApplication shoppingMarketApplication;
    @Autowired
    StoreItem storeItem;
    @Autowired
    StoreRepository storeRepository;
    Scanner scanner = new Scanner(System.in);

    public String  stringInput() throws BusinessIntegrityException{
           String input =scanner.nextLine();
           try{if(!input.isEmpty()){
                   return input;
               }else {return  inputItemNameFromTerminal();}
           } catch (BusinessIntegrityException e) {
               System.out.println("...Empty value...Retry...\n");
               return inputItemNameFromTerminal();
           }
    }

    public BigDecimal bigDecimalInput() throws BusinessIntegrityException {
        String input = scanner.nextLine();
        try{BigDecimal convertedInput = new BigDecimal(input);
            if (!(convertedInput.compareTo(BigDecimal.ZERO) > 0)) {
                System.out.println("...Invalid value... Retry...\n");
                return inputItemPriceFromTerminal();
            }else return convertedInput;
        } catch (Exception e) {
            System.out.println("...Invalid value... Retry...\n");
        } return inputItemPriceFromTerminal();
    }

    public int integerInput() throws BusinessIntegrityException{
        String input = scanner.nextLine();
        try {int convertedInput=Integer.parseInt(input);
            if(convertedInput>=0){
                return convertedInput;
            }else System.out.println("...Invalid value... Retry...\n");
        } catch (Exception e) {
            System.out.println("...Invalid value... Retry...\n");
        }return inputItemQuantityFromTerminal();

    }

    public String adminCommandList()throws BusinessIntegrityException{
        System.out.println("\nChoose command below\n"
                + "... ADD      => Add any item to store...\n"
                + "... DELETE   => Delete any item from store...\n"
                + "... DONE     => Save all items in store...\n"
                + "... QUIT     => Quit from store without adding any item...\n");
        String input = stringInput();
        return input;
    }

    public String inputItemNameFromTerminal() throws BusinessIntegrityException {
        System.out.println("...Please Enter Item Name...\n");
        String input = stringInput();
        if(!storeRepository.isItemExist(input)){
            return input;
        } else {
            System.out.println("...Item is already existed.Retry...\n");
            return inputItemNameFromTerminal();
        }
    }

    public BigDecimal inputItemPriceFromTerminal()throws BusinessIntegrityException {
        System.out.println("...Please enter item price...\n");
        return bigDecimalInput();
    }

    public int inputItemQuantityFromTerminal() throws BusinessIntegrityException{
        System.out.println("...Please enter item quantity...\n");
        return integerInput();
    }

}
