package uk.co.raharrison.mathengine.unitconversion.units.currency;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

class XmlCurrencyProvider extends CurrencyProvider
{
	private Document document;
	
	XmlCurrencyProvider(String url)
	{
		super(url);
	}

	@Override
	void download() throws SAXException, IOException, ParserConfigurationException
	{
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		Document doc = dBuilder.parse(new URL(getUrl()).openStream());

		this.document = doc;
	}

	@Override
	List<Unit> process() throws ParseException
	{
		if(document == null)
			return null;
		
		document.getDocumentElement().normalize();

		List<Unit> results = new ArrayList<Unit>();
		NodeList elements = document.getElementsByTagName("Cube");

		for (int i = 0; i < elements.getLength(); i++)
		{
			Node node = elements.item(i);

			NamedNodeMap map = node.getAttributes();
			int length = map.getLength();

			if (length == 1)
			{
				lastUpdated = new SimpleDateFormat("yyyy-MM-dd").parse(map.getNamedItem("time")
						.getNodeValue());
			}
			else if (length == 2)
			{
				double rate = Double.parseDouble(map.getNamedItem("rate").getNodeValue());
				String currency = map.getNamedItem("currency").getNodeValue();
				
				results.add(new Unit(currency, rate));
			}
		}
		
		results.add(new Unit("eur", 1.0));
		return results;
	}

}
