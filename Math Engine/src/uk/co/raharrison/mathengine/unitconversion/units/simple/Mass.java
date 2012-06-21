package uk.co.raharrison.mathengine.unitconversion.units.simple;

public class Mass extends SimpleUnitGroup
{
	@Override
	protected void fillUnits()
	{
		units.add(new SimpleSubUnit("atomic mass unit", "atomic mass units", new String[] { "u",
				"amu", "amus" }, 1.66053873e-27));
		units.add(new SimpleSubUnit("atomic unit of mass", "atomic units of mass", new String[] {
				"me", "aum", "aums" }, 9.10938215E-31));
		units.add(new SimpleSubUnit("bag (coffee)", "bags (coffee)", new String[] { "bag", "bags",
				"bg", "bgs" }, 60));
		units.add(new SimpleSubUnit("bag (Portland cement)", "bags (Portland cement)",
				new String[] { "pbag", "pbags", "pbg", "pbgs" }, 42.63768278));
		units.add(new SimpleSubUnit("bale", "bales", new String[] { "bl", "bls" }, 217.7243376));
		units.add(new SimpleSubUnit("barge", "barges", new String[] { "brg", "brgs", "barg",
				"bargs" }, 20411.65665));
		units.add(new SimpleSubUnit("carat", "carats", new String[] { "kt", "kts" },
				0.000205196548333));
		units.add(new SimpleSubUnit("carat (metric)", "carats (metric)",
				new String[] { "ct", "cts" }, 0.0002));
		units.add(new SimpleSubUnit("centigram", "centigrams", new String[] { "cg", "cgs" },
				0.00001));
		units.add(new SimpleSubUnit("clove", "cloves", new String[] { "clv", "clvs" }, 3.62873896));
		units.add(new SimpleSubUnit("crith", "criths", new String[] { "crth", "crths" },
				0.0000899349));
		units.add(new SimpleSubUnit("dalton", "daltons", new String[] { "dltn", "dltns", "da",
				"das" }, 1.66090210E-27));
		units.add(new SimpleSubUnit("decigram", "decigrams", new String[] { "dg", "dgs" }, 0.0001));
		units.add(new SimpleSubUnit("decitonne", "decitonnes", new String[] { "dt", "dts" }, 100.0));
		units.add(new SimpleSubUnit("dekagram", "dekagrams", new String[] { "dkg", "dkgs" }, 0.01));
		units.add(new SimpleSubUnit("dekatonne", "dekatonne", new String[] { "dkt", "dkts" },
				10000.0));
		units.add(new SimpleSubUnit("dram (troy)", "drams (troy)", new String[] { "dr", "drs",
				"dram", "drams" }, 0.0038879346));
		units.add(new SimpleSubUnit("dram (avoirdupois)", "drams (avoirdupois)", new String[] {
				"dr av", "dravs", "dram av", "drams av" }, 0.0017718452));
		units.add(new SimpleSubUnit("electronvolt", "electronvolts", new String[] { "ev", "evs",
				"elecvolt", "elecvolts" }, 1.782662e-36));
		units.add(new SimpleSubUnit("femtogram", "femtograms", new String[] { "fg", "fgs" },
				1.0E-18));
		units.add(new SimpleSubUnit("gamma", "gammas", new String[] { "gma", "gma" }, 1.0e-9));
		units.add(new SimpleSubUnit("gigagram", "gigagrams", new String[] { "ggg", "gggs" },
				1000000));
		units.add(new SimpleSubUnit("gigatonne", "giggatonnes", new String[] { "ggt", "ggts" },
				1000000000000.0));
		units.add(new SimpleSubUnit("grain", "grains", new String[] { "gr", "grs" }, 6.479891E-5));
		units.add(new SimpleSubUnit("gram", "grams", new String[] { "g", "gs" }, 0.001));
		units.add(new SimpleSubUnit("grave", "graves", new String[] { "grv", "grvs" }, 1));
		units.add(new SimpleSubUnit("hectogram", "hectograms", new String[] { "hg", "hgs" }, 0.1));
		units.add(new SimpleSubUnit("hundredweight (long)", "hundredweights (long)", new String[] {
				"cwt", "cwt", "hundredweight", "hundredweights" }, 50.80234544));
		units.add(new SimpleSubUnit("hundredweight (short)", "hundredweights (short)",
				new String[] { "sh cwt", "sh cwt" }, 45.359237));
		units.add(new SimpleSubUnit("kilogram", "kilograms", new String[] { "kg", "kgs", "kilo",
				"kilos" }, 1));
		units.add(new SimpleSubUnit("kiloton (short)", "kilotons (short)", new String[] {
				"short kiloton", "short kilotons", "sh ktn", "sh ktns" }, 907184.74));
		units.add(new SimpleSubUnit("kiloton (long)", "kilotons (long)", new String[] {
				"long kiloton", "long kilotons", "lng ktn", "lng ktns", "kiloton", "kilotons" },
				1016046.9088));
		units.add(new SimpleSubUnit("kilotonne", "kilotonnes", new String[] { "kt", "kts" },
				1000000));
		units.add(new SimpleSubUnit("kip", "kips", new String[] { "kp", "kps" }, 453.59237));
		units.add(new SimpleSubUnit("mark", "marks", new String[] { "mrk", "mrks" }, 0.248827814));
		units.add(new SimpleSubUnit("megagram", "megagrams", new String[] { "mgag", "mgags" },
				1000.0));
		units.add(new SimpleSubUnit("megatonne", "megatonnes", new String[] { "mt", "mts" },
				1000000000.0));
		units.add(new SimpleSubUnit("microgram", "micrograms", new String[] { "mug", "mugs" },
				1.0e-9));
		units.add(new SimpleSubUnit("millidalton", "millidaltons", new String[] { "mda", "mdas" },
				1.6605402e-30));
		units.add(new SimpleSubUnit("milligram", "milligrams", new String[] { "mg", "mgs" },
				0.000001));
		units.add(new SimpleSubUnit("mite", "mites", new String[] { "mte", "mtes" }, 3.2399455E-6));
		units.add(new SimpleSubUnit("mite (metric)", "mites (metric)", new String[] { "mtem",
				"mtems" }, 5.0E-5));
		units.add(new SimpleSubUnit("nanogram", "nanograms", new String[] { "ng", "ngs" }, 1.0E-12));
		units.add(new SimpleSubUnit("ounce", "ounces", new String[] { "oz", "ozs" }, 0.028349523125));
		units.add(new SimpleSubUnit("ounce (troy)", "ounces (troy)",
				new String[] { "oz t", "ozs t" }, 0.0311034768));
		units.add(new SimpleSubUnit("pennyweight", "pennyweights", new String[] { "dwt", "dwts",
				"pwt", "pwts" }, 0.00155517384));
		units.add(new SimpleSubUnit("petagram", "petagrams", new String[] { "pg", "pgs" },
				1000000000000.0));
		units.add(new SimpleSubUnit("picogram", "picogram", new String[] { "pcg", "pcgs" }, 1.0e-15));
		units.add(new SimpleSubUnit("point", "points", new String[] { "pt", "pts" }, 0.000002));
		units.add(new SimpleSubUnit("pound", "pounds", new String[] { "lb", "lbs" }, 0.45359237));
		units.add(new SimpleSubUnit("pound (metric)", "pounds (metric)", new String[] { "lb m",
				"lbs m" }, 0.5));
		units.add(new SimpleSubUnit("pound (troy)", "pounds (troy)",
				new String[] { "lb t", "lbs t" }, 0.3732417216));
		units.add(new SimpleSubUnit("quarter", "quarters", new String[] { "qrt", "qrts" },
				12.70058636));
		units.add(new SimpleSubUnit("quarter (ton)", "quarters (ton)", new String[] { "qrt t",
				"qrt t" }, 226.796185));
		units.add(new SimpleSubUnit("quintal", "quintals", new String[] { "q", "q" }, 100));
		units.add(new SimpleSubUnit("scruple", "scruples", new String[] { "sc", "scs" },
				0.0012959782));
		units.add(new SimpleSubUnit("sheet", "sheet", new String[] { "sht", "shts" }, 0.0006479891));
		units.add(new SimpleSubUnit("slug", "slugs", new String[] { "slg", "slgs" }, 14.593903));
		units.add(new SimpleSubUnit("stone", "stone", new String[] { "st", "sts" }, 6.35029318));
		units.add(new SimpleSubUnit("teragram", "teragram", new String[] { "tg", "tgs" },
				1000000000));
		units.add(new SimpleSubUnit("ton (short)", "tons (short)", new String[] { "short ton",
				"short tons", "sh tn", "sh tns" }, 907.18474));
		units.add(new SimpleSubUnit("ton (long)", "tons (long)", new String[] { "long ton",
				"long tons", "lng tn", "lng tns", "ton", "tons" }, 1016.0469088));
		units.add(new SimpleSubUnit("tonne", "tonnes", new String[] { "t", "ts", "metric ton",
				"metric tons" }, 1000.0));
		units.add(new SimpleSubUnit("wey", "weys", new String[] { "wy", "wys" }, 114.30527724));
		units.add(new SimpleSubUnit("yoctogram", "yoctograms", new String[] { "ycg", "ycgs" },
				1.0e-27));
		units.add(new SimpleSubUnit("yottagram", "yottagrams", new String[] { "ytg", "ytgs" },
				1.0e21));
		units.add(new SimpleSubUnit("zentner", "zentners", new String[] { "ztr", "ztrs" }, 50));
		units.add(new SimpleSubUnit("zeptogram", "zeptograms", new String[] { "zpg", "zpgs" },
				1.0e-24));
		units.add(new SimpleSubUnit("zettagram", "zettagrams", new String[] { "ztg", "ztgs" },
				1000000000000000000.0));

	}

	@Override
	public String toString()
	{
		return "mass";
	}
}
