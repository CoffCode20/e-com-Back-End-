package co.istad.ishop.service.impl;

import co.istad.ishop.entities.Product;
import co.istad.ishop.exception.ApiException;
import co.istad.ishop.exception.ResourceNotFoundUuid;
import co.istad.ishop.mapper.ProductMapper;
import co.istad.ishop.model.request.ProductCreateDTO;
import co.istad.ishop.model.request.ProductUpdateDTO;
import co.istad.ishop.model.response.ProductRespondDTO;
import co.istad.ishop.repository.ProductRepository;
import co.istad.ishop.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @Override
    public void addProduct(ProductCreateDTO productCreateDTO) {
        Product product = productMapper.toProduct(productCreateDTO);
        product.setUuid(UUID.randomUUID().toString());
        product.setImageUrls(productCreateDTO.imageUrls());

        productRepository.save(product);
    }

    @Override
    public List<ProductRespondDTO> findAllProducts() {
        List<ProductRespondDTO> proList = productRepository.findAll().stream()
                .map(productMapper::toProductRespondDTO)
                .toList();
        if (proList.isEmpty()) {
            throw new ApiException(HttpStatus.NOT_FOUND, "NO_PRODUCT_FOUND");
        }
        return proList;
    }

    @Override
    public ProductRespondDTO findProductByUuid(String uuid) {
        Product product = productRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundUuid("PRODUCT", uuid));
        return productMapper.toProductRespondDTO(product);
    }

    @Override
    public void updateProductByUuid(String uuid, ProductUpdateDTO request){
        Product product = productRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundUuid("PRODUCT", uuid));
        productMapper.toProductUpdate(request, product);
        productRepository.save(product);
    }

    @Override
    public void deleteProduct(String uuid) {
        Product product = productRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundUuid("PRODUCT", uuid));
        productRepository.delete(product);
    }

    @Override
    public List<ProductRespondDTO> findByName(String name) {
        List<ProductRespondDTO> collect = productRepository.findByNameContainingIgnoreCase(name)
                .stream().map(productMapper::toProductRespondDTO).collect(Collectors.toList());
        if (collect.isEmpty()) {
            throw new ApiException(HttpStatus.NOT_FOUND, "NO_PRODUCT_FOUND");
        }
        return collect;
    }
}
