package uk.co.ryanharrison.mathengine.parser.operators.unary;

import uk.co.ryanharrison.mathengine.parser.AngleUnit;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeConstant;
import uk.co.ryanharrison.mathengine.parser.nodes.NodeNumber;

import java.util.function.Function;

public abstract class TrigOperator extends NumberOperator {

    protected AngleUnit unit;

    private double degToRad(double radians) {
        return Math.PI * radians / 180.0;
    }

    public abstract NodeConstant getResult(NodeNumber num, AngleUnit unit);

    @Override
    protected Function<NodeNumber, NodeConstant> getFunc() {
        return num -> getResult(num, unit);
    }

    private double gradToRad(double radians) {
        return radians * (Math.PI / 200);
    }

    protected double radiansTo(double radians, AngleUnit angleUnits) {
        switch (angleUnits) {
            case Degrees:
                return degToRad(radians);
            case Gradians:
                return gradToRad(radians);
            default:
                return radians;
        }
    }

    public void setAngleUnit(AngleUnit units) {
        this.unit = units;
    }
}
