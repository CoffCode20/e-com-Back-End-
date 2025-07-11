package co.istad.ishop.service;

import co.istad.ishop.model.request.BrandCreateDTO;
import co.istad.ishop.model.response.BrandRespondDTO;

import java.util.List;

public interface BrandService {
    List<BrandRespondDTO> findAllBrands();

    void addBrand(BrandCreateDTO  brandCreateDTO);

    BrandRespondDTO findBrandByUuid(String uuid);

    void deleteBrandByUuid(String uuid);

    void updateBrandByUuid(String uuid, BrandCreateDTO brandCreateDTO);
}
