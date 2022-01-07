package com.example.task.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.task.exception.BusinessException;
import com.example.task.exception.ResourceNotFoundException;
import com.example.task.model.SupermarketEntity;
import com.example.task.service.SupermarketService;

@RestController
@RequestMapping("/api/supermarkets")
public class SupermarketController {
    @Autowired
    SupermarketService service;

    @PostMapping("/addSupermarket")
    public SupermarketEntity addSupermarket(@RequestParam(name = "arabicName") String arabicName, @RequestParam(name = "englishName",required = false) String englishName, @RequestParam(name = "address",required = false) String address, @RequestParam(name = "image",required = false) MultipartFile image) throws BusinessException {
	// String fileName = StringUtils.cleanPath(image.getOriginalFilename());
	// String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
	// .path("/downloadFile/")
	// .path(fileName)
	// .toUriString();
	// System.out.println(fileDownloadUri);
	return service.createSupermarket(arabicName, englishName, address, image);
    }

    @PutMapping("/updateSupermarket")
    public SupermarketEntity updateSupermarket(@RequestParam(name = "supermarketId") Long id,@RequestParam(name = "arabicName",required = false) String arabicName, @RequestParam(name = "englishName",required = false) String englishName, @RequestParam(name = "address",required = false) String address, @RequestParam(name = "image",required = false) MultipartFile image) throws BusinessException {
	return service.editSupermarket(id,arabicName, englishName, address, image);
    }

    @DeleteMapping("/deleteSupermarket")
    public void deleteAssessment(@RequestParam(name = "supermarketId") Long id) throws ResourceNotFoundException {
	service.deleteSupermarket(id);
    }

    @GetMapping("/getAllSupermarkets")
    public List<SupermarketEntity> getAllSupermarkets() throws BusinessException {
	return service.getAllSupermarkets();
    }

    @PutMapping("/activateSupermarket")
    public SupermarketEntity activateSupermarket(@RequestParam(name = "supermarketId") Long id, Integer type) throws BusinessException {
	return service.activateOrDeactivateSupermarket(id, 1);
    }

    @PutMapping("/dectivateSupermarket")
    public SupermarketEntity dectivateSupermarket(@RequestParam(name = "supermarketId") Long id, Integer type) throws BusinessException {
	return service.activateOrDeactivateSupermarket(id, 0);
    }

    @GetMapping("/getSupermarketById")
    public SupermarketEntity getSupermarketByid(@RequestParam(name = "supermarketId") Long id) throws ResourceNotFoundException {
	return service.getSuperMarketById(id);

    }

}
