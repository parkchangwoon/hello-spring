package hello.hellospring.controller;

import hello.hellospring.domain.Brand;
import hello.hellospring.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/home")
public class HomeController {

    private final BrandService brandService;

    @Autowired
    public HomeController(BrandService brandService) {
        this.brandService = brandService;
    }

    @GetMapping("/brands")
    public ResponseEntity<List<Brand>> getAllBrands() {
        List<Brand> brands = brandService.getAllBrands();
        return ResponseEntity.ok(brands);
    }
}