package controller;

import model.Product;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;


public class HtmlParser {

    public volatile static int httpRequests = 0;
    public long startTime = System.nanoTime();

    private String pattern;

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public static void setCategory(int a) {
        HtmlParser.a = a;
    }

    private static int a;


    //make our https links - link #1 and push to urlForAllPages
    public synchronized ArrayList<String> getUrlSetFromSearchByPattern() { //brand
        ArrayList<String> offersURLs = new ArrayList<>();
        int[] categories = new int[]{a}; /*138113, 20201, 20202;*/
        for (int category : categories) {
            offersURLs.add("https://www.aboutyou.de/suche?"
                    + "term=" + getPattern().replaceAll(" ", "+")
                    + "&category=" + category);
        }
        return offersURLs;
    }


    public synchronized List<Product> parser2(ArrayList<String> arrayList) {
        List<Product> products = new ArrayList<>();

        System.out.println("Please wait...\n");
        System.out.println("The file will be saved in: c:/userfolder/parsedProducts\n");

        for (String category : arrayList) {


            Document document = null;
            try {
                document = Jsoup.connect(category).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.print("- ");

            try {
                document = Jsoup.connect(category).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Elements elements;

            elements = document.body().getElementsByClass("styles__container--1bqmB").first().getElementsByAttribute("href");
            for (Element element : elements) {
                String url = element.attr("href");

                if (url.contains("/p/")) {
                    String url3 = "https://www.aboutyou.de" + url;


                    try {
                        document = Jsoup.connect(url3)
                                .timeout(10000)  //Set request timeout
                                .get();
                        httpRequests++;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Elements elements1 = document.select(".productPrices"); //price
                    Elements elements2 = document.select(".priceStyles__strike--PSBGK"); //initialPrice
                    Elements elements3 = document.select("h1"); //brand
                    Elements elements4 = document.select(".styles__title--UFKYd"); //color
                    Elements elements5 = document.select(".styles__title--3Jos_"); // name
                    Elements elements6 = document.select(".styles__articleNumber--1UszN"); //articul
                    Elements elements7 = document.select(".styles__textElement--3QlT_"); //description
                    Elements elements8 = document.select(".styles__label--1cfc7"); //shippingCosts


                    Product product = new Product();
                    product.setName(cleanHTML(elements5.text()));
                    product.setBrand(cleanHTML(brandCleaner(elements3.text())));
                    product.setColor(cleanHTML(elements4.text()));
                    product.setPrice(cleanHTML(elements1.text()));
                    product.setInitialPrice(cleanHTML(elements2.text()));
                    product.setDescription(cleanHTML(elements7.text()));
                    product.setArticleId(cleanHTML(elements6.text()));
                    product.setShippingCosts(cleanHTML(elements8.text()));
                    products.add(product);

                }
            }
        }

        return products;
    }

    private static String cleanHTML(String html) {
        return html
                .replaceAll("[<](/)?div[^>]*[>]", "")
                .replaceAll(" class?=\"[^>]*[\"]", "")
                .replaceAll(" data-reactid?=\"[^>]*[\"]", "")
                .replaceAll("<!--.*?-->", "")
                .replaceAll(" +", " ")
                .replaceAll("\n ", "\n")
                .replaceAll("\n+", "");
    }

    private static String brandCleaner(String a) {
        if (a.length() != 0) {
            return a.substring(0, a.indexOf("|"));
        } else return null;
    }

    //count of products
    public int amounOfProducts() {
        int qOfpages = 0;

        for (String url : getUrlSetFromSearchByPattern()) {

            Document document = null;
            try {
                document = Jsoup.connect(url).get();
            } catch (IOException e) {
                e.printStackTrace();
            }


            Elements elements10 = document.getElementsByClass("styles__brandProductCount--1VNm6");
            String qGoods = elements10.text();
            if (qGoods.length() != 0) {
                String tq = qGoods.replaceAll("[Produkte]", "");
                String tq2 = tq.replaceAll(" ", "");
                System.out.println("Amount of extracted products: " + tq2 + " in Category " + url);
                System.out.println("---------------------------------------------------------------------------");

            }
         else
             {
            elements10 = document.getElementsByClass("styles__productsCount--16QoZ");
            String qGoods2 = elements10.text();
            String tq = qGoods2.replaceAll("[Produkte]", "");
            String tq2 = tq.replaceAll(" ", "");
            System.out.println("Amount of extracted products: " + tq2 + " in Category " + url);
                 System.out.println("---------------------------------------------------------------------------");
        }
    }
        return qOfpages;
    }

    //push link to parser2
    public ArrayList<String> urlForAllPages() {

        boolean ar = true;

        ArrayList<String> urlList = new ArrayList<>();
        urlList.add(getUrlSetFromSearchByPattern().toString().replaceAll("^\\[|\\]$", ""));

        while (ar)
        {
            String newURL = urlList.get(urlList.size() - 1);

            Document doc0 = null;

            try {
                doc0 = Jsoup.connect(newURL).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements elements10 = doc0.body().getElementsByClass("styles__buttonNext--3YXvj").first().getElementsByAttribute("href");
            if (elements10.size() == 0) {
                ar = false;
            }
            for (Element element : elements10) {
                String url2 = element.attr("href");

                    String url3 = "https://www.aboutyou.de" + url2;
                    urlList.add(url3);

            }
        }
        return urlList;
    }
}

