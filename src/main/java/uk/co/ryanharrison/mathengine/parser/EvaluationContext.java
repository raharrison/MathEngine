package uk.co.ryanharrison.mathengine.parser;

public class EvaluationContext {

    private AngleUnit angleUnit;

    EvaluationContext(AngleUnit angleUnit) {
        this.angleUnit = angleUnit;
    }

    public AngleUnit getAngleUnit() {
        return angleUnit;
    }

    public void setAngleUnit(AngleUnit angleUnit) {
        this.angleUnit = angleUnit;
    }

    public static EvaluationContext defaultContext() {
        return new EvaluationContext(AngleUnit.Radians);
    }

}
