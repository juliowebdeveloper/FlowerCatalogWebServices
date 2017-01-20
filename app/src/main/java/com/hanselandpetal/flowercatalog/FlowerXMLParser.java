package com.hanselandpetal.flowercatalog;

import com.hanselandpetal.flowercatalog.model.Flower;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Teste2 on 20/01/2017.
 */

public class FlowerXMLParser {

        public static List<Flower> parseFeed(String content) {

            try {

                boolean inDataItemTag = false; //Diz se estou numa tag que me interessa
                String currentTagName = ""; //Qual tag estou
                Flower flower = null;
                List<Flower> flowerList = new ArrayList<>();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = factory.newPullParser();
                parser.setInput(new StringReader(content));

                int eventType = parser.getEventType();
                //Ele gera eventos, um para cada tag
                while (eventType != XmlPullParser.END_DOCUMENT) {//Se não for o fim do document

                    switch (eventType) {
                        case XmlPullParser.START_TAG:
                            currentTagName = parser.getName();
                            if (currentTagName.equals("product")) {
                                inDataItemTag = true;
                                flower = new Flower();
                                flowerList.add(flower);
                            }
                            break;

                        case XmlPullParser.END_TAG:
                            if (parser.getName().equals("product")) {
                                inDataItemTag = false;
                            }
                            currentTagName = "";
                            break;

                        case XmlPullParser.TEXT:
                            if (inDataItemTag && flower != null) {
                                switch (currentTagName) {
                                    case "productId":
                                        flower.setProductId(Integer.parseInt(parser.getText()));
                                        break;
                                    case "name":
                                        flower.setName(parser.getText());
                                        break;
                                    case "instructions":
                                        flower.setInstructions(parser.getText());
                                        break;
                                    case "category":
                                        flower.setCategory(parser.getText());
                                        break;
                                    case "price" :
                                        flower.setPrice(Double.parseDouble(parser.getText()));
                                        break;
                                    case "photo" :
                                        flower.setPhoto(parser.getText());
                                    default:
                                        break;
                                }
                            }
                            break;
                    }

                    eventType = parser.next();

                }

                return flowerList;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }


        }

}
