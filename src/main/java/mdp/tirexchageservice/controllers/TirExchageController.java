package mdp.tirexchageservice.controllers;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import mdp.tirexchageservice.models.TirMessage;
import mdp.tirexchageservice.processor.Epd015Processor;
import mdp.tirexchageservice.respositories.TirMessageRepository;
import mdp.tirexchageservice.services.TirExchangeService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tir")
@RequiredArgsConstructor
public class TirExchageController {

    private final TirMessageRepository repository;
    private final TirExchangeService service;

    @PostMapping(value = "/exchange", consumes = MediaType.APPLICATION_XML_VALUE, produces = MediaType.APPLICATION_XML_VALUE)
    public String receiveExchange(@RequestBody String xml) {
        return service.handleMessage(xml);
    }

    @GetMapping("/messages")
    public List<TirMessage> getMessages() {
        return repository.findAll();
    }

}
