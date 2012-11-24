package uk.co.raharrison.mathengine.unitconversion.units.currency;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class JsonCurrencyProvider extends CurrencyProvider
{
	private String json;
	private static final Pattern patternJson = Pattern.compile("\"([A-Z]+)\":\\s(\\d+\\.?\\d*)",
			Pattern.CASE_INSENSITIVE);

	JsonCurrencyProvider(String url)
	{
		super(url);
	}

	@Override
	void download() throws IOException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new URL(getUrl()).openStream()));

		String in = "";
		StringBuilder builder = new StringBuilder();

		while ((in = reader.readLine()) != null)
		{
			builder.append("\n" + in);
		}

		reader.close();

		this.json = builder.toString();
	}

	@Override
	List<Unit> process()
	{
		if(json == null)
			return null;
		
		Matcher matcher = patternJson.matcher(json);
		ArrayList<Unit> results = new ArrayList<Unit>();
		
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

				results.add(new Unit(currency, rate));
			}
		}
		return results;
	}

}
