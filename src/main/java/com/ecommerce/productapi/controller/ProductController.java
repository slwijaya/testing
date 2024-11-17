package com.ecommerce.productapi.controller;

import com.ecommerce.productapi.model.Product;
import com.ecommerce.productapi.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@Tag(name = "Product API", description = "API untuk mengelola data produk") // Swagger Tag
public class ProductController {

    @Autowired
    private ProductService productService;

    @Operation(summary = "Get all products", description = "Mengambil daftar semua produk")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Daftar produk berhasil diambil",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "400", description = "Terjadi kesalahan saat mengambil produk",
                    content = @Content)
    })
    @GetMapping
    public ResponseEntity<?> getAllProducts() {
        try {
            List<Product> products = productService.getAllProducts();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error retrieving products");
        }
    }

    @Operation(summary = "Add a new product", description = "Menambahkan produk baru ke dalam database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Produk berhasil dibuat",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "500", description = "Terjadi kesalahan pada server",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<?> addProduct(@RequestBody(description = "Detail produk yang akan ditambahkan", required = true)
                                        Product product) {
        try {
            Product createdProduct = productService.addProduct(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating product");
        }
    }

    @Operation(summary = "Update a product", description = "Memperbarui data produk yang sudah ada berdasarkan ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produk berhasil diperbarui",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "404", description = "Produk tidak ditemukan",
                    content = @Content),
            @ApiResponse(responseCode = "400", description = "Permintaan tidak valid",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduct(
            @Parameter(description = "ID dari produk yang akan diperbarui", required = true) @PathVariable Integer id,
            @RequestBody(description = "Detail produk yang diperbarui", required = true) Product product) {
        try {
            Product updatedProduct = productService.updateProduct(id, product);
            return ResponseEntity.ok(updatedProduct);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
    }

    @Operation(summary = "Delete a product", description = "Menghapus produk dari database berdasarkan ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Produk berhasil dihapus", content = @Content),
            @ApiResponse(responseCode = "404", description = "Produk tidak ditemukan", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(
            @Parameter(description = "ID dari produk yang akan dihapus", required = true) @PathVariable Integer id) {
        try {
            productService.deleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
    }

    @Operation(summary = "Get product by ID", description = "Mengambil data produk berdasarkan ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Produk berhasil ditemukan",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class))),
            @ApiResponse(responseCode = "404", description = "Produk tidak ditemukan",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getProductById(
            @Parameter(description = "ID dari produk yang ingin diambil", required = true) @PathVariable Integer id) {
        try {
            Product product = productService.getProductById(id);
            return ResponseEntity.ok(product);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
    }
}
