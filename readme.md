#Autowired - Primary - Qualifier
##-------------------------------------------------

##@Autowired: 
để đánh dấu cho **Spring** biết rằng sẽ tự động inject bean tương ứng vào vị trí được đánh dấu.

##@Component:
Đánh dấu đó là 1 bean

```sh
@Component
public class Girl {
    // Đánh dấu để Spring inject một đối tượng Outfit vào đây
    @Autowired
    Outfit outfit;

    // public Girl(Outfit outfit) {
    //     this.outfit = outfit;
    // }

    // GET
    // SET
}
```

Sau khi tìm thấy một Class đánh dấu là **@Component** thì quá trình inject Bean xảy ra như sau:

- Nếu Class không có hàm **Constructor** hay **Setter** thì java sẽ tự reflection để đối tượng  vào thuộc tính có đánh dấu là @Autowired
- Nếu hàm có **Constructor** thì sẽ inject Bean vào bởi tham số của hàm
- Nếu có hàm **Setter** thì sẽ inject **Bean** vào bởi tham số của hàm

Nếu không sử dụng **@Autowired** thì phải có 1 hàm **Constructor** hoặc một **Setter** tương ứng:

```sh
@Component
public class Girl {

    // Đánh dấu để Spring inject một đối tượng Outfit vào đây
    @Autowired
    Outfit outfit;

    // Spring sẽ inject outfit thông qua Constructor trước
    public Girl() { }


    // Nếu không tìm thấy Constructor thoả mãn, nó sẽ thông qua setter
    public void setOutfit(Outfit outfit) {
        this.outfit = outfit;
    }

    // GET
    // SET
}
```

Vẫn có thể gắn thêm **@Autowired** vào vì thuộc tính, chức năng vẫn tương tự, nó sẽ tìm Bean phù hợp với method để truyền vào.

```sh
@Component
public class Girl {

    // Đánh dấu để Spring inject một đối tượng Outfit vào đây
    Outfit outfit;

    // Spring sẽ inject outfit thông qua Constructor trước
    public Girl() { }


    @Autowired
    // Nếu không tìm thấy Constructor thoả mãn, nó sẽ thông qua setter
    public void setOutfit(Outfit outfit) {
        this.outfit = outfit;
    }

    // GET
    // SET
}
```

## Nếu có 2 Bean cùng loại trong Context
Lúc này thì Spring sẽ bối rối và không biết sử dụng Bean nào để inject vào đối tượng.

```
import org.springframework.stereotype.Component;

public interface Outfit {
    public void wear();
}

/*
 Đánh dấu class bằng @Component
 Class này sẽ được Spring Boot hiểu là một Bean (hoặc dependency)
 Và sẽ được Spring Boot quản lý
  */
@Component
public class Bikini implements Outfit {
    @Override
    public void wear() {
        System.out.println("Mặc bikini");
    }
}


@Component
public class Naked implements Outfit {
    @Override
    public void wear() {
        System.out.println("Đang không mặc gì");
    }
}
```

```

@Component
public class Girl {

    @Autowired
    Outfit outfit;

    // GET
    // SET
}
```

Output:
```
***************************
APPLICATION FAILED TO START
***************************

Description:

Parameter 0 of constructor in me.loda.spring.helloprimaryqualifier.Girl required a single bean, but 2 were found:
	- bikini: defined in file [/Users/lv00141/Documents/WORKING_SPACE/GITHUB/spring-boot-learning/spring-boot-helloworld-@Primary - @Qualifier/target/classes/me/loda/spring/helloprimaryqualifier/Bikini.class]
	- naked: defined in file [/Users/lv00141/Documents/WORKING_SPACE/GITHUB/spring-boot-learning/spring-boot-helloworld-@Primary - @Qualifier/target/classes/me/loda/spring/helloprimaryqualifier/Naked.class]
```

Trong quá trình cài đặt, nó tìm thấy tới 2 đối tượng thoả mãn Outfit. Giờ nó không biết sử dụng cái nào để inject vào trong Girl

## @Primary
@Primary là annotation đánh dấu trên một Bean, giúp nó luôn được ưu tiên lựa chọn trong trường hợp có nhiều Bean cùng loại trong Context.

```

@Component
@Primary
public class Naked implements Outfit {
    @Override
    public void wear() {
        System.out.println("Đang không mặc gì");
    }
}
```

```
@SpringBootApplication
public class App {
    public static void main(String[] args) {
        // ApplicationContext chính là container, chứa toàn bộ các Bean
        ApplicationContext context = SpringApplication.run(App.class, args);

        // Khi chạy xong, lúc này context sẽ chứa các Bean có đánh
        // dấu @Component.

        Girl girl = context.getBean(Girl.class);

        System.out.println("Girl Instance: " + girl);

        System.out.println("Girl Outfit: " + girl.outfit);

        girl.outfit.wear();
    }
}
```
Output:
```
Girl Instance: me.loda.spring.helloprimaryqualifier.Girl@eb9a089
Girl Outfit: me.loda.spring.helloprimaryqualifier.Naked@1688653c
Đang không mặc gì
```

Spring Boot đã ưu tiên Naked và inject nó vào Girl.

## @Qualifier
@Qualifier xác định tên của một Bean mà bạn muốn chỉ định inject.

```
@Component("bikini")
public class Bikini implements Outfit {
    @Override
    public void wear() {
        System.out.println("Mặc bikini");
    }
}

@Component("naked")
public class Naked implements Outfit {
    @Override
    public void wear() {
        System.out.println("Đang không mặc gì");
    }
}

@Component
public class Girl {

    Outfit outfit;

    // Đánh dấu để Spring inject một đối tượng Outfit vào đây
    public Girl(@Qualifier("naked") Outfit outfit) {
        // Spring sẽ inject outfit thông qua Constructor đầu tiên
        // Ngoài ra, nó sẽ tìm Bean có @Qualifier("naked") trong context để ịnject
        this.outfit = outfit;
    }

    // GET
    // SET
}
```
Kết: 
@Primary và @Qualifier là một trong những tính năng bạn nên biết trong Spring để có thể xử lý vấn đề nhiều Bean cùng loại trong một Project.

Tham khảo:
https://loda.me/articles/sb2-autowired-primary-qualifier
