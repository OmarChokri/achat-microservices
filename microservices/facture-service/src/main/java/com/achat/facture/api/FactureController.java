package com.achat.facture.api;

import com.achat.facture.service.FactureService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/factures")
public class FactureController {
    private final FactureService factureService;

    public FactureController(FactureService factureService) {
        this.factureService = factureService;
    }

    @GetMapping
    public List<FactureResponse> findAll() {
        return factureService.findAll();
    }

    @GetMapping("/{idFacture}")
    public FactureResponse findById(@PathVariable Long idFacture) {
        return factureService.findById(idFacture);
    }

    @GetMapping("/fournisseur/{fournisseurId}")
    public List<FactureResponse> findByFournisseurId(@PathVariable Long fournisseurId) {
        return factureService.findByFournisseurId(fournisseurId);
    }

    @PostMapping
    public ResponseEntity<FactureResponse> create(@Valid @RequestBody FactureRequest request) {
        FactureResponse response = factureService.create(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{idFacture}")
                .buildAndExpand(response.idFacture())
                .toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PutMapping("/{idFacture}/archive")
    public ResponseEntity<Void> archive(@PathVariable Long idFacture) {
        factureService.archive(idFacture);
        return ResponseEntity.noContent().build();
    }
}
