package com.evolgames.utilities;

import com.badlogic.gdx.math.Vector2;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.OutputStream;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class XmlUtils {

    public static Element createVectorElement(Document document, String tag, Vector2 v) {
        Element vectorElement = document.createElement(tag);
        vectorElement.setAttribute("x", String.valueOf(v.x));
        vectorElement.setAttribute("y", String.valueOf(v.y));
        return vectorElement;
    }

    public static Vector2 readVector(Element element) {
        float x = Float.parseFloat(element.getAttribute("x"));
        float y = Float.parseFloat(element.getAttribute("y"));
        return new Vector2(x, y);
    }

    // write doc to output stream
    public static void writeXml(Document doc, OutputStream output) throws TransformerException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(output);

        transformer.transform(source, result);
    }
}
