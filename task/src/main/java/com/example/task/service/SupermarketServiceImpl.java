package com.example.task.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.task.exception.BusinessException;
import com.example.task.exception.ExceptionsHandlerService;
import com.example.task.exception.ResourceNotFoundException;
import com.example.task.model.SupermarketEntity;
import com.example.task.respository.SupermarketRepository;

@Service
public class SupermarketServiceImpl implements SupermarketService {
    @Autowired
    SupermarketRepository repository;

    @Override
    public SupermarketEntity createSupermarket(String arabicName, String englishName, String address, MultipartFile image) throws BusinessException {
	String fileName = StringUtils.cleanPath(image.getOriginalFilename());

	try {
	    // Check if the file's name contains invalid characters
	    if (fileName.contains("..")) {
		throw new BusinessException("Sorry! Filename contains invalid path sequence " + fileName);
	    }

	    SupermarketEntity createdSupermarket = new SupermarketEntity(englishName, arabicName, address, image.getBytes());
	    validateSupermarket(createdSupermarket);
	    return repository.save(createdSupermarket);
	} catch (Exception e) {
	    throw ExceptionsHandlerService.handleException(e, "error creating supermarket");
	}
    }

    @Override
    public SupermarketEntity editSupermarket(Long id, String arabicName, String englishName, String address, MultipartFile image) throws BusinessException {
	SupermarketEntity entity = getSuperMarketById(id);
	if (arabicName != null) {
	    entity.setArabicName(arabicName);
	}
	if (englishName != null) {
	    entity.setEnglishName(englishName);
	}
	if (address != null) {
	    entity.setAddress(address);
	}
	if (image != null) {
	    String fileName = StringUtils.cleanPath(image.getOriginalFilename());

	    try {
		// Check if the file's name contains invalid characters
		if (fileName.contains(".."))
		    throw new BusinessException("Sorry! Filename contains invalid path sequence " + fileName);
		entity.setImage(image.getBytes());
	    } catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
	}
	validateSupermarket(entity);
	return repository.save(entity);
    }

    @Override
    public List<SupermarketEntity> getAllSupermarkets() throws BusinessException {
	try {
	    List<SupermarketEntity> list = repository.findAll();
	    if (list.size() == 0 || list.isEmpty())
		list = new ArrayList<SupermarketEntity>();
	    return list;
	} catch (Exception e) {
	    throw ExceptionsHandlerService.handleException(e, "error_getting supermarkets");
	}
    }

    @Override
    public ResponseEntity<Object> deleteSupermarket(Long id) throws ResourceNotFoundException {
	SupermarketEntity entity = getSuperMarketById(id);
	repository.delete(entity);

	return ResponseEntity.ok().build();
    }

    @Override
    public SupermarketEntity getSuperMarketById(Long id) {
	return this.repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("supermarket can't be found by the given id"));
    }

    @Override
    //// if type =0 deactivte, if =1 activate
    public SupermarketEntity activateOrDeactivateSupermarket(Long id, Integer type) throws ResourceNotFoundException, BusinessException {
	SupermarketEntity createdSupermarket = getSuperMarketById(id);
	if (type == 1) {
	    if (!createdSupermarket.getActivated())
		createdSupermarket.setActivated(true);
	} else if (type == 0) {
	    if (createdSupermarket.getActivated())
		createdSupermarket.setActivated(false);
	}
	return this.repository.save(createdSupermarket);
    }
///////validation for mandatory fields when adding a supermarket arabicName and address 
    private void validateSupermarket(SupermarketEntity supermarket) throws BusinessException {
	if (supermarket.getArabicName() == null || supermarket.getArabicName().trim().isEmpty())
	    throw new BusinessException("Supermarket Arabic name doesn't exist or is empty");
	if (supermarket.getAddress() == null || supermarket.getAddress().trim().isEmpty())
	    throw new BusinessException("Supermarket Address doesn't exist or is empty");

    }
}
