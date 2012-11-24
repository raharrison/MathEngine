package uk.co.raharrison.mathengine.unitconversion.units.timezones;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

class CityTimeZoneProvider
{
	private String archivePath;

	private static final String ZONEFILE = "zone.csv";
	private static final String TIMEZONEFILE = "timezone.csv";

	private HashMap<Integer, TimeZone> timezones;

	public CityTimeZoneProvider(String archivePath)
	{
		this.archivePath = archivePath;
		timezones = new HashMap<>();
	}

	private long getUnixNow()
	{
		return System.currentTimeMillis() / 1000L;
	}

	private void parseTimezones(BufferedReader reader)
	{
		try
		{
			long current = getUnixNow();

			int currentIndex = 1;
			String match = "";
			String line = "";
			boolean set = false;

			while ((line = reader.readLine()) != null)
			{
				line = line.replace("\"", "").replace("_", " ");
				String[] split = line.split(",");
				int index = Integer.parseInt(split[0]);

				if(index == currentIndex + 1 && !set)
				{
					String[] tokens = match.split(",");
					TimeZone zone = timezones.get(Integer.parseInt(tokens[0]));
					zone.setOffset(Integer.parseInt(tokens[3]));
					match = "";
					currentIndex++;
					set = true;
				}
				if (index == currentIndex)
				{
					long start = Long.parseLong(split[2]);
					if (start > current && !match.equals(""))
					{
						String[] tokens = match.split(",");
						TimeZone zone = timezones.get(Integer.parseInt(tokens[0]));
						zone.setOffset(Integer.parseInt(tokens[3]));
						match = "";
						currentIndex++;
						set = true;
					}
					else
					{
						match = line;
						set = false;
					}
				}
				else
					set = false;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				reader.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	private void parseZones(BufferedReader reader)
	{
		try
		{
			String line = "";

			while ((line = reader.readLine()) != null)
			{
				String[] tokens = line.replace("\"", "").split(",");
				String zone = tokens[2].replace("_", " ").trim();
				String city = zone.substring(zone.lastIndexOf("/") + 1).trim();

				String index = tokens[0];
				timezones.put(Integer.parseInt(index), new TimeZone(city));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			try
			{
				reader.close();
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}

	public List<TimeZone> process() throws IOException
	{
		ZipFile zipFile = null;
		try
		{
			zipFile = new ZipFile(this.archivePath);
			parseZones(new BufferedReader(new InputStreamReader(zipFile.getInputStream(new ZipEntry(ZONEFILE)))));
			parseTimezones(new BufferedReader(new InputStreamReader(zipFile.getInputStream(new ZipEntry(TIMEZONEFILE)))));
			
			ArrayList<TimeZone> results = new ArrayList<>(timezones.values());
			
			return results;
		}
		finally
		{
			if(zipFile != null)
				try
				{
					zipFile.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
		}
	}
}

class TimeZone
{
	private String city;
	private int offset;

	public TimeZone(String city)
	{
		this(city, 0);
	}

	public TimeZone(String city, int offset)
	{
		this.city = city;
		this.offset = offset;
	}

	public String getCity()
	{
		return city;
	}
	
	public int getOffset()
	{
		return offset;
	}

	public void setOffset(int offset)
	{
		this.offset = offset;
	}
}
