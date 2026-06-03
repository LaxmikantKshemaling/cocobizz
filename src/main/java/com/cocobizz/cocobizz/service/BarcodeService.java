package com.cocobizz.cocobizz.service;

import com.google.zxing.*;
import com.google.zxing.client.j2se.*;
import com.google.zxing.common.BitMatrix;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class BarcodeService {

    private static final String BASE_PATH = "C:/cocobizz/barcodes/";

    public String generateBarcode(String text) {
        try {

            Files.createDirectories(Paths.get(BASE_PATH));

            BitMatrix matrix = new MultiFormatWriter()
                    .encode(text, BarcodeFormat.CODE_128, 400, 150);

            String fileName = text + ".png";

            Path path = Paths.get(BASE_PATH + fileName);

            MatrixToImageWriter.writeToPath(matrix, "PNG", path);

            System.out.println("✅ Barcode Created: " + path);

            return "/barcodes/" + fileName;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Barcode failed");
        }
    }
}