package uk.co.ryanharrison.mathengine.unitconversion.units;

import uk.co.ryanharrison.mathengine.core.BigRational;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class UnitGroup {

    protected String name;
    protected List<SubUnit> units;

    public UnitGroup() {
        name = null;
        units = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addSubUnit(SubUnit subUnit) {
        units.add(subUnit);
    }

    public Conversion convert(BigRational amount, String from, String to) {
        Conversion params = getConversionParams(from, to);
        params.setValue(amount);
        params.setResult(doConversion(params));
        params.setUnitGroup(this);

        return params;
    }

    protected abstract BigRational doConversion(Conversion params);

    public Conversion getConversionParams(String from, String to) {
        Conversion params = new Conversion();

        for (SubUnit unit : units) {
            if (unit.isMatch(from))
                params.setFrom(unit);

            if (unit.isMatch(to))
                params.setTo(unit);
        }

        return params;
    }

    public Set<String> getUnits() {
        Set<String> results = new HashSet<String>();

        for (SubUnit unit : units) {
            results.add(unit.getPlural());
        }

        return results;
    }

    public Set<String> getAllAliases() {
        Set<String> results = new HashSet<String>();

        for (SubUnit unit : units) {
            results.addAll(unit.getUniqueAliases());
        }

        return results;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    public void update() {

    }
}
