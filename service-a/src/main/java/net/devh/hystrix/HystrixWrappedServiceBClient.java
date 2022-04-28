package net.devh.hystrix;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import net.devh.feign.ServiceBClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

/**
 * User: Michael
 * Email: yidongnan@gmail.com
 * Date: 2016/6/3
 */
@Service
public class HystrixWrappedServiceBClient implements ServiceBClient {

    @Autowired
    private ServiceBClient serviceBClient;

    @Override
    @HystrixCommand(groupKey = "helloGroup", fallbackMethod = "fallBackCall")
    public String printServiceB() {
        return serviceBClient.printServiceB();
    }

    @Override
    @HystrixCommand(fallbackMethod = "bServiceIsNotAccessible")
    public String pingServiceB() {
        return serviceBClient.pingServiceB();
    }

    public String fallBackCall() {
        return "FAILED SERVICE B CALL! - FALLING BACK";
    }
    public String bServiceIsNotAccessible() {return "Межсервисное взаимодействие развалилось";}
}
