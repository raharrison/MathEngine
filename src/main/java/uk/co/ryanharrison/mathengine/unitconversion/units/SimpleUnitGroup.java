package uk.co.ryanharrison.mathengine.unitconversion.units;

import uk.co.ryanharrison.mathengine.BigRational;

public class SimpleUnitGroup extends UnitGroup {

    @Override
    protected BigRational doConversion(Conversion params) {
        SimpleSubUnit from, to;

        if ((from = (SimpleSubUnit) params.getFrom()) != null
                && (to = (SimpleSubUnit) params.getTo()) != null) {
            return params.getValue().divide(to.getConversion()).multiply(from.getConversion());
        }

        return null;
    }
}
