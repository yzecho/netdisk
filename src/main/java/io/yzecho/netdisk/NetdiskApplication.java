package io.yzecho.netdisk;

import com.github.pagehelper.autoconfigure.PageHelperAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author yecho
 */
@EnableAsync
@SpringBootApplication(exclude = PageHelperAutoConfiguration.class)
@MapperScan(basePackages = {"io.yzecho.netdisk.mapper"})
public class NetdiskApplication {

    public static void main(String[] args) {
        SpringApplication.run(NetdiskApplication.class, args);
    }

}
