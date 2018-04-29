package com.example.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/map")
public class MapApiController {
    private static Logger LOGGER = LoggerFactory.getLogger(MapApiController.class);

    @Autowired
    private Ss7Stack ss7Stack;

    @RequestMapping(value = "/sendSri/{msisdn}", method = RequestMethod.GET)
    String sendSri(@PathVariable String msisdn) {
        LOGGER.info("Sending sri...");
        return ss7Stack.sendSri(msisdn, "93791010507");
    }

    @RequestMapping(value = "/stop", method = RequestMethod.GET)
    String stop() {
        LOGGER.info("Stopping ss7 stack...");
        ss7Stack.stop();
        return ss7Stack.getState();
    }

    @RequestMapping(value = "/start", method = RequestMethod.GET)
    String start() {
        LOGGER.info("Starting ss7 stack...");
        ss7Stack.start();
        return ss7Stack.getState();
    }

    @RequestMapping(value = "/aspStart", method = RequestMethod.GET)
    String aspStart() {
        LOGGER.info("Starting asps...");
        ss7Stack.aspStart();
        return ss7Stack.getState();
    }

    @RequestMapping(value = "/state", method = RequestMethod.GET)
    String state() {
        LOGGER.info("Getting state of ss7 stack...");
        return ss7Stack.getState();
    }

}