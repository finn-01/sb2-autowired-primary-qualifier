package com.spring.sb2autowiredprimaryqualifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Bikini implements Outfit{

    @Override
    public void wear() {
        System.out.println("Bikini");
    }
}
