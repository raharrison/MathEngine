package uk.co.ryanharrison.mathengine.parser.nodes;

import uk.co.ryanharrison.mathengine.unitconversion.units.Unit;

import java.util.Objects;

public class NodeUnit extends NodeDouble {

    private final Node value;
    private final Unit unit;
    private final boolean hasValue;

    public NodeUnit(Unit unit, Node val) {
        this(unit, val, true);
    }

    private NodeUnit(Unit unit, Node val, boolean hasValue) {
        super(1.0);
        this.unit = unit;
        this.value = val;
        this.hasValue = hasValue;
    }

    public Unit getUnit() {
        return unit;
    }

    public Node getValue() {
        return value;
    }

    @Override
    public double doubleValue() {
        return value.getTransformer().toNodeNumber().doubleValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        NodeUnit nodeUnit = (NodeUnit) o;
        return hasValue == nodeUnit.hasValue &&
                Objects.equals(value, nodeUnit.value) &&
                Objects.equals(unit, nodeUnit.unit);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), value, unit, hasValue);
    }

    @Override
    public NodeDouble copy() {
        return new NodeUnit(unit, value);
    }

    @Override
    public NodeTransformer createTransformer() {
        return new NodeUnitTransformer();
    }

    private class NodeUnitTransformer extends DefaultNodeTransformer {
        @Override
        public NodeNumber toNodeNumber() {
            return value.getTransformer().toNodeNumber();
        }
    }

    @Override
    public String toString() {
        if (hasValue) {
            if (doubleValue() == 1)
                return getUnit().getSingular();

            if (hasValue)
                return doubleValue() + " " + getUnit().getPlural();
        }

        return getUnit().getPlural();
    }
}
