package co.istad.ishop.service;

import co.istad.ishop.model.request.ProductCreateDTO;
import co.istad.ishop.model.request.ProductUpdateDTO;
import co.istad.ishop.model.response.ProductRespondDTO;

import java.util.List;

public interface ProductService {

    void addProduct(ProductCreateDTO productCreateDTO);

    List<ProductRespondDTO> findAllProducts();

    ProductRespondDTO findProductByUuid(String uuid);

    void updateProductByUuid(String uuid, ProductUpdateDTO request);

    void deleteProduct(String uuid);

    List<ProductRespondDTO> findByName(String name);


}
