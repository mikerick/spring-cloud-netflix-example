package net.devh.controller;

import net.devh.hystrix.HystrixWrappedServiceBClient;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;

import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * User: Michael
 * Email: yidongnan@gmail.com
 * Date: 2016/6/3
 */
@RefreshScope
@RestController
@Api
public class AServiceController {
    private Logger logger = Logger.getLogger(AServiceController.class.getName());

    String guid = null;

    @Value("${name:unknown}")
    private String name;

    @Autowired
    private HystrixWrappedServiceBClient serviceBClient;

    @Autowired
    private Registration registration;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String printServiceA() {
        return registration.getServiceId() + " (" + registration.getHost() + ":" + registration.getPort() + ")" + "===>name:" + name + "<br/>" + serviceBClient.printServiceB();
    }

    @PostMapping("/pong")
    public String pongServiceA(@RequestParam String guid) {
        logger.info("Received pong from B, GUID: " + guid);
        if (this.guid == null)
            this.guid = guid;
        return "ok";
    }

    @GetMapping("/start-ping-pong")
    public String startPingPongA() {
        this.guid = null;
        String receivedGuid = serviceBClient.pingServiceB();

        Callable<String> waitingForGuid = () -> {
            DateTime finishFime = DateTime.now().plusSeconds(10);  // to not work indefinitely
            String returnGuid = null;
            while (finishFime.isAfterNow()) {
                if (this.guid == null) continue;
                returnGuid = guid;
                break;
            }
            return returnGuid;
        };
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<String> future = executorService.submit(waitingForGuid);
        String returnedGuid = null;
        try {
            returnedGuid = future.get(10, TimeUnit.SECONDS);
            this.guid = null;
            if (returnedGuid == null) {
                logger.info("Межсервисное взаимодействие развалилось");
                return "Межсервисное взаимодействие развалилось";
            }
        } catch (InterruptedException | ExecutionException ignored) {
        } catch (TimeoutException e) {
            logger.info("Межсервисное взаимодействие развалилось");
            return "Межсервисное взаимодействие развалилось";
        }

        if (receivedGuid.equals(returnedGuid)) {
            logger.info("Всё работает отлично");
            return "Всё работает отлично";
        }
        else {
            logger.info("Ошибка: Несовпали идентификаторы вызовов");
            return "Ошибка: Несовпали идентификаторы вызовов";
        }
    }
}
