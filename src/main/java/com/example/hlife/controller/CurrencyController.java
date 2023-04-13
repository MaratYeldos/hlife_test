package com.example.hlife.controller;

import com.example.hlife.model.Currency;
import com.example.hlife.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/currency")
public class CurrencyController {

    @Autowired
    private CurrencyService currencyService;

    @GetMapping("/save/{date}")
    public ResponseEntity<?> saveData(@PathVariable("date") String date) {
        String apiUrl = "https://nationalbank.kz/rss/get_rates.cfm?fdate=" + date;
        RestTemplate restTemplate = new RestTemplate();
        String xmlResponse = restTemplate.getForObject(apiUrl, String.class);

        List<Currency> currencies = parseXmlResponse(xmlResponse, date);
        int savedCount = currencyService.saveCurrencyData(currencies);

        if (savedCount > 0) {
            Map<String, Integer> responseMap = new HashMap<>();
            responseMap.put("count", savedCount);
            return ResponseEntity.ok(responseMap);
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private List<Currency> parseXmlResponse(String xmlResponse, String date) {
        List<Currency> currencies = new ArrayList<>();
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            InputSource inputSource = new InputSource(new StringReader(xmlResponse));
            Document doc = dBuilder.parse(inputSource);

            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("item");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    String title = element.getElementsByTagName("title").item(0).getTextContent();
                    String code = title.substring(0, 3);
                    String valueStr = element.getElementsByTagName("description").item(0).getTextContent();
                    BigDecimal value = new BigDecimal(valueStr);

                    Currency currency = new Currency(null, title, code, value, LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                    currencies.add(currency);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return currencies;
    }

    @GetMapping("/{date}/{code}")
    public ResponseEntity<List<Currency>> getDataByDateAndCode(
            @PathVariable("date") String date,
            @PathVariable("code") String code) {
        LocalDate localDate = LocalDate.parse(date);
        List<Currency> currencies = currencyService.findByDateAndCode(localDate, code);
        return ResponseEntity.ok(currencies);
    }

    @GetMapping("/{date}")
    public ResponseEntity<List<Currency>> getDataByDate(@PathVariable("date") String date) {
        LocalDate localDate = LocalDate.parse(date);
        List<Currency> currencies = currencyService.findByDate(localDate);
        return ResponseEntity.ok(currencies);
    }
}
