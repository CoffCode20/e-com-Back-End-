package co.istad.ishop.service.impl;

import co.istad.ishop.entities.Brand;
import co.istad.ishop.exception.ApiException;
import co.istad.ishop.exception.ResourceNotFoundUuid;
import co.istad.ishop.mapper.BrandMapper;
import co.istad.ishop.model.request.BrandCreateDTO;
import co.istad.ishop.model.response.BrandRespondDTO;
import co.istad.ishop.repository.BrandRepository;
import co.istad.ishop.service.BrandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final BrandMapper brandMapper;

    @Override
    public List<BrandRespondDTO> findAllBrands() {
        List<BrandRespondDTO> brandList = brandRepository.findAll().stream()
                .map(brandMapper::toBrandRespondDTO)
                .toList();
        if(brandList.isEmpty()) {
            throw new ApiException(HttpStatus.NOT_FOUND, "BRAND_NOT_FOUND");
        }
        return brandList;
    }

    @Override
    public void addBrand(BrandCreateDTO brandCreateDTO) {
        if (brandRepository.existsBrandsByName(brandCreateDTO.name())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Brands already exist.");
        }
        Brand createBrand = brandMapper.toBrandCreate(brandCreateDTO);
        createBrand.setUuid(UUID.randomUUID().toString());
        brandRepository.save(createBrand);
    }

    @Override
    public BrandRespondDTO findBrandByUuid(String uuid) {
        return brandRepository.findAll().stream()
                .filter(brand -> brand.getUuid().equals(uuid))
                .map(brandMapper::toBrandRespondDTO)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundUuid("BRAND", uuid));
    }

    @Override
    public void deleteBrandByUuid(String uuid) {
        Brand brand = brandRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundUuid("BRAND", uuid));
        brandRepository.delete(brand);
    }

    @Override
    public void updateBrandByUuid(String uuid, BrandCreateDTO brandCreateDTO) {
        Brand brand = brandRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResourceNotFoundUuid("BRAND", uuid));

        if (brandCreateDTO.name() != null) {
            brand.setName(brandCreateDTO.name());
        }

        brandRepository.save(brand);
    }
}
