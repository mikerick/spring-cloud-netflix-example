package net.devh;

import io.swagger.annotations.Api;
import net.devh.hystrix.HystrixWrappedServiceAClient;
import net.devh.services.GuidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.logging.Logger;

/**
 * User: Michael
 * Email: yidongnan@gmail.com
 * Date: 2016/6/3
 */
@RefreshScope
@RestController
@Api
public class ServiceB1Controller {

    @Autowired
    private GuidService guidService;

    @Autowired
    private Registration registration;

    @Autowired
    private HystrixWrappedServiceAClient serviceAClient;

    @Value("${msg:unknown}")
    private String msg;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String printServiceB() {
        return registration.getServiceId() + " (" + registration.getHost() + ":" + registration.getPort() + ")" + "===>Say " + msg;
    }

    @GetMapping("/ping")
    public String pingServiceB() {
        Logger logger = Logger.getLogger(this.getClass().getName());
        logger.info("Request for guid received. GUID = " + guidService.getGuid());
        serviceAClient.pongServiceA(guidService.getGuid());
        return guidService.getGuid();
    }
}
