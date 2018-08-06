package main;

import controller.HtmlParser;
import controller.XMLBuilder;
import model.Product;

import java.util.*;

public class Main {


    public static void main(String[] args) {
        String ProdName = null;
        if (args[0]!=null) {
            ProdName = args[0];
        }else System.out.println("Search line incorrect");

        int[] arCat = new int[]{138113, 20201, 20202};

        for (int a = 0; a < arCat.length; a++) {
            HtmlParser.setCategory(arCat[a]);

            String catName = null;
            switch (arCat[a]){
                case 138113 : catName = "Kinder";
                    break;
                case 20201 : catName = "Frauen";
                    break;
                case 20202 : catName = "Männer";
                    break;

            }



            HtmlParser htmlParser = new HtmlParser();
            htmlParser.setPattern(ProdName);
            htmlParser.amounOfProducts();
            List<Product> products = htmlParser.parser2(htmlParser.urlForAllPages());
            XMLBuilder xmlBuilder = new XMLBuilder();
            xmlBuilder.update(products, catName);

            long runTime = (System.nanoTime() - htmlParser.startTime) / 10000000;

            System.out.println("---------------------------------------------------------------------------");
            System.out.println("Run-time = " + runTime / 100 + " sec");
            System.out.println("---------------------------------------------------------------------------");

            System.out.println("Amount of triggered HTTP request " + htmlParser.httpRequests);
            System.out.println("---------------------------------------------------------------------------");


            System.out.println("Memory Footprint: " + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1024) + " kilobytes.");
            System.out.println("---------------------------------------------------------------------------");

        }
    }
}

