package hello.hellospring.service;

import hello.hellospring.domain.Brand;
import hello.hellospring.repository.BrandRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BrandService {
    private final BrandRepository brandRepository;

    public BrandService(BrandRepository brandRepository) {
        this.brandRepository = brandRepository;
    }

    public Optional<Brand> findBrandByName(String name) {
        return brandRepository.findByName(name);
    }//이름을 통해 브랜드를 찾는 서비스

    public Optional<Long> findBrandIdByName(String name) {
        return brandRepository.findByName(name).map(Brand::getId);
    }//브랜드 이름을 통해 브랜드 ID를 찾는 서비스

    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }//모든 브랜드를 불러오는 서비스
    public List<Brand> getBrandsOrderByAverageRating() {
        return brandRepository.findAllOrderByAverageRatingDesc();
    }//브랜드의 평점 평균을 불러오는 서비스
    public Optional<Brand> findById(Long id) {
        return brandRepository.findById(id);
    }//ID를 통해 찾는 서비스

    public Optional<Brand> findBrandById(Long brandId) {
        return brandRepository.findById(brandId);
    }
}