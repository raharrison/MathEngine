package uk.co.ryanharrison.mathengine.parser.nodes;

import uk.co.ryanharrison.mathengine.linearalgebra.Matrix;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.Function;

public final class NodeMatrix extends NodeConstant {

    private Node[][] values;

    public NodeMatrix(Node[][] values) {
        this.values = values;
    }

    public NodeMatrix(uk.co.ryanharrison.mathengine.linearalgebra.Matrix matrix) {
        values = new Node[matrix.getRowCount()][matrix.getColumnCount()];

        for (int i = 0; i < matrix.getRowCount(); i++) {
            for (int j = 0; j < matrix.getColumnCount(); j++) {
                values[i][j] = new NodeDouble(matrix.get(i, j));
            }
        }
    }

    @Override
    public NodeConstant applyUniFunc(Function<NodeNumber, NodeConstant> func) {
        NodeConstant[][] results = new NodeConstant[rowCount()][colCount()];

        for (int i = 0; i < rowCount(); i++) {
            for (int j = 0; j < colCount(); j++) {
                if (values[i][j] instanceof NodeNumber) {
                    results[i][j] = func.apply(values[i][j].getTransformer().toNodeNumber());
                } else {
                    results[i][j] = ((NodeConstant) values[i][j]).applyUniFunc(func);
                }
            }
        }
        return new NodeMatrix(results);
    }

    @Override
    public int compareTo(NodeConstant cons) {
        double sum = getTransformer().toNodeNumber().doubleValue();

        if (cons instanceof NodeMatrix) {
            return Double.compare(sum, cons.getTransformer().toNodeNumber().doubleValue());
        } else {
            return new NodeDouble(sum).compareTo(cons);
        }
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof NodeMatrix) {
            return this.toDoubleMatrix().equals(
                    ((NodeMatrix) object).toDoubleMatrix());
        }

