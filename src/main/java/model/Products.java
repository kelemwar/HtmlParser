package model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;



@XmlRootElement
public class Products {
    @XmlElement(name = "offer")
    private List<Product> productList;

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    @Override
    public String toString() {
        return "model.Products{" +
                "productList=" + productList +
                '}';
    }
}
