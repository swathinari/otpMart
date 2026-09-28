package mvp_mart.paymentm_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PaymentmServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentmServiceApplication.class, args);
        System.out.println("payment service executed successfully");
	}

}
