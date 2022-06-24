package com.dilmurod.component;


import com.dilmurod.entity.DeliveryType;
import com.dilmurod.entity.DocSender;
import com.dilmurod.repository.DeliveryTypeRepository;
import com.dilmurod.repository.DocSenderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class DataLoader implements CommandLineRunner {

    private final DocSenderRepository docSenderRepository;
    private final DeliveryTypeRepository deliveryTypeRepository;

    @Value("${spring.sql.init.mode}")
    String initMode;

    public DataLoader(DocSenderRepository docSenderRepository, DeliveryTypeRepository deliveryTypeRepository) {
        this.docSenderRepository = docSenderRepository;
        this.deliveryTypeRepository = deliveryTypeRepository;
    }


    @Override
    public void run(String... args) throws Exception {


        if (initMode.equals("always")) {

            deliveryTypeRepository.save(new DeliveryType(1, "Courier", true));
            deliveryTypeRepository.save(new DeliveryType(2, "Email", true));
            deliveryTypeRepository.save(new DeliveryType(3, "Telephone Message", true));

            docSenderRepository.save(new DocSender(1, "GNI", true));
            docSenderRepository.save(new DocSender(2, "TSJ", true));
            docSenderRepository.save(new DocSender(3, "CB", true));

        }
    }
}