        return false;
    }

    public int colCount() {
        if (rowCount() == 0)
            return 0;

        return values[0].length;
    }

    public int rowCount() {
        return values.length;
    }

    public Node[][] getValues() {
        return values;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(values);
    }

    public Matrix toDoubleMatrix() {
        NodeConstant[][] a = toNodeConstants();

        double[][] v = new double[rowCount()][colCount()];

        for (int i = 0; i < rowCount(); i++) {
            for (int j = 0; j < colCount(); j++) {
                v[i][j] = a[i][j].getTransformer().toNodeNumber().doubleValue();
            }
        }

        return new Matrix(v);
    }

    public String toShortString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        if (values.length == 0)
            return "[]";

        for (int i = 0; i < values.length; i++) {
            builder.append(new NodeVector(values[i]).toString());
        }

        builder.append("]");
        return builder.toString();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        String tmp;

        int m = this.values.length;
        if (m == 0)
            return "[]";

        int n = this.values[0].length;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                tmp = j == 0 ? "\n" : "\t";

                tmp += this.values[i][j].toString();
                builder.append(tmp);
            }
        }
        return builder.toString().trim();
    }

    @Override
    public NodeTransformer createTransformer() {
        return new NodeMatrixTransformer();
    }

    @Override
    public NodeMatrix copy() {
        return this;
    }

    private NodeConstant[][] toNodeConstants() {
        NodeConstant[][] result = new NodeConstant[rowCount()][colCount()];
        for (int i = 0; i < rowCount(); i++)
            for (int j = 0; j < colCount(); j++)
                result[i][j] = (NodeConstant) values[i][j];

        return result;
    }

    private class NodeMatrixTransformer implements NodeTransformer {

        @Override
        public NodeVector toNodeVector() {
            NodeVector[] vectors = new NodeVector[values.length];
            for (int i = 0; i < values.length; i++) {
                vectors[i] = new NodeVector(values[i]);
            }
            return new NodeVector(vectors);
        }

        @Override
        public NodeMatrix toNodeMatrix() {
            return NodeMatrix.this;
        }

        @Override
        public NodeNumber toNodeNumber() {
            NodeNumber sum = NodeFactory.createZeroNumber();
            NodeConstant[][] constants = toNodeConstants();
            for (int i = 0; i < rowCount(); i++)
                for (int j = 0; j < colCount(); j++)
                    sum = sum.add(constants[i][j].getTransformer().toNodeNumber());
            return sum;

        }
    }

    @Override
    public NodeMatrix applyBiFunc(NodeConstant b, BiFunction<NodeNumber, NodeNumber, NodeConstant> func) {
        NodeMatrix arg2 = b.getTransformer().toNodeMatrix();
        normalizeMatrixSizes(arg2);

        NodeConstant[][] results = new NodeConstant[rowCount()][colCount()];

        for (int i = 0; i < rowCount(); i++) {
            for (int j = 0; j < colCount(); j++) {
                results[i][j] = func.apply(this.values[i][j].getTransformer().toNodeNumber(),
                        arg2.values[i][j].getTransformer().toNodeNumber());
            }
        }

        return new NodeMatrix(results);
    }

    private void normalizeMatrixSizes(NodeMatrix b) {
        if (this.rowCount() == b.rowCount() && this.colCount() == b.colCount())
            return;

        // m == rows
        int longest = Math.max(this.rowCount(), b.rowCount());

        if (this.rowCount() != longest) {
            NodeConstant[][] results = new NodeConstant[longest][colCount()];

            if (this.rowCount() == 1) {
                for (int i = 0; i < longest; i++)
                    for (int j = 0; j < this.colCount(); j++)
                        results[i][j] = (NodeConstant) this.values[0][j];
            } else {
                for (int i = 0; i < this.rowCount(); i++)
                    for (int j = 0; j < this.colCount(); j++)
                        results[i][j] = (NodeConstant) this.values[i][j];

                for (int i = this.rowCount(); i < longest; i++)
                    for (int j = 0; j < colCount(); j++) {
                        results[i][j] = NodeFactory.createZeroNumber();
                    }
            }
            this.values = results;
        } else {
            NodeConstant[][] results = new NodeConstant[longest][b.colCount()];

            if (b.rowCount() == 1) {
                for (int i = 0; i < longest; i++)
                    for (int j = 0; j < b.colCount(); j++)
                        results[i][j] = (NodeConstant) b.values[0][j];
            } else {
                for (int i = 0; i < b.rowCount(); i++)
                    for (int j = 0; j < b.colCount(); j++)
                        results[i][j] = (NodeConstant) b.values[i][j];

                for (int i = b.rowCount(); i < longest; i++)
                    for (int j = 0; j < b.colCount(); j++)
                        results[i][j] = NodeFactory.createZeroNumber();
            }

            b.values = results;
        }

        longest = Math.max(this.colCount(), b.colCount());

        if (this.colCount() != longest) {
            NodeConstant[][] results = new NodeConstant[this.rowCount()][longest];

            if (this.colCount() == 1) {
                for (int i = 0; i < rowCount(); i++)
                    for (int j = 0; j < longest; j++)
                        results[i][j] = (NodeConstant) this.values[i][0];
            } else {
                for (int i = 0; i < this.rowCount(); i++)
                    for (int j = 0; j < this.colCount(); j++)
                        results[i][j] = (NodeConstant) this.values[i][j];

                for (int i = 0; i < this.rowCount(); i++)
                    for (int j = this.colCount(); j < longest; j++)
                        results[i][j] = NodeFactory.createZeroNumber();
            }

            this.values = results;
        } else {
            NodeConstant[][] results = new NodeConstant[b.rowCount()][longest];

            if (b.colCount() == 1) {
                for (int i = 0; i < b.rowCount(); i++)
                    for (int j = 0; j < longest; j++)
                        results[i][j] = (NodeConstant) b.values[i][0];
            } else {
                for (int i = 0; i < b.rowCount(); i++)
                    for (int j = 0; j < b.colCount(); j++)
                        results[i][j] = (NodeConstant) b.values[i][j];

                for (int i = 0; i < b.rowCount(); i++)
                    for (int j = b.colCount(); j < longest; j++)
                        results[i][j] = NodeFactory.createZeroNumber();
            }

            b.values = results;
        }
    }
}
