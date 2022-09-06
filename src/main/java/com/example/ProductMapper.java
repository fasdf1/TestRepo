package com.example;

import com.example.entity.ExcelData;
import com.example.entity.Product;
import org.mapstruct.Mapper;

import java.util.List;
@Mapper(componentModel = "spring")
public interface ProductMapper {

    List<Product> excelDatsToProducts (List<ExcelData> dataList);
}
