package mdp.tirexchageservice.controllers;

import jakarta.xml.bind.JAXBException;
import lombok.RequiredArgsConstructor;
import mdp.tirexchageservice.exceptions.SoapFaultException;
import mdp.tirexchageservice.entities.TirMessage;
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
    public String receiveExchange(@RequestBody String xml) throws SoapFaultException, JAXBException {
        return service.handleMessage(xml);
    }

    @GetMapping("/messages")
    public List<TirMessage> getMessages(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size) {
        return service.getMessages(page, size);
    }

}
