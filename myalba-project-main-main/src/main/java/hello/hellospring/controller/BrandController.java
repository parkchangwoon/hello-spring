package hello.hellospring.controller;

import hello.hellospring.domain.Brand;
import hello.hellospring.service.BrandService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/brands")
public class BrandController {
    private final BrandService brandService;

    public BrandController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping("/{name}")
    public ResponseEntity<Brand> getBrandByName(@PathVariable("name") String name) {
        Optional<Brand> brand = brandService.findBrandByName(name);
        return brand.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(404).body(null));
    }

    @GetMapping("/top")//브랜드 평점 평균 높은 순위로 출력 메서드
    public ResponseEntity<List<BrandResponse>> getTopRatedBrands() {
        List<Brand> brands = brandService.getBrandsOrderByAverageRating();
        List<BrandResponse> response = brands.stream()
                .map(brand -> new BrandResponse(brand.getName(), brand.getAverageRating()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // BrandResponse 클래스 정의(브랜드 평점 평균 관리)
    public static class BrandResponse {
        private String name;
        private double averageRating;

        public BrandResponse(String name, double averageRating) {
            this.name = name;
            this.averageRating = averageRating;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public double getAverageRating() {
            return averageRating;
        }

        public void setAverageRating(double averageRating) {
            this.averageRating = averageRating;
        }
    }
}


