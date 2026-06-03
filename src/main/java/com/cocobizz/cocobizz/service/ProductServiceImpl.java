package com.cocobizz.cocobizz.service;

import com.cocobizz.cocobizz.dto.ProductDto;
import com.cocobizz.cocobizz.entity.*;
import com.cocobizz.cocobizz.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.nio.file.*;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepo;
    private final CategoryRepository categoryRepo;
    private final StockInflowRepository stockRepo;
    private final ShipmentRepository shipmentRepo;
    private final WarehouseRepository warehouseRepo;
    private final UserRepository userRepo;

    private final String DIR = "C:/cocobizz/uploads/products/";

    // ================= CREATE PRODUCT =================
    @Override
    public ProductDto createProduct(Long stockInflowId,
                                    String name,
                                    Long categoryId,
                                    Double price,
                                    String unitType,
                                    String desc,
                                    MultipartFile image,
                                    Long currentUserId) {

        // 🔐 ROLE CHECK
        Users user = userRepo.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getRole() != Role.ADMIN && user.getRole() != Role.SELLER) {
            throw new RuntimeException("Only ADMIN or SELLER can create product");
        }

        // 📦 STOCK FETCH
        StockInflow stock = stockRepo.findById(stockInflowId)
                .orElseThrow(() -> new RuntimeException("Stock not found"));

        // 💰 PAYMENT CHECK
        if (!Boolean.TRUE.equals(stock.getPaymentDone())) {
            throw new RuntimeException("Payment not completed");
        }

        // 🚚 SHIPMENT CHECK
        List<Shipment> shipments = shipmentRepo.findByStockInflow_Id(stockInflowId);

        if (shipments.isEmpty()) {
            throw new RuntimeException("Shipment not assigned");
        }

        Shipment shipment = shipments.get(0);

        if (shipment.getStatus() != ShipmentStatus.DELIVERED) {
            throw new RuntimeException("Stock not available or not delivered");
        }

        // 🏬 WAREHOUSE CHECK
        Warehouse warehouse = stock.getWarehouse();

        if (warehouse.getCurrentStock() == null ||
                warehouse.getCurrentStock().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("No stock available in warehouse");
        }

        // 📊 QUANTITY CHECK
        BigDecimal available = warehouse.getCurrentStock();

        if (available.compareTo(stock.getQuantity()) < 0) {
            throw new RuntimeException("Insufficient quantity in warehouse");
        }

        // 🚫 DUPLICATE PRODUCT CHECK
        List<Product> existing = productRepo.findByNameContainingIgnoreCaseAndActiveTrue(name);
        if (!existing.isEmpty()) {
            throw new RuntimeException("Product already exists");
        }

        // 📂 CATEGORY
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        // 🖼 IMAGE
        String fileName = image != null && !image.isEmpty() ? save(image) : null;

        // ✅ CREATE PRODUCT (FIXED WITH STOCK)
        Product product = Product.builder()
                .name(name)
                .category(category)
                .unitPrice(BigDecimal.valueOf(price))
                .unitType(unitType)
                .description(desc)
                .imageUrl(fileName)
                .quantity(stock.getQuantity())

                // ✅ FIX (ONLY THIS LINE ADDED)
                .warehouse(stock.getWarehouse())

                .active(true)
                .build();

        productRepo.save(product);

        // 📉 REDUCE STOCK
        warehouse.setCurrentStock(warehouse.getCurrentStock().subtract(stock.getQuantity()));
        warehouseRepo.save(warehouse);

        return map(product);
    }

    // ================= GET ALL =================
    @Override
    public List<ProductDto> getAllProducts() {
        return productRepo.findByActiveTrue()
                .stream().map(this::map).toList();
    }

    // ================= GET BY ID =================
    @Override
    public ProductDto getProductById(Long id) {
        return map(productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found")));
    }

    // ================= UPDATE =================
    @Override
    public ProductDto updateProduct(Long id, String name, Long categoryId,
                                    Double price, String unitType,
                                    String desc, MultipartFile image) {

        Product p = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Category cat = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        p.setName(name);
        p.setCategory(cat);
        p.setUnitPrice(BigDecimal.valueOf(price));
        p.setUnitType(unitType);
        p.setDescription(desc);

        if (image != null && !image.isEmpty()) {
            delete(p.getImageUrl());
            p.setImageUrl(save(image));
        }

        return map(productRepo.save(p));
    }

    // ================= DELETE =================
    @Override
    public void deleteProduct(Long id) {
        Product p = productRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        p.setActive(false);
        productRepo.save(p);
    }

    // ================= CATEGORY =================
    @Override
    public List<ProductDto> getProductsByCategory(Long categoryId) {
        Category c = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        return productRepo.findByCategoryAndActiveTrue(c)
                .stream().map(this::map).toList();
    }

    // ================= SEARCH =================
    @Override
    public List<ProductDto> searchProducts(String name) {
        return productRepo.findByNameContainingIgnoreCaseAndActiveTrue(name)
                .stream().map(this::map).toList();
    }

    // ================= FILE =================
    private String save(MultipartFile file) {
        try {
            File dir = new File(DIR);
            if (!dir.exists()) dir.mkdirs();

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

            Files.copy(file.getInputStream(),
                    Paths.get(DIR + fileName),
                    StandardCopyOption.REPLACE_EXISTING);

            return fileName;

        } catch (Exception e) {
            throw new RuntimeException("Image upload failed");
        }
    }

    private void delete(String name) {
        if (name != null) new File(DIR + name).delete();
    }

    // ================= DTO =================
    private ProductDto map(Product p) {

        Category c = p.getCategory(); // ✅ SAFE

        return ProductDto.builder()
                .id(p.getId())
                .name(p.getName())

                // ✅ CATEGORY FIX (STRONG SAFE)
                .categoryId(c != null ? c.getId() : null)
                .categoryName(c != null ? c.getCategoryName() : null)
                .categoryImage(
                        (c != null && c.getCategoryImage() != null)
                                ? "/category-images/" + c.getCategoryImage()
                                : null
                )

                // ✅ PRODUCT DATA
                .unitPrice(p.getUnitPrice())
                .unitType(p.getUnitType())
                .description(p.getDescription())

                // ✅ PRODUCT IMAGE FIX
                .imageUrl(
                        p.getImageUrl() != null
                                ? "/product-images/" + p.getImageUrl()
                                : null
                )

                .build();
    }
}