package co.istad.ishop.controller;

import co.istad.ishop.model.request.BrandCreateDTO;
import co.istad.ishop.model.response.BrandRespondDTO;
import co.istad.ishop.service.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/brands")
@RequiredArgsConstructor()
public class BrandController {

    private final BrandService brandService;

    @PostMapping
    public ResponseEntity<String> createBrand(@Valid @RequestBody BrandCreateDTO brandCreateDTO) {
        brandService.addBrand(brandCreateDTO);
        return ResponseEntity.ok("BRAND_CREATED_SUCCESSFULLY");
    }

    @GetMapping
    public ResponseEntity<Map<String, List<BrandRespondDTO>>> getAllBrands() {
        return ResponseEntity.ok(Map.of("brands", brandService.findAllBrands()));
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<BrandRespondDTO> getBrandByUuid(@PathVariable String uuid) {
        return ResponseEntity.ok(brandService.findBrandByUuid(uuid));
    }

    @PutMapping("/{uuid}")
    public ResponseEntity<String> updateBrand (@PathVariable String uuid, @Valid @RequestBody BrandCreateDTO brandCreateDTO) {
        brandService.updateBrandByUuid(uuid, brandCreateDTO);
        return ResponseEntity.ok("BRAND_UPDATED_SUCCESSFULLY");
    }

    @DeleteMapping("/{uuid}")
    public ResponseEntity<String> deleteBrandByUuid(@PathVariable String uuid) {
        brandService.deleteBrandByUuid(uuid);
        return ResponseEntity.ok("BRAND_DELETED_SUCCESSFULLY");
    }
}
