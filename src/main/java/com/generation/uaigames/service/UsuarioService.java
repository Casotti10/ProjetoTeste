package com.generation.uaigames.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.generation.uaigames.model.UsuarioLogin;
import com.generation.uaigames.model.Usuario;
import com.generation.uaigames.repository.UsuarioRepository;
import com.generation.uaigames.security.JwtService;

@Service 
public class UsuarioService {

	@Autowired 
	private UsuarioRepository usuarioRepository;

	@Autowired 
	private JwtService jwtService;

	@Autowired 
	private AuthenticationManager authenticationManager;


	public Optional<Usuario> cadastrarUsuario(Usuario usuario) {


		if (usuarioRepository.findByUsuario(usuario.getUsuario()).isPresent())
			return Optional.empty(); 

		usuario.setSenha(criptografarSenha(usuario.getSenha()));

		return Optional.of(usuarioRepository.save(usuario));
	}

	public Optional<Usuario> atualizarUsuario(Usuario usuario) {

		if (usuarioRepository.findById(usuario.getId()).isPresent()) {

			Optional<Usuario> buscaUsuario = usuarioRepository.findByUsuario(usuario.getUsuario());
			if ((buscaUsuario.isPresent()) && (buscaUsuario.get().getId() != usuario.getId()))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já existe!", null); // us


// Criptografa a nova senha antes de atualizar o usuário
			usuario.setSenha(criptografarSenha(usuario.getSenha()));

// Atualiza o usuário no banco de dados e retorna o usuário atualizado
			return Optional.ofNullable(usuarioRepository.save(usuario));
		}

		return Optional.empty(); // Retorna vazio se o usuário não for encontrado
	}

// Método para autenticar um usuário
	public Optional<UsuarioLogin> autenticarUsuario(Optional<UsuarioLogin> usuarioLogin) {

// Cria um objeto de autenticação com as credenciais do usuário (nome de usuário
// e senha)
		var credenciais = new UsernamePasswordAuthenticationToken(usuarioLogin.get().getUsuario(),
				usuarioLogin.get().getSenha());

// Tenta autenticar o usuário
		Authentication authentication = authenticationManager.authenticate(credenciais);

// Se a autenticação foi bem-sucedida
		if (authentication.isAuthenticated()) {

// Busca o usuário no banco de dados com o nome de usuário informado
			Optional<Usuario> usuario = usuarioRepository.findByUsuario(usuarioLogin.get().getUsuario());

// Se o usuário for encontrado
			if (usuario.isPresent()) {

				// Preenche o objeto UsuarioLogin com os dados do usuário (nome, foto, token,
				// etc.)
				usuarioLogin.get().setId(usuario.get().getId());
				usuarioLogin.get().setNome(usuario.get().getNome());
				usuarioLogin.get().setFoto(usuario.get().getFoto());
				usuarioLogin.get().setToken(gerarToken(usuarioLogin.get().getUsuario())); // Gera o token JWT
				usuarioLogin.get().setSenha(""); // Limpa a senha antes de retornar (não deve ser enviada)

				// Retorna o objeto usuarioLogin preenchido
				return usuarioLogin;
			}
		}

		return Optional.empty(); // Retorna vazio se a autenticação falhar
	}

// Método para criptografar a senha com o algoritmo BCrypt
	private String criptografarSenha(String senha) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.encode(senha); // Retorna a senha criptografada
	}

// Método para gerar o token JWT para o usuário
	private String gerarToken(String usuario) {
		return "Bearer " + jwtService.generateToken(usuario); // Adiciona "Bearer" ao token gerado
	}

}
