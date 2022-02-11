package com.spring.sb2autowiredprimaryqualifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class Girl{
    //Uu tien bean khi co 2 bean
    //@Qualifier("bikini")
    @Autowired
    Outfit outfit;


}
