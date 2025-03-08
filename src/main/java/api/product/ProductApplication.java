package api.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class ProductApplication {
    private static final Map<Integer, Product> productRepo = new HashMap<>();

    public static void main(String[] args) {
        SpringApplication.run(ProductApplication.class, args);
    }

    public static class Product {
        public int id;
        public String name;
        public double price;
        public int stock;

        public Product(int id, String name, double price, int stock) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.stock = stock;
        }
    }

	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	public static class NotFoundException extends RuntimeException {
		public NotFoundException(String message) {
			super(message);
		}
	}

	@ResponseStatus(value = HttpStatus.CONFLICT)
	public static class ConflictException extends RuntimeException {
		public ConflictException(String message) {
			super(message);
		}
	}

    @RestController
    @RequestMapping("/products")
    public static class ProductController {

        @GetMapping
        public Collection<Product> getAllProducts() {
            return productRepo.values();
        }

        @GetMapping("/{id}")
        public Product getProduct(@PathVariable int id) {
            if (!productRepo.containsKey(id)) {
				throw new NotFoundException("id not found.");
            }
            return productRepo.get(id);
        }

		@PostMapping
		public ResponseEntity<String> addProduct(@RequestBody Product product) {
			if (productRepo.containsKey(product.id)) {
				throw new ConflictException("id exists");
			}
			productRepo.put(product.id, product);
			return ResponseEntity.status(HttpStatus.CREATED).body("added");
		}

		@PutMapping("/{id}")
		public ResponseEntity<String> updateProduct(@PathVariable int id, @RequestBody Product product) {
			if (!productRepo.containsKey(id)) {
				throw new NotFoundException("id not found.");
			}
			productRepo.put(id, product);
			return ResponseEntity.ok().body("updated");
		}

		@DeleteMapping("/{id}")
		public ResponseEntity<String> deleteProduct(@PathVariable int id) {
			if (!productRepo.containsKey(id)) {
				throw new NotFoundException("id not found.");
			}
			productRepo.remove(id);
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("deleted");
		}
    }
}

