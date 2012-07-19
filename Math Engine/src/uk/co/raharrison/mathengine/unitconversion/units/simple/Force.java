package uk.co.raharrison.mathengine.unitconversion.units.simple;

public class Force extends SimpleUnitGroup
{
	@Override
	protected void fillUnits()
	{
		units.add(new SimpleSubUnit("atomic unit of force", "atomic units of force", new String[] { }, 8.23872206E-8));
		units.add(new SimpleSubUnit("dyne", "dynes", new String[] { "dyn", "dyns" }, 10E-5));
		units.add(new SimpleSubUnit("kilogram force", "kilograms force", new String[] { "kilopond", "kiloponds", "kp", "kps", "kgf", "kgfs" }, 9.80665));
		units.add(new SimpleSubUnit("megapond force", "megaponds force", new String[] { "mpnf", "mpnfs" }, 9806.65));
		units.add(new SimpleSubUnit("gram force", "grams force", new String[] { "gf", "gfs", "pond", "ponds", "pond force", "ponds force", "pnf", "pnfs" }, 0.00980665));
		units.add(new SimpleSubUnit("decigram force", "decigrams force", new String[] { "dgf", "dgfs" }, 0.000980665));
		units.add(new SimpleSubUnit("dekagram force", "dekagrams force", new String[] { "dkgf", "dkgfs" }, 0.0980665));
		units.add(new SimpleSubUnit("kip", "kips", new String[] { "kip force", "kips force", "kipf", "kipfs", "klbf", "klbfs" }, 4.4482216152605E3));
		units.add(new SimpleSubUnit("ounce force", "ounces force", new String[] { "ozf", "ozfs" }, 0.2780138509537812));
		units.add(new SimpleSubUnit("pound force", "pounds force", new String[] { "lbf", "lbfs" }, 4.4482216152605));
		units.add(new SimpleSubUnit("poundal", "poundals", new String[] { "pdl", "pdls" }, 0.138254954376));
		units.add(new SimpleSubUnit("sthene", "sthenes", new String[] { "sn", "sns" }, 1.0E3));
		
		units.add(new SimpleSubUnit("ton force (short)", "tons force (short)", new String[] { "ton (us) force", "tons (us) force", "short ton force", "short tons force", "stf", "stfs" }, 8.896443230521E3));
		units.add(new SimpleSubUnit("ton force (long)", "tons force (long)", new String[] { "ton (uk) force", "tons (uk) force", "long ton force", "long tons force", "ltf", "ltfs" }, 9964.016384));
		units.add(new SimpleSubUnit("tonne force", "tonnes force", new String[] { "metric ton force", "metric tons force", "tnf", "tnfs" }, 9806.65));
		
		units.add(new SimpleSubUnit("newton", "newtons", new String[] { "n", "ns", "joule/metre", "joule/metres", "joule/meter", "joule/meters","joule metre", "joule metres", "joule meter", "joule meters" }, 1.0));
		units.add(new SimpleSubUnit("attonewton", "attonewtons", new String[] { "atn", "atns" }, 1.0E-18));
		units.add(new SimpleSubUnit("centinewton", "centinewtons", new String[] { "cn", "cns" }, 0.01));
		units.add(new SimpleSubUnit("decinewton", "decinewtons", new String[] { "dn", "dns" }, 0.1));
		units.add(new SimpleSubUnit("dekanewton", "dekanewtons", new String[] { "dkn", "dkns" }, 10));
		units.add(new SimpleSubUnit("dekanewton", "dekanewtons", new String[] { "dkn", "dkns" }, 10));
		units.add(new SimpleSubUnit("exanewton", "exanewtons", new String[] { "exn", "exns" }, 1000000000000000000.0));
		units.add(new SimpleSubUnit("femtonewton", "femtonewtons", new String[] { "fmtn", "fmtns" }, 1.0E-15));
		units.add(new SimpleSubUnit("giganewton", "giganewtons", new String[] { "gn", "gns" }, 1000000000));
		units.add(new SimpleSubUnit("hectonewton", "hectonewtons", new String[] { "hn", "hns" }, 100));
		units.add(new SimpleSubUnit("kilonewton", "kilonewtons", new String[] { "kn", "kns" }, 1000));
		units.add(new SimpleSubUnit("meganewton", "meganewtons", new String[] { "mgn", "mgns" }, 1000000));
		units.add(new SimpleSubUnit("micronewton", "micronewton", new String[] { "mcn", "mcns" }, 0.000001));
		units.add(new SimpleSubUnit("millinewton", "millinewtons", new String[] { "mn", "mns" }, 0.001));
		units.add(new SimpleSubUnit("nanonewton", "nanonewtons", new String[] { "nn", "nns" }, 1.0E-9));
		units.add(new SimpleSubUnit("petanewton", "petanewtons", new String[] { "pn", "pns" }, 1000000000000000.0));
		units.add(new SimpleSubUnit("piconewton", "piconewtons", new String[] { "pcn", "pcns" }, 1.0E-12));
		units.add(new SimpleSubUnit("teranewton", "teranewtons", new String[] { "tn", "tns" }, 1000000000000.0));
		units.add(new SimpleSubUnit("yoctonewton", "yoctonewton", new String[] { "ycn", "ycns" }, 1.0E-24));
		units.add(new SimpleSubUnit("yottanewton", "yottanewtons", new String[] { "ytn", "ytns" }, 1.0E24));
		units.add(new SimpleSubUnit("zeptonewton", "zeptonewtons", new String[] { "zpn", "zpns" }, 1.0E-21));
		units.add(new SimpleSubUnit("zettanewton", "zettanewtons", new String[] { "ztn", "ztns" }, 1.0E21));
	}

	@Override
	public String toString()
	{
		return "force";
	}
}
