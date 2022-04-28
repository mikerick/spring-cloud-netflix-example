package net.devh.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * User: Michael
 * Email: yidongnan@gmail.com
 * Date: 2016/6/3
 */
@FeignClient(name = "service-a")
public interface ServiceAClient {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    String printServiceA();

    @PostMapping("/pong")
    public String pongServiceA(@RequestParam String guid);

}
