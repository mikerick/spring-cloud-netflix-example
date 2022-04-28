package net.devh.hystrix;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import net.devh.feign.ServiceAClient;

import net.devh.services.GuidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * User: Michael
 * Email: yidongnan@gmail.com
 * Date: 2016/6/3
 */
@Service
public class HystrixWrappedServiceAClient implements ServiceAClient {

    @Autowired
    private ServiceAClient serviceAClient;

    @Override
    @HystrixCommand(groupKey = "helloGroup", fallbackMethod = "fallBackCall")
    public String printServiceA() {
        return serviceAClient.printServiceA();
    }

    @Override
    @HystrixCommand(fallbackMethod = "logMissingServiceA")
    public String pongServiceA(String guid) {
        serviceAClient.pongServiceA(guid);
        return "ok";
    }

    public String fallBackCall() {
        return "FAILED SERVICE A CALL! - FALLING BACK";
    }
    public String logMissingServiceA(String guid) {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.info("Межсерверное взаимодействие развалилось");
        return "Межсерверное взаимодействие развалилось";
    }
}
