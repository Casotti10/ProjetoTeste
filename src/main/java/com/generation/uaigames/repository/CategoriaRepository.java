package com.generation.uaigames.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.generation.uaigames.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
	
	List<Categoria> findAllByCategoriaContainingIgnoreCase(@Param("categoria")String categoria);

	
}