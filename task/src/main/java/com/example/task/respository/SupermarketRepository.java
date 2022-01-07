package com.example.task.respository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.task.model.SupermarketEntity;

@Repository
public interface SupermarketRepository extends JpaRepository<SupermarketEntity, Long> {

}
