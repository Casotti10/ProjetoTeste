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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.generation.uaigames.model.UsuarioLogin;
import com.generation.uaigames.model.Categoria;
import com.generation.uaigames.model.Usuario;
import com.generation.uaigames.repository.UsuarioRepository;
import com.generation.uaigames.service.UsuarioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios") // Define o endpoint base para "/usuarios"
@CrossOrigin(origins = "*", allowedHeaders = "*") // Permite requisições de qualquer origem
public class UsuarioController {

	@Autowired // Injeção de dependência para o serviço de usuários
	private UsuarioService usuarioService;

	@Autowired
	private UsuarioRepository usuarioRepository; // Injeção de dependência para o repositório de usuários

	@GetMapping("/all") // Método para buscar todos os usuários cadastrados
	public ResponseEntity<List<Usuario>> getAll() {

		return ResponseEntity.ok(usuarioRepository.findAll());
	}

	@GetMapping("/{id}") // Método para buscar um usuário pelo ID
	public ResponseEntity<Usuario> getById(@PathVariable Long id) { // Autentica o usuário e retorna o status 200 (OK)
																	// se válido
		return usuarioRepository.findById(id).map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.notFound().build()); // Retorna 404 (Not Found) se não encontrado

	}

	@PostMapping("/logar") // Método para autenticar um usuário
	public ResponseEntity<UsuarioLogin> autenticarUsuario(@RequestBody Optional<UsuarioLogin> usuarioLogin) {

		return usuarioService.autenticarUsuario(usuarioLogin)
				.map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
				.orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
	}

	@PostMapping("/cadastrar") // Método para cadastrar um novo usuário
	public ResponseEntity<Usuario> postUsuario(@RequestBody @Valid Usuario usuario) {

		return usuarioService.cadastrarUsuario(usuario)
				.map(resposta -> ResponseEntity.status(HttpStatus.CREATED).body(resposta))
				.orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build()); // Retorna 404 (Not Found) se o usuário
																				// não existir

	}

	@PutMapping("/atualizar") // Método para atualizar os dados de um usuário existente
	public ResponseEntity<Usuario> putUsuario(@Valid @RequestBody Usuario usuario) {

		return usuarioService.atualizarUsuario(usuario) // Retorna o status 201 (Created) se bem-sucedido
				.map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build()); // Retorna 404 (Not Found) se o usuário
																				// não existir
	}

	
	
}