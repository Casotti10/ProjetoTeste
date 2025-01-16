package com.generation.uaigames.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.uaigames.model.Produto;
import com.generation.uaigames.repository.ProdutoRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/produto") // Define qual endpoint vai ser tratado por essa classe
@CrossOrigin(origins = "*", allowedHeaders = "*") // Libera o acesso a qualquer front
public class ProdutoController {

	@Autowired
	private ProdutoRepository produtoRepository;

	@GetMapping
	public ResponseEntity<List<Produto>> getAll() {
		return ResponseEntity.ok(produtoRepository.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Produto> getById(@PathVariable Long id) { // Faz o Spring pegar o id da URL e colocá-lo no //
																	// parâmetro id do método
		return produtoRepository.findById(id).map(reposta -> ResponseEntity.ok(reposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}

	@GetMapping("/nome/{nome}") // Buscar produto por nome
	public ResponseEntity<List<Produto>> getBynome(@PathVariable String nome) {
		return ResponseEntity.ok(produtoRepository.findAllByNomeContainingIgnoreCase(nome));
	}

	@PostMapping // cadastrar(cadastra algo no bd)
	public ResponseEntity<Produto> post(@Valid @RequestBody Produto produto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(produtoRepository.save(produto));
	}

	@PutMapping
	public ResponseEntity<Produto> put(@Valid @RequestBody Produto produto) {
	    return produtoRepository.findById(produto.getId())
	            .map(resposta -> ResponseEntity.status(HttpStatus.OK)
	                    .body(produtoRepository.save(produto)))
	            .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        Optional<Produto> produto = produtoRepository.findById(id); // Busca a postagem no repositório pelo ID recebido
    
    if(produto.isEmpty()) //Verifica se a categoria existe
    	throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    
    produtoRepository.deleteById(id);
	}

}
