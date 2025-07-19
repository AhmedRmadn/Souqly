package com.github.souqly.souqly.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value; // Import @Value
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.github.souqly.souqly.Exception.UnAuthorizedException;
import com.github.souqly.souqly.payload.response.ProductImageResponse;
import com.github.souqly.souqly.repository.ProductRepository;

@Service
public class ProductImageService {

	@Autowired
	ProductRepository productRepository;

	// 1. FIX: Inject the upload directory path from application.properties
	private final Path rootLocation;

	@Autowired
    public ProductImageService(ProductRepository productRepository, @Value("${file.upload-dir}") String uploadDir) {
        this.productRepository = productRepository;
        this.rootLocation = Paths.get(uploadDir);
    }


	private String generatingImageIdName() {
		return UUID.randomUUID().toString();
	}

	public ProductImageResponse uploadImageForProduct(MultipartFile file, String productId, String userId) {
		String productSeller = productRepository.findProductSeller(productId);
		if (productSeller == null || !productSeller.equals(userId)) {
			throw new UnAuthorizedException("UNAUTHORIZED: You do not have permission to modify this product or it does not exist.");
		}
		
		try {
            // 2. FIX: Ensure the root directory (e.g., C:/souqly-uploads/images/) exists
			Files.createDirectories(this.rootLocation);

			String originalFilename = file.getOriginalFilename();
			String extension = "";
			if (originalFilename != null && originalFilename.contains(".")) {
				extension = originalFilename.substring(originalFilename.lastIndexOf("."));
			}

			String filename = generatingImageIdName() + extension;
			
			// 3. FIX: Resolve the full, absolute path for the new file
			Path destinationFile = this.rootLocation.resolve(Paths.get(filename)).toAbsolutePath();

			// Save the file
			file.transferTo(destinationFile.toFile());

			// The URL path should be simple for the web server to handle
			String imageUrl = "/images/" + filename;

			ProductImageResponse response = new ProductImageResponse();
			response.setImageUrl(imageUrl);
			productRepository.uploadImage(imageUrl, productId);
			return response;

		} catch (IOException e) {
			throw new RuntimeException("Failed to upload image. Please try again later.", e);
		}
	}
}