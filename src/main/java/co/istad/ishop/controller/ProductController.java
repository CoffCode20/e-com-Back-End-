package co.istad.ishop.controller;

import co.istad.ishop.model.request.ProductCreateDTO;
import co.istad.ishop.model.request.ProductUpdateDTO;
import co.istad.ishop.model.response.ProductRespondDTO;
import co.istad.ishop.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Create a new product", description = "Adds a new product to the inventory")
//    @ApiResponses({
//            @ApiResponse(responseCode = "201", description = "Product created successfully"),
//            @ApiResponse(responseCode = "400", description = "Invalid input data")
//    })
    ResponseEntity<String> addProduct(@Valid @RequestBody ProductCreateDTO productCreateDTO) {
        productService.addProduct(productCreateDTO);
        return ResponseEntity.ok("Product created successfully");
    }

    @GetMapping
    @Operation(summary = "Get all product")
    ResponseEntity<Map<String, List<ProductRespondDTO>>> allProducts() {
        return ResponseEntity.ok(Map.of("products", productService.findAllProducts()));
    }

    @GetMapping("/{uuid}")
    @Operation(summary = "Get product by id")
    ResponseEntity<Map<String, ProductRespondDTO>> findProductByUuid(@PathVariable String uuid) {
        return ResponseEntity.ok(Map.of("product", productService.findProductByUuid(uuid)));
    }

    @PutMapping("/uuid/{uuid}")
    @Operation(summary = "Update product product by uuid")
    ResponseEntity<String> updateProductByUuid(@PathVariable String uuid, @RequestBody ProductUpdateDTO productEditRequest) {
        productService.updateProductByUuid(uuid, productEditRequest);
        return ResponseEntity.ok("Product has been updated successfully");
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/uuid/{uuid}")
    @Operation(summary = "Delete product by uuid")
    public String deleteProductByUuid(@PathVariable String uuid) {
        productService.deleteProduct(uuid);
        return "Product has been deleted";
    }

    @GetMapping("/filter")
    ResponseEntity<Map<String, List<ProductRespondDTO>>> findAllProducts(@RequestParam("name") String name) {
        return ResponseEntity.ok(Map.of("product",  productService.findByName(name)));
    }
}
