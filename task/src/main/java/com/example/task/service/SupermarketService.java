package com.example.task.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.example.task.exception.BusinessException;
import com.example.task.exception.ResourceNotFoundException;
import com.example.task.model.SupermarketEntity;

public interface SupermarketService {
    SupermarketEntity createSupermarket(String arabicName,String englishName, String address,MultipartFile image ) throws BusinessException;
    List<SupermarketEntity> getAllSupermarkets() throws BusinessException;
    ResponseEntity<Object> deleteSupermarket(Long id)  throws ResourceNotFoundException;
    SupermarketEntity getSuperMarketById(Long id) throws ResourceNotFoundException;
    SupermarketEntity activateOrDeactivateSupermarket (Long id,Integer type) throws BusinessException;
    SupermarketEntity editSupermarket(Long id, String arabicName, String englishName, String address, MultipartFile image) throws BusinessException;
}

