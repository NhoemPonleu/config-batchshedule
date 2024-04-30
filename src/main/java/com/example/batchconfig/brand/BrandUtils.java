package com.example.batchconfig.brand;

import com.example.batchconfig.brand.Brand;
import com.example.batchconfig.brand.exception.AllBrandsClosedException;

import java.util.List;

public class BrandUtils {

    public static void checkBrandClosed(List<Brand> brands) {
        boolean isOpenBrandExist = false;

        // Check if any brand is open
        for (Brand brand : brands) {
            if (brand.getCloseYN().equals("N")) {
                isOpenBrandExist = true;
                break;
            }
        }

        if (!isOpenBrandExist) {
            throw new AllBrandsClosedException();
        }
    }

}
