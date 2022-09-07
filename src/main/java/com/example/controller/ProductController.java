package com.example.controller;

import com.example.ProductMapper;
import com.example.ProductRepository;
import com.example.ProductService;
import com.example.entity.ExcelData;
import com.example.entity.Product;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/admin")
@Validated
public class ProductController {

    private final ProductService productService;
    private final ProductMapper mapper;
    private final ProductRepository productRepository;

    List<ExcelData> dataList = new ArrayList<>();
    public ProductController(ProductService productService,
                             ProductRepository productRepository,
                             ProductMapper mapper){
        this.productService = productService;
        this.productRepository = productRepository;
        this.mapper = mapper;
    }
    // localhost : 8080 / admin 페이지
    // admin 페이지에 파일 선택 버튼,  등록 버튼, 상품 목록 초기화 버튼
    // 등록 버튼 누르면 Post-> admin/postProducts 연결
    // 상품 목록 초기화 버튼 누르면 Delete -> admin/deleteProducts

    @PostMapping("/postProducts") //
    public String postProducts(@RequestParam("file") MultipartFile file) throws IOException{

        //productRepository.deleteAll(); // 우선 기본 정보들은 삭제
        List<ExcelData> dataList = new ArrayList<>();

        String extension = FilenameUtils.getExtension(file.getOriginalFilename()); // 3

        if (!extension.equals("xlsx") && !extension.equals("xls")) {
            throw new IOException("엑셀파일만 업로드 해주세요.");
        }

        Workbook workbook = null;

        if (extension.equals("xlsx")) {
            workbook = new XSSFWorkbook(file.getInputStream());
        } else if (extension.equals("xls")) {
            workbook = new HSSFWorkbook(file.getInputStream());
        }

        Sheet worksheet = workbook.getSheetAt(0);

        for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) { // 4

            Row row = worksheet.getRow(i);

            ExcelData data = new ExcelData();

            data.setImage_url(row.getCell(0).getStringCellValue());
            data.setProduct(row.getCell(1).getStringCellValue());
            data.setPrice(row.getCell(2).getStringCellValue()+"원");

            dataList.add(data);
        }

        if(dataList.isEmpty()){
            return "등록 실패";
        }
        else{
            productService.saveProducts(mapper.excelDatsToProducts(dataList));
            return "상품 목록이 등록되었습니다";
        }

    }

    @DeleteMapping("/deleteProducts")
    public String deleteProducts() {
        productRepository.deleteAll();
        return "삭제 완료";
    }



}
