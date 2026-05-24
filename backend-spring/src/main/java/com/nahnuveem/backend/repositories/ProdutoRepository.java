package com.nahnuveem.backend.repositories;

import com.nahnuveem.backend.models.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, UUID> {

    // Método para verificar se já existe um produto com o mesmo código de barras
    Optional<Produto> findByCodigoBarras(String codigoBarras);
}