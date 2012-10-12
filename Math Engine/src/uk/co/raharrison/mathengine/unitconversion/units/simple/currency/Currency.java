package uk.co.raharrison.mathengine.unitconversion.units.simple.currency;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import uk.co.raharrison.mathengine.MathUtils;
import uk.co.raharrison.mathengine.unitconversion.units.Conversion;
import uk.co.raharrison.mathengine.unitconversion.units.SubUnit;
import uk.co.raharrison.mathengine.unitconversion.units.simple.SimpleSubUnit;
import uk.co.raharrison.mathengine.unitconversion.units.simple.SimpleUnitGroup;

public class Currency extends SimpleUnitGroup
{
	private Date lastUpdated;
	
	private static final Pattern patternJson = Pattern.compile("\"([A-Z]+)\":\\s(\\d+\\.?\\d*)",
			Pattern.CASE_INSENSITIVE);

	private static final String primaryUrl = "http://openexchangerates.org/api/latest.json";
	private static final String secondaryUrl = "http://www.ecb.int/stats/eurofxref/eurofxref-daily.xml";

	public Currency()
	{
		this(false);
	}

	public Currency(boolean downloadCurrencies)
	{
		super();
		
		GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("bst"), Locale.ENGLISH);
		//Thurs Aug 02 19:02:11 BST 2012
		calendar.set(2012, Calendar.AUGUST, 2, 18, 2, 11);
		
		lastUpdated = calendar.getTime();

		if (downloadCurrencies)
		{
			update();
		}
	}

	@Override
	protected double doConversion(Conversion params, double amount)
	{
		SimpleSubUnit from, to;

		if ((from = (CurrencySimpleSubUnit) params.getFrom()) != null
				&& (to = (CurrencySimpleSubUnit) params.getTo()) != null)
		{
			double result =  amount * to.getConversion() / from.getConversion();
			
			return MathUtils.round(result, 2);
		}

		throw new IllegalArgumentException("Unable to handle units " + params.getFrom() + " and "
				+ params.getTo());
	}

	private Document downloadXML(String url) throws ParserConfigurationException,
			MalformedURLException, SAXException, IOException
	{
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

		Document doc = dBuilder.parse(new URL(url).openStream());

		return doc;
	}
	
	private String downloadJson(String url) throws MalformedURLException, IOException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(new URL(url).openStream()));

		String in = "";
		StringBuilder builder = new StringBuilder();

		while ((in = reader.readLine()) != null)
		{
			builder.append("\n" + in);
		}
		
		reader.close();
		
		return builder.toString();
	}
	
	@Override
	// TODO : Implement correct currency plurals
	protected void fillUnits()
	{
//		units.add(new CurrencySimpleSubUnit("U.S dollar", "U.S dollars",
//				new String[] { "usd", "$" }, 1.2426));
//		units.add(new CurrencySimpleSubUnit("Japanese yen", "Japanese yen", new String[] { "jpy" },
//				99.14));
//		units.add(new CurrencySimpleSubUnit("Bulgarian lev", "Bulgarian levove",
//				new String[] { "bgn" }, 1.9558));
//		units.add(new CurrencySimpleSubUnit("Czech koruna", "Czech koruny", new String[] { "czk" },
//				25.457));
//		units.add(new CurrencySimpleSubUnit("Danish krone", "Danish kroner",
//				new String[] { "dkk" }, 7.4367));
//		units.add(new CurrencySimpleSubUnit("British pound", "British pounds", new String[] {
//				"gbp", "£", "british pound", "british pounds", "pound sterling", "pounds sterling",
//				"pound", "pounds" }, 0.7984));
//		units.add(new CurrencySimpleSubUnit("Euro", "Euros", new String[] { "eur", "€" }, 1.0));
//		units.add(new CurrencySimpleSubUnit("Hungarian forint", "Hungarian forint",
//				new String[] { "huf" }, 285.95));
//		units.add(new CurrencySimpleSubUnit("Lithuanian litas", "Lithuanian litai",
//				new String[] { "ltl" }, 3.4528));
//		units.add(new CurrencySimpleSubUnit("Latvian lat", "Latvian lati", new String[] { "lvl" },
//				0.6959));
//		units.add(new CurrencySimpleSubUnit("Polish zloty", "Polish zloty", new String[] { "pln", "zloty" },
//				4.206));
//		units.add(new CurrencySimpleSubUnit("Romanian leu", "Romanian lei", new String[] { "ron" },
//				4.4865));
//		units.add(new CurrencySimpleSubUnit("Swedish krona", "Swedish kronor",
//				new String[] { "sek" }, 8.6177));
//		units.add(new CurrencySimpleSubUnit("Swiss franc", "Swiss francs", new String[] { "chf" },
//				1.2012));
//		units.add(new CurrencySimpleSubUnit("Norwegian krone", "Norwegian kroner",
//				new String[] { "nok" }, 7.476));
//		units.add(new CurrencySimpleSubUnit("Croatian kuna", "Croatian kuna",
//				new String[] { "hrk" }, 7.4963));
//		units.add(new CurrencySimpleSubUnit("Russian ruble", "Russian rubles",
//				new String[] { "rub" }, 40.351));
//		units.add(new CurrencySimpleSubUnit("Turkish lira", "Turkish lira", new String[] { "try" },
//				2.2467));
//		units.add(new CurrencySimpleSubUnit("Australian dollar", "Australian dollars",
//				new String[] { "aud" }, 1.2071));
//		units.add(new CurrencySimpleSubUnit("Brazil real", "Brazil reais", new String[] { "brl" },
//				2.5116));
//		units.add(new CurrencySimpleSubUnit("Canadian dollar", "Canadian dollars",
//				new String[] { "cad" }, 1.258));
//		units.add(new CurrencySimpleSubUnit("Chinese yuan", "Chinese yuan", new String[] { "cny" },
//				7.8992));
//		units.add(new CurrencySimpleSubUnit("Hong Kong dollar", "Hong Kong dollars",
//				new String[] { "hkd" }, 9.636));
//		units.add(new CurrencySimpleSubUnit("Indonesian rupiah", "Indonesian rupiah",
//				new String[] { "idr" }, 11639.91));
//		units.add(new CurrencySimpleSubUnit("Israeli shekel", "Israeli shekalim",
//				new String[] { "ils" }, 4.8683));
//		units.add(new CurrencySimpleSubUnit("Indian rupee", "Indian rupees",
//				new String[] { "inr" }, 68.284));
//		units.add(new CurrencySimpleSubUnit("South Korean won", "South Korean won",
//				new String[] { "krw" }, 1409.68));
//		units.add(new CurrencySimpleSubUnit("Mexican peso", "Mexican pesos",
//				new String[] { "mxn" }, 16.5626));
//		units.add(new CurrencySimpleSubUnit("Malaysian ringgit", "Malaysian ringgit",
//				new String[] { "myr" }, 3.9313));
//		units.add(new CurrencySimpleSubUnit("New Zealand dollar", "New Zealand dollars",
//				new String[] { "nzd" }, 1.5448));
//		units.add(new CurrencySimpleSubUnit("Philippine peso", "Philippine pesos",
//				new String[] { "php" }, 51.822));
//		units.add(new CurrencySimpleSubUnit("Singapore dollar", "Singapore dollars",
//				new String[] { "sgd" }, 1.5695));
//		units.add(new CurrencySimpleSubUnit("Thai baht", "Thai baht", new String[] { "thb" },
//				39.192));
//		units.add(new CurrencySimpleSubUnit("South African rand", "South African rand",
//				new String[] { "zar" }, 10.0836));
		
		units.add(new CurrencySimpleSubUnit("United Arab Emirates Dirham", "United Arab Emirates Dirham", new String[] { "aed" }, 3.672975));
		units.add(new CurrencySimpleSubUnit("Afghan Afghani", "Afghan Afghani", new String[] { "afn" }, 48.5702));
		units.add(new CurrencySimpleSubUnit("Albanian Lek", "Albanian Lek", new String[] { "all" }, 112.68));
		units.add(new CurrencySimpleSubUnit("Armenian Dram", "Armenian Dram", new String[] { "amd" }, 408.719999));
		units.add(new CurrencySimpleSubUnit("Netherlands Antillean Guilder", "Netherlands Antillean Guilder", new String[] { "ang" }, 1.7763));
		units.add(new CurrencySimpleSubUnit("Angolan Kwanza", "Angolan Kwanza", new String[] { "aoa" }, 95.378998));
		units.add(new CurrencySimpleSubUnit("Argentine Peso", "Argentine Peso", new String[] { "ars" }, 4.583151));
		units.add(new CurrencySimpleSubUnit("Australian Dollar", "Australian Dollar", new String[] { "aud" }, 0.95695));
		units.add(new CurrencySimpleSubUnit("Aruban Florin", "Aruban Florin", new String[] { "awg" }, 1.792));
		units.add(new CurrencySimpleSubUnit("Azerbaijani Manat", "Azerbaijani Manat", new String[] { "azn" }, 0.785657));
		units.add(new CurrencySimpleSubUnit("Bosnia-Herzegovina Convertible Mark", "Bosnia-Herzegovina Convertible Mark", new String[] { "bam" }, 1.609625));
		units.add(new CurrencySimpleSubUnit("Barbadian Dollar", "Barbadian Dollar", new String[] { "bbd" }, 2.0));
		units.add(new CurrencySimpleSubUnit("Bangladeshi Taka", "Bangladeshi Taka", new String[] { "bdt" }, 82.063366));
		units.add(new CurrencySimpleSubUnit("Bulgarian Lev", "Bulgarian Lev", new String[] { "bgn" }, 1.600738));
		units.add(new CurrencySimpleSubUnit("Bahraini Dinar", "Bahraini Dinar", new String[] { "bhd" }, 0.37702));
		units.add(new CurrencySimpleSubUnit("Burundian Franc", "Burundian Franc", new String[] { "bif" }, 1450.23));
		units.add(new CurrencySimpleSubUnit("Bermudan Dollar", "Bermudan Dollar", new String[] { "bmd" }, 1.0));
		units.add(new CurrencySimpleSubUnit("Brunei Dollar", "Brunei Dollar", new String[] { "bnd" }, 1.250763));
		units.add(new CurrencySimpleSubUnit("Bolivian Boliviano", "Bolivian Boliviano", new String[] { "bob" }, 6.996242));
		units.add(new CurrencySimpleSubUnit("Brazilian Real", "Brazilian Real", new String[] { "brl" }, 2.04715));
		units.add(new CurrencySimpleSubUnit("Bahamian Dollar", "Bahamian Dollar", new String[] { "bsd" }, 1.0));
		units.add(new CurrencySimpleSubUnit("Bhutanese Ngultrum", "Bhutanese Ngultrum", new String[] { "btn" }, 51.575));
		units.add(new CurrencySimpleSubUnit("Botswanan Pula", "Botswanan Pula", new String[] { "bwp" }, 7.782313));
		units.add(new CurrencySimpleSubUnit("Belarusian Ruble", "Belarusian Ruble", new String[] { "byr" }, 8345.19));
		units.add(new CurrencySimpleSubUnit("Belize Dollar", "Belize Dollar", new String[] { "bzd" }, 1.904383));
		units.add(new CurrencySimpleSubUnit("Canadian Dollar", "Canadian Dollar", new String[] { "cad" }, 1.006752));
		units.add(new CurrencySimpleSubUnit("Congolese Franc", "Congolese Franc", new String[] { "cdf" }, 922.407654));
		units.add(new CurrencySimpleSubUnit("Swiss Franc", "Swiss Franc", new String[] { "chf" }, 0.986063));
		units.add(new CurrencySimpleSubUnit("Chilean Unit of Account (UF)", "Chilean Unit of Account (UF)", new String[] { "clf" }, 0.02147));
		units.add(new CurrencySimpleSubUnit("Chilean Peso", "Chilean Peso", new String[] { "clp" }, 485.130444));
		units.add(new CurrencySimpleSubUnit("Chinese Yuan", "Chinese Yuan", new String[] { "cny" }, 6.370605));
		units.add(new CurrencySimpleSubUnit("Colombian Peso", "Colombian Peso", new String[] { "cop" }, 1792.828573));
		units.add(new CurrencySimpleSubUnit("Costa Rican Colón", "Costa Rican Colón", new String[] { "crc" }, 501.043875));
		units.add(new CurrencySimpleSubUnit("Cuban Peso", "Cuban Peso", new String[] { "cup" }, 1.0));
		units.add(new CurrencySimpleSubUnit("Cape Verdean Escudo", "Cape Verdean Escudo", new String[] { "cve" }, 90.541867));
		units.add(new CurrencySimpleSubUnit("Czech Republic Koruna", "Czech Republic Koruna", new String[] { "czk" }, 20.790897));
		units.add(new CurrencySimpleSubUnit("Djiboutian Franc", "Djiboutian Franc", new String[] { "djf" }, 177.780001));
		units.add(new CurrencySimpleSubUnit("Danish Krone", "Danish Krone", new String[] { "dkk" }, 6.109377));
		units.add(new CurrencySimpleSubUnit("Dominican Peso", "Dominican Peso", new String[] { "dop" }, 39.213243));
		units.add(new CurrencySimpleSubUnit("Algerian Dinar", "Algerian Dinar", new String[] { "dzd" }, 82.468193));
		units.add(new CurrencySimpleSubUnit("Egyptian Pound", "Egyptian Pound", new String[] { "egp" }, 6.07754));
		units.add(new CurrencySimpleSubUnit("Ethiopian Birr", "Ethiopian Birr", new String[] { "etb" }, 17.986833));
		units.add(new CurrencySimpleSubUnit("Euro", "Euro", new String[] { "eur" }, 0.820915));
		units.add(new CurrencySimpleSubUnit("Fijian Dollar", "Fijian Dollar", new String[] { "fjd" }, 1.784378));
		units.add(new CurrencySimpleSubUnit("Falkland Islands Pound", "Falkland Islands Pound", new String[] { "fkp" }, 0.644456));
		units.add(new CurrencySimpleSubUnit("British Pound Sterling", "British Pound Sterling", new String[] { "gbp" }, 0.644456));
		units.add(new CurrencySimpleSubUnit("Georgian Lari", "Georgian Lari", new String[] { "gel" }, 1.727383));
		units.add(new CurrencySimpleSubUnit("Ghanaian Cedi", "Ghanaian Cedi", new String[] { "ghs" }, 1.96555));
		units.add(new CurrencySimpleSubUnit("Gibraltar Pound", "Gibraltar Pound", new String[] { "gip" }, 0.633795));
		units.add(new CurrencySimpleSubUnit("Gambian Dalasi", "Gambian Dalasi", new String[] { "gmd" }, 31.850034));
		units.add(new CurrencySimpleSubUnit("Guinean Franc", "Guinean Franc", new String[] { "gnf" }, 7190.719922));
		units.add(new CurrencySimpleSubUnit("Guatemalan Quetzal", "Guatemalan Quetzal", new String[] { "gtq" }, 7.85645));
		units.add(new CurrencySimpleSubUnit("Guyanaese Dollar", "Guyanaese Dollar", new String[] { "gyd" }, 202.949998));
		units.add(new CurrencySimpleSubUnit("Hong Kong Dollar", "Hong Kong Dollar", new String[] { "hkd" }, 7.754609));
		units.add(new CurrencySimpleSubUnit("Honduran Lempira", "Honduran Lempira", new String[] { "hnl" }, 19.169237));
		units.add(new CurrencySimpleSubUnit("Croatian Kuna", "Croatian Kuna", new String[] { "hrk" }, 6.177227));
		units.add(new CurrencySimpleSubUnit("Haitian Gourde", "Haitian Gourde", new String[] { "htg" }, 42.184266));
		units.add(new CurrencySimpleSubUnit("Hungarian Forint", "Hungarian Forint", new String[] { "huf" }, 230.573352));
		units.add(new CurrencySimpleSubUnit("Indonesian Rupiah", "Indonesian Rupiah", new String[] { "idr" }, 9458.165565));
		units.add(new CurrencySimpleSubUnit("Israeli New Sheqel", "Israeli New Sheqel", new String[] { "ils" }, 4.002788));
		units.add(new CurrencySimpleSubUnit("Indian Rupee", "Indian Rupee", new String[] { "inr" }, 55.751717));
		units.add(new CurrencySimpleSubUnit("Iraqi Dinar", "Iraqi Dinar", new String[] { "iqd" }, 1167.826667));
		units.add(new CurrencySimpleSubUnit("Iranian Rial", "Iranian Rial", new String[] { "irr" }, 12319.113333));
		units.add(new CurrencySimpleSubUnit("Icelandic Króna", "Icelandic Króna", new String[] { "isk" }, 121.126667));
		units.add(new CurrencySimpleSubUnit("Jamaican Dollar", "Jamaican Dollar", new String[] { "jmd" }, 89.226578));
		units.add(new CurrencySimpleSubUnit("Jordanian Dinar", "Jordanian Dinar", new String[] { "jod" }, 0.707915));
		units.add(new CurrencySimpleSubUnit("Japanese Yen", "Japanese Yen", new String[] { "jpy" }, 78.277906));
		units.add(new CurrencySimpleSubUnit("Kenyan Shilling", "Kenyan Shilling", new String[] { "kes" }, 84.340578));
		units.add(new CurrencySimpleSubUnit("Kyrgystani Som", "Kyrgystani Som", new String[] { "kgs" }, 45.129601));
		units.add(new CurrencySimpleSubUnit("Cambodian Riel", "Cambodian Riel", new String[] { "khr" }, 4095.79));
		units.add(new CurrencySimpleSubUnit("Comorian Franc", "Comorian Franc", new String[] { "kmf" }, 402.326335));
		units.add(new CurrencySimpleSubUnit("North Korean Won", "North Korean Won", new String[] { "kpw" }, 900.0));
		units.add(new CurrencySimpleSubUnit("South Korean Won", "South Korean Won", new String[] { "krw" }, 1131.370887));
		units.add(new CurrencySimpleSubUnit("Kuwaiti Dinar", "Kuwaiti Dinar", new String[] { "kwd" }, 0.281856));
		units.add(new CurrencySimpleSubUnit("Kazakhstani Tenge", "Kazakhstani Tenge", new String[] { "kzt" }, 150.53252));
		units.add(new CurrencySimpleSubUnit("Laotian Kip", "Laotian Kip", new String[] { "lak" }, 8047.016667));
		units.add(new CurrencySimpleSubUnit("Lebanese Pound", "Lebanese Pound", new String[] { "lbp" }, 1507.881025));
		units.add(new CurrencySimpleSubUnit("Sri Lankan Rupee", "Sri Lankan Rupee", new String[] { "lkr" }, 131.845387));
		units.add(new CurrencySimpleSubUnit("Liberian Dollar", "Liberian Dollar", new String[] { "lrd" }, 75.050002));
		units.add(new CurrencySimpleSubUnit("Lesotho Loti", "Lesotho Loti", new String[] { "lsl" }, 8.374167));
		units.add(new CurrencySimpleSubUnit("Lithuanian Litas", "Lithuanian Litas", new String[] { "ltl" }, 2.829851));
		units.add(new CurrencySimpleSubUnit("Latvian Lats", "Latvian Lats", new String[] { "lvl" }, 0.571672));
		units.add(new CurrencySimpleSubUnit("Libyan Dinar", "Libyan Dinar", new String[] { "lyd" }, 1.270367));
		units.add(new CurrencySimpleSubUnit("Moroccan Dirham", "Moroccan Dirham", new String[] { "mad" }, 9.014648));
		units.add(new CurrencySimpleSubUnit("Moldovan Leu", "Moldovan Leu", new String[] { "mdl" }, 12.572979));
		units.add(new CurrencySimpleSubUnit("Malagasy Ariary", "Malagasy Ariary", new String[] { "mga" }, 2271.996667));
		units.add(new CurrencySimpleSubUnit("Macedonian Denar", "Macedonian Denar", new String[] { "mkd" }, 50.047387));
		units.add(new CurrencySimpleSubUnit("Myanma Kyat", "Myanma Kyat", new String[] { "mmk" }, 879.08));
		units.add(new CurrencySimpleSubUnit("Mongolian Tugrik", "Mongolian Tugrik", new String[] { "mnt" }, 1388.479981));
		units.add(new CurrencySimpleSubUnit("Macanese Pataca", "Macanese Pataca", new String[] { "mop" }, 8.012333));
		units.add(new CurrencySimpleSubUnit("Mauritanian Ouguiya", "Mauritanian Ouguiya", new String[] { "mro" }, 301.353333));
		units.add(new CurrencySimpleSubUnit("Mauritian Rupee", "Mauritian Rupee", new String[] { "mur" }, 31.063116));
		units.add(new CurrencySimpleSubUnit("Maldivian Rufiyaa", "Maldivian Rufiyaa", new String[] { "mvr" }, 15.400033));
		units.add(new CurrencySimpleSubUnit("Malawian Kwacha", "Malawian Kwacha", new String[] { "mwk" }, 275.594996));
		units.add(new CurrencySimpleSubUnit("Mexican Peso", "Mexican Peso", new String[] { "mxn" }, 13.389863));
		units.add(new CurrencySimpleSubUnit("Malaysian Ringgit", "Malaysian Ringgit", new String[] { "myr" }, 3.1221));
		units.add(new CurrencySimpleSubUnit("Mozambican Metical", "Mozambican Metical", new String[] { "mzn" }, 28.359851));
		units.add(new CurrencySimpleSubUnit("Namibian Dollar", "Namibian Dollar", new String[] { "nad" }, 8.364054));
		units.add(new CurrencySimpleSubUnit("Nigerian Naira", "Nigerian Naira", new String[] { "ngn" }, 161.597323));
		units.add(new CurrencySimpleSubUnit("Nicaraguan Córdoba", "Nicaraguan Córdoba", new String[] { "nio" }, 23.697001));
		units.add(new CurrencySimpleSubUnit("Norwegian Krone", "Norwegian Krone", new String[] { "nok" }, 6.050022));
		units.add(new CurrencySimpleSubUnit("Nepalese Rupee", "Nepalese Rupee", new String[] { "npr" }, 89.502263));
		units.add(new CurrencySimpleSubUnit("New Zealand Dollar", "New Zealand Dollar", new String[] { "nzd" }, 1.237363));
		units.add(new CurrencySimpleSubUnit("Omani Rial", "Omani Rial", new String[] { "omr" }, 0.384973));
		units.add(new CurrencySimpleSubUnit("Panamanian Balboa", "Panamanian Balboa", new String[] { "pab" }, 1.0));
		units.add(new CurrencySimpleSubUnit("Peruvian Nuevo Sol", "Peruvian Nuevo Sol", new String[] { "pen" }, 2.632876));
		units.add(new CurrencySimpleSubUnit("Papua New Guinean Kina", "Papua New Guinean Kina", new String[] { "pgk" }, 2.06604));
		units.add(new CurrencySimpleSubUnit("Philippine Peso", "Philippine Peso", new String[] { "php" }, 41.840483));
		units.add(new CurrencySimpleSubUnit("Pakistani Rupee", "Pakistani Rupee", new String[] { "pkr" }, 94.795317));
		units.add(new CurrencySimpleSubUnit("Polish Zloty", "Polish Zloty", new String[] { "pln" }, 3.379026));
		units.add(new CurrencySimpleSubUnit("Paraguayan Guarani", "Paraguayan Guarani", new String[] { "pyg" }, 4442.62111));
		units.add(new CurrencySimpleSubUnit("Qatari Rial", "Qatari Rial", new String[] { "qar" }, 3.641162));
		units.add(new CurrencySimpleSubUnit("Romanian Leu", "Romanian Leu", new String[] { "ron" }, 3.794625));
		units.add(new CurrencySimpleSubUnit("Serbian Dinar", "Serbian Dinar", new String[] { "rsd" }, 97.212949));
		units.add(new CurrencySimpleSubUnit("Russian Ruble", "Russian Ruble", new String[] { "rub" }, 32.557078));
		units.add(new CurrencySimpleSubUnit("Rwandan Franc", "Rwandan Franc", new String[] { "rwf" }, 614.514208));
		units.add(new CurrencySimpleSubUnit("Saudi Riyal", "Saudi Riyal", new String[] { "sar" }, 3.750264));
		units.add(new CurrencySimpleSubUnit("Solomon Islands Dollar", "Solomon Islands Dollar", new String[] { "sbd" }, 7.065961));
		units.add(new CurrencySimpleSubUnit("Seychellois Rupee", "Seychellois Rupee", new String[] { "scr" }, 14.047883));
		units.add(new CurrencySimpleSubUnit("Sudanese Pound", "Sudanese Pound", new String[] { "sdg" }, 4.41275));
		units.add(new CurrencySimpleSubUnit("Swedish Krona", "Swedish Krona", new String[] { "sek" }, 6.810557));
		units.add(new CurrencySimpleSubUnit("Singapore Dollar", "Singapore Dollar", new String[] { "sgd" }, 1.249638));
		units.add(new CurrencySimpleSubUnit("Saint Helena Pound", "Saint Helena Pound", new String[] { "shp" }, 0.644456));
		units.add(new CurrencySimpleSubUnit("Sierra Leonean Leone", "Sierra Leonean Leone", new String[] { "sll" }, 4337.66811));
		units.add(new CurrencySimpleSubUnit("Somali Shilling", "Somali Shilling", new String[] { "sos" }, 1627.733333));
		units.add(new CurrencySimpleSubUnit("Surinamese Dollar", "Surinamese Dollar", new String[] { "srd" }, 3.275655));
		units.add(new CurrencySimpleSubUnit("São Tomé and Príncipe Dobra", "São Tomé and Príncipe Dobra", new String[] { "std" }, 20281.6));
		units.add(new CurrencySimpleSubUnit("Salvadoran Colón", "Salvadoran Colón", new String[] { "svc" }, 8.768795));
		units.add(new CurrencySimpleSubUnit("Syrian Pound", "Syrian Pound", new String[] { "syp" }, 65.675133));
		units.add(new CurrencySimpleSubUnit("Swazi Lilangeni", "Swazi Lilangeni", new String[] { "szl" }, 8.376567));
		units.add(new CurrencySimpleSubUnit("Thai Baht", "Thai Baht", new String[] { "thb" }, 31.607806));
		units.add(new CurrencySimpleSubUnit("Tajikistani Somoni", "Tajikistani Somoni", new String[] { "tjs" }, 4.58185));
		units.add(new CurrencySimpleSubUnit("Turkmenistani Manat", "Turkmenistani Manat", new String[] { "tmt" }, 2.85107));
		units.add(new CurrencySimpleSubUnit("Tunisian Dinar", "Tunisian Dinar", new String[] { "tnd" }, 1.627775));
		units.add(new CurrencySimpleSubUnit("Tongan Pa'anga", "Tongan Pa'anga", new String[] { "top" }, 1.743812));
		units.add(new CurrencySimpleSubUnit("Turkish Lira", "Turkish Lira", new String[] { "try" }, 1.803212));
		units.add(new CurrencySimpleSubUnit("Trinidad and Tobago Dollar", "Trinidad and Tobago Dollar", new String[] { "ttd" }, 6.41794));
		units.add(new CurrencySimpleSubUnit("New Taiwan Dollar", "New Taiwan Dollar", new String[] { "twd" }, 30.044001));
		units.add(new CurrencySimpleSubUnit("Tanzanian Shilling", "Tanzanian Shilling", new String[] { "tzs" }, 1581.746768));
		units.add(new CurrencySimpleSubUnit("Ukrainian Hryvnia", "Ukrainian Hryvnia", new String[] { "uah" }, 8.132105));
		units.add(new CurrencySimpleSubUnit("Ugandan Shilling", "Ugandan Shilling", new String[] { "ugx" }, 2496.970548));
		units.add(new CurrencySimpleSubUnit("United States Dollar", "United States Dollar", new String[] { "usd" }, 1.0));
		units.add(new CurrencySimpleSubUnit("Uruguayan Peso", "Uruguayan Peso", new String[] { "uyu" }, 21.424971));
		units.add(new CurrencySimpleSubUnit("Uzbekistan Som", "Uzbekistan Som", new String[] { "uzs" }, 1909.335481));
		units.add(new CurrencySimpleSubUnit("Venezuelan Bolívar", "Venezuelan Bolívar", new String[] { "vef" }, 4.294698));
		units.add(new CurrencySimpleSubUnit("Vietnamese Dong", "Vietnamese Dong", new String[] { "vnd" }, 20900.593325));
		units.add(new CurrencySimpleSubUnit("Vanuatu Vatu", "Vanuatu Vatu", new String[] { "vuv" }, 92.650002));
		units.add(new CurrencySimpleSubUnit("Samoan Tala", "Samoan Tala", new String[] { "wst" }, 2.312448));
		units.add(new CurrencySimpleSubUnit("CFA Franc BEAC", "CFA Franc BEAC", new String[] { "xaf" }, 537.805099));
		units.add(new CurrencySimpleSubUnit("East Caribbean Dollar", "East Caribbean Dollar", new String[] { "xcd" }, 2.70165));
		units.add(new CurrencySimpleSubUnit("Special Drawing Rights", "Special Drawing Rights", new String[] { "xdr" }, 0.663902));
		units.add(new CurrencySimpleSubUnit("CFA Franc BCEAO", "CFA Franc BCEAO", new String[] { "xof" }, 537.089992));
		units.add(new CurrencySimpleSubUnit("CFP Franc", "CFP Franc", new String[] { "xpf" }, 97.721466));
		units.add(new CurrencySimpleSubUnit("Yemeni Rial", "Yemeni Rial", new String[] { "yer" }, 215.10556));
		units.add(new CurrencySimpleSubUnit("South African Rand", "South African Rand", new String[] { "zar" }, 8.36734));
		units.add(new CurrencySimpleSubUnit("Zambian Kwacha", "Zambian Kwacha", new String[] { "zmk" }, 4948.259544));
		units.add(new CurrencySimpleSubUnit("Zimbabwean Dollar", "Zimbabwean Dollar", new String[] { "zwl" }, 322.355011));
	}

	public Date getLastUpdated()
	{
		return lastUpdated;
	}

	private void processJson(String json)
	{
		Matcher matcher = patternJson.matcher(json);

		while (matcher.find())
		{
			if (matcher.group(1).equals("timestamp"))
			{
				lastUpdated = new Date(Long.parseLong(matcher.group(2)) * 1000);
			}
			else
			{
				double rate = Double.parseDouble(matcher.group(2));
				String currency = matcher.group(1);
				
				updateCurrencyUnit(currency, rate);
			}
		}
	}
	
	private void processXML(Document doc) throws DOMException, ParseException,
			CloneNotSupportedException
	{
		doc.getDocumentElement().normalize();

		NodeList elements = doc.getElementsByTagName("Cube");

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
				
				updateCurrencyUnit(currency, rate);
			}
		}
		
		updateCurrencyUnit("eur", 1.0);
	}

	@Override
	public String toString()
	{
		return "currency";
	}

	public void update()
	{
		try
		{
			String json = downloadJson(primaryUrl);
			processJson(json);
		}
		catch (IOException e)
		{
			try
			{	
				Document doc = downloadXML(secondaryUrl);
				processXML(doc);
			}
			catch (ParserConfigurationException | SAXException | IOException | ParseException
					| DOMException | CloneNotSupportedException exc)
			{
				throw new RuntimeException("Unable to update currencies", exc);
			}
		}
	}
	
	private void updateCurrencyUnit(String currency, double rate)
	{
		for (SubUnit unit : units)
		{
			if(unit.isMatch(currency))
			{
				((CurrencySimpleSubUnit) unit).setConversion(rate);
			}
		}
	}
}
