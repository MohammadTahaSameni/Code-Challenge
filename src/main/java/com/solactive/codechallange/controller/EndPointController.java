package com.solactive.codechallange.controller;

import com.solactive.codechallange.model.Statistic;
import com.solactive.codechallange.model.Ticks;
import com.solactive.codechallange.service.ProviderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.solactive.codechallange.constant.Cons.SLIDE_TIME_INTERVAL;

@RestController
public class EndPointController {

    final ProviderService service;
    public EndPointController(ProviderService service) {
        this.service = service;
    }

    @PostMapping(value = "/ticks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity receiveTick(@RequestBody Ticks ticks) {
        final long currentTime = System.currentTimeMillis();
        if (ticks.getTimestamp() < currentTime - SLIDE_TIME_INTERVAL
                || ticks.getInstrument() == null
                || ticks.getTimestamp() >= currentTime + 10
                || ticks.getPrice() <= 0)
            return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);

        service.showTicksMsg(ticks);
        return new ResponseEntity<Void>(HttpStatus.CREATED);

    }
    @GetMapping("/statistics/{instrumentId}")
    public Statistic getStatistics(@PathVariable String instrumentId) {
        return service.currentAssetState(instrumentId);

    }
    @GetMapping(value = "/statistics")
    public Statistic getAggregatedStatistics() {
        return service.currAggregateStats();
    }
}
