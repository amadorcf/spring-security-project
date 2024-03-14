package amadorcf.es.springsecurityproject.controller;


import amadorcf.es.springsecurityproject.dto.SaveProduct;
import amadorcf.es.springsecurityproject.persistance.entity.Product;
import amadorcf.es.springsecurityproject.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/products")
public class ProductController {

    // SERVICE
    @Autowired
    private ProductService productService;

    //
    @GetMapping
    public ResponseEntity<Page<Product>> findAll(Pageable pageable){

        // Servicio del metodo con el que se obtienen los productos paginados
        Page<Product> productsPage = productService.findAll(pageable);

        // Validacion
        if(productsPage.hasContent()){
            return ResponseEntity.ok(productsPage);
        }

        //return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{productId}")
    public ResponseEntity<Product> findOneById(@PathVariable Long productId){

        // Servicio para obtener un producto
        Optional<Product> product = productService.findOneById(productId);

        // Validacion
        if(product.isPresent()){
            return ResponseEntity.ok(product.get());
        }

        return ResponseEntity.notFound().build();
        // Otra opcion... return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

    }

    // Metodo que recibe el DTO saveProduct
    @PostMapping
    public ResponseEntity<Product> createOne(@RequestBody @Valid SaveProduct saveProduct){

        Product product = productService.createOne(saveProduct);

        return ResponseEntity.status(HttpStatus.CREATED).body(product);

    }

    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateOneById(@PathVariable Long productId,
                                                 @RequestBody @Valid SaveProduct saveProduct){

        Product product = productService.updateOneById(productId, saveProduct);

        return ResponseEntity.ok(product);

    }

    @PutMapping("/{productId}/disabled")
    public ResponseEntity<Product> disabledOneById(@PathVariable Long productId){

        Product product = productService.disabledOneById(productId);

        return ResponseEntity.ok(product);

    }



}
