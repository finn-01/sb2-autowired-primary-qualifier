package com.spring.sb2autowiredprimaryqualifier;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
//Uu tien bean khi co primary
@Primary
public class Naked implements Outfit{
    @Override
    public void wear() {
        System.out.println("Naked");
    }
}
