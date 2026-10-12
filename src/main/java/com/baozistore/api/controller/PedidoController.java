
package com.baozistore.api.controller;

import com.baozistore.api.model.Pedido;
import com.baozistore.api.repository.PedidoRepository;
import com.baozistore.api.repository.ClienteRepository;
import com.baozistore.api.repository.ProdutoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoRepository pedidos;
    private final ClienteRepository clientes;
    private final ProdutoRepository produtos;

    public PedidoController(PedidoRepository pedidos,
                            ClienteRepository clientes,
                            ProdutoRepository produtos) {
        this.pedidos = pedidos;
        this.clientes = clientes;
        this.produtos = produtos;
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody Pedido pedido) {
        if (pedido.getClienteId() == null ||
                !clientes.existsById(pedido.getClienteId())) {
            return ResponseEntity.badRequest()
                    .body("Cliente não encontrado");
        }

        if (pedido.getProdutoId() == null ||
                !produtos.existsById(pedido.getProdutoId())) {
            return ResponseEntity.badRequest()
                    .body("Produto não encontrado");
        }

        if (pedido.getQuantidade() == null ||
                pedido.getQuantidade() <= 0) {
            return ResponseEntity.badRequest()
                    .body("Quantidade inválida");
        }

        Pedido salvo = pedidos.save(pedido);
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @GetMapping
    public List<Pedido> listar() {
        return pedidos.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pedido> buscar(@PathVariable Long id) {
        return pedidos.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        if (!pedidos.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        pedidos.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}

