package controller;

import model.Product;
import model.Products;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.*;
import java.util.List;


public class XMLBuilder {
    String filePath;
    String userFilePath = System.getProperty("user.dir");
    File fileFolder = new File(userFilePath + System.getProperty("file.separator") + "parsedProducts");



    public void update(List<Product> products, String category) {
        if (!fileFolder.exists()){
            fileFolder.mkdir();
        }
        filePath = fileFolder.getAbsolutePath() + System.getProperty("file.separator") + "offers_"+category+".xml";
        try {
            Products products1 = new Products();
            products1.setProductList(products);
            File file = new File(filePath);
            JAXBContext jaxbContext = JAXBContext.newInstance(Products.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.marshal(products1, file);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
