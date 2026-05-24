package com.nahnuveem.backend.services;

import com.nahnuveem.backend.dtos.ProdutoRequestDTO;
import com.nahnuveem.backend.dtos.ProdutoResponseDTO;
import com.nahnuveem.backend.models.Produto;
import com.nahnuveem.backend.models.Usuario;
import com.nahnuveem.backend.repositories.ProdutoRepository;
import com.nahnuveem.backend.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    // 1. CADASTRAR PRODUTO
    public ProdutoResponseDTO salvarProduto(ProdutoRequestDTO request) {
        // Validação de negócio: código de barras duplicado
        if (produtoRepository.findByCodigoBarras(request.getCodigoBarras()).isPresent()) {
            throw new RuntimeException("Já existe um produto cadastrado com este código de barras.");
        }

        // Busca o usuário que será o dono do produto
        Usuario usuario = usuarioRepository.findById(request.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID fornecido."));

        // Cria a entidade Produto e mapeia os dados do DTO
        Produto produto = new Produto();
        produto.setNome(request.getNome());
        produto.setDescricao(request.getDescricao());
        produto.setPreco(request.getPreco());
        produto.setQuantidadeEstoque(request.getQuantidadeEstoque());
        produto.setCodigoBarras(request.getCodigoBarras());
        produto.setUsuario(usuario);

        Produto produtoSalvo = produtoRepository.save(produto);
        return converterParaResponseDTO(produtoSalvo);
    }

    // Modifique o método de listagem dentro do seu ProdutoService:
    public Page<ProdutoResponseDTO> listarTodos(Pageable paginacao) {
        return produtoRepository.findAll(paginacao)
                .map(this::converterParaResponseDTO);
    }

    // 3. BUSCAR PRODUTO POR ID
    public ProdutoResponseDTO buscarPorId(UUID id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com o ID fornecido."));
        return converterParaResponseDTO(produto);
    }

    // 4. ATUALIZAR PRODUTO
    public ProdutoResponseDTO atualizarProduto(UUID id, ProdutoRequestDTO request) {
        Produto produtoExistente = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado para atualização."));

        // Validação de código de barras duplicado (apenas se o código enviado for diferente do atual)
        if (request.getCodigoBarras() != null && !request.getCodigoBarras().equalsIgnoreCase(produtoExistente.getCodigoBarras())) {
            if (produtoRepository.findByCodigoBarras(request.getCodigoBarras()).isPresent()) {
                throw new RuntimeException("Já existe um produto cadastrado com este código de barras.");
            }
        }

        // Atualiza os dados do produto existente
        produtoExistente.setNome(request.getNome());
        produtoExistente.setDescricao(request.getDescricao());
        produtoExistente.setPreco(request.getPreco());
        produtoExistente.setQuantidadeEstoque(request.getQuantidadeEstoque());
        produtoExistente.setCodigoBarras(request.getCodigoBarras());

        // Se o dono do produto mudou, busca e atualiza o relacionamento
        if (!produtoExistente.getUsuario().getId().equals(request.getUsuarioId())) {
            Usuario novoDono = usuarioRepository.findById(request.getUsuarioId())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado com o ID fornecido."));
            produtoExistente.setUsuario(novoDono);
        }

        Produto produtoAtualizado = produtoRepository.save(produtoExistente);
        return converterParaResponseDTO(produtoAtualizado);
    }

    // 5. DELETAR PRODUTO
    public void deletarProduto(UUID id) {
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado para exclusão."));
        produtoRepository.delete(produto);
    }

    // MÉTODO AUXILIAR: Converte a Entidade original para o DTO de Resposta limpo
    private ProdutoResponseDTO converterParaResponseDTO(Produto produto) {
        ProdutoResponseDTO response = new ProdutoResponseDTO();
        response.setId(produto.getId());
        response.setNome(produto.getNome());
        response.setDescricao(produto.getDescricao());
        response.setPreco(produto.getPreco());
        response.setQuantidadeEstoque(produto.getQuantidadeEstoque());
        response.setCodigoBarras(produto.getCodigoBarras());
        response.setUsuarioId(produto.getUsuario().getId());
        return response;
    }
}