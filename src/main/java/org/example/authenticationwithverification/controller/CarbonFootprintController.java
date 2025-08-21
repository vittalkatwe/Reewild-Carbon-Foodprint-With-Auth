package org.example.authenticationwithverification.controller;

import org.example.authenticationwithverification.dto.CarbonFootprintRequest;
import org.example.authenticationwithverification.dto.CarbonFootprintResponse;
import org.example.authenticationwithverification.service.CarbonFootprintService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;

@RestController
@RequestMapping("/estimate")
public class CarbonFootprintController {

    @Autowired
    private CarbonFootprintService carbonFootprintService;

    @PostMapping
    public ResponseEntity<CarbonFootprintResponse> getCarbonFootprint(@RequestBody CarbonFootprintRequest request) {
        CarbonFootprintResponse response = carbonFootprintService.calculateCarbonFootprint(request.getDish(), request.getServings());

        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/image")
    @PreAuthorize("hasAnyRole()")
    public ResponseEntity<CarbonFootprintResponse> getCarbonFootprintFromImage(
            @RequestParam("image") MultipartFile image,
            @RequestParam("servings") long servings) {

        if (image.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Image file must be provided.");
        }

        try {
            CarbonFootprintResponse response = carbonFootprintService.calculateCarbonFootprintFromImage(image, servings);
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            System.err.println("Error processing uploaded file: " + e.getMessage());
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error processing image file.", e);

        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }


}

