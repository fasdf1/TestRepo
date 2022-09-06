package com.example.controller;

import com.example.ProductMapper;
import com.example.ProductService;
import com.example.entity.ExcelData;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*@RestController*/
@Controller         // 테스트 컨트롤러
@Validated
@Slf4j
public class FileController {

    private final ProductService productService;
    private final ProductMapper mapper;
    public FileController(ProductService productService,
                          ProductMapper mapper){
        this.productService = productService;
        this.mapper = mapper;

    }

    @GetMapping("/excel")
    public String main() { // 1
        return "excel";
    }

    @PostMapping("/excel/read")
    public String register(@RequestParam("file") MultipartFile file, Model model) throws IOException { // 2

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
            //data.setPrice((int)row.getCell(2).getNumericCellValue());

            //private String image_url;
            //    private String product;
            //    private Integer price;
            dataList.add(data);
        }

        model.addAttribute("datas", dataList); // 5

        productService.saveProducts(mapper.excelDatsToProducts(dataList));

        return "excelList";
        /*return "등록 성공!";*/
    }


}
