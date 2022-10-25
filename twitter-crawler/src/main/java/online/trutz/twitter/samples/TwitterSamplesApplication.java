package online.trutz.twitter.samples;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
class TwitterSamplesApplication {

    public static void main(String[] args) {
        SpringApplication.run(TwitterSamplesApplication.class, args);
    }

}
