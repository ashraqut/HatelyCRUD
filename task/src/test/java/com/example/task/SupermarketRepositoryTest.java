package com.example.task;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.example.task.model.SupermarketEntity;
import com.example.task.respository.SupermarketRepository;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SupermarketRepositoryTest {
    @Autowired
    private SupermarketRepository supermarketRepository;

    @Test
    @Order(1)
    @Rollback(value=false)
    public void createSupermarketTest() {
	SupermarketEntity entity = new SupermarketEntity("super market", "سوبر ماركت", "block 23 street 2 maadi cairo", null);
	SupermarketEntity saveEntity = supermarketRepository.save(entity);
	Assertions.assertThat(saveEntity.getId()).isGreaterThan(0);
    }

    @Test
    @Order(2)
    public void getSupermarketTest() {
	SupermarketEntity entity = supermarketRepository.getById(1L);
	Assertions.assertThat(entity.getId()).isEqualTo(1L);
    }

    @Test
    @Order(3)
    public void updateSupermarketTest() {
	SupermarketEntity entity = supermarketRepository.getById(1L);
	entity.setEnglishName("super marche");
	SupermarketEntity savedEntity = supermarketRepository.save(entity);
	Assertions.assertThat(savedEntity.getEnglishName()).isEqualTo("super marche");

    }

    @Test
    @Order(4)
    public void getAllSupermarketsTest() {
	List<SupermarketEntity> supermarkets = supermarketRepository.findAll();
	Assertions.assertThat(supermarkets.size()).isGreaterThan(0);
    }

    @Test
    @Order(5)
    public void deleteSuperMarketTest() {
	SupermarketEntity entity = supermarketRepository.getById(1L);
	SupermarketEntity supermarketEntity = null;
	supermarketRepository.delete(entity);
	Optional<SupermarketEntity> optionalSupermarket = supermarketRepository.findById(1L);
	if (optionalSupermarket.isPresent())
	    supermarketEntity = optionalSupermarket.get();
	Assertions.assertThat(supermarketEntity).isNull();
    }

}
