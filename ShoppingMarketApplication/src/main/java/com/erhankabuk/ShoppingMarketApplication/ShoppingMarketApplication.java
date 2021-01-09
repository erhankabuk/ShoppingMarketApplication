package com.erhankabuk.ShoppingMarketApplication;

import com.erhankabuk.ShoppingMarketApplication.repo.BusinessIntegrityException;
import com.erhankabuk.ShoppingMarketApplication.utility.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class ShoppingMarketApplication implements CommandLineRunner {

	@Autowired
	MainPageUtility mainPageUtility;

	public static void main(String[] args) {
		SpringApplication.run(ShoppingMarketApplication.class, args).close();
	}

	@Override
	public void run(String... args) throws BusinessIntegrityException {
		mainPageUtility.startShopping();
	}

}
