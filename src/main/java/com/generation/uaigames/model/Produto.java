package com.generation.uaigames.model;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "tb_produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto Incremento
    private Long id;

    @NotBlank(message = "O atributo Nome é obrigatório.")
    @Size(min = 4, max = 50, message = "O Nome deve ter entre 4 e 50 caracteres.")
    private String nome;

    @NotNull(message = "O atributo Preço é obrigatório.")
    @Column(precision = 10, scale = 2) // Configuração de casas decimais
    private BigDecimal preco;

    @NotNull(message = "O atributo Estoque é obrigatório.")
    private Integer estoque;

    @NotBlank(message = "O atributo Descrição é obrigatório.")
    @Size(min = 5, max = 255, message = "A Descrição deve ter entre 5 e 255 caracteres.")
    private String descricao;

    @ManyToOne // Relacionamento de muitos para um
    @JsonIgnoreProperties("produto") // Ajuste para refletir o relacionamento correto
    private Categoria categoria;
    
    public Categoria getCategoria() {
		return categoria;
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public BigDecimal getPreco() {
		return preco;
	}

	public void setPreco(BigDecimal preco) {
		this.preco = preco;
	}

	public Integer getEstoque() {
		return estoque;
	}

	public void setEstoque(Integer estoque) {
		this.estoque = estoque;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

}
	
   