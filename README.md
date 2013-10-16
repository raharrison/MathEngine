# Math Engine

A mathematical library complete with an advanced expression parser with custom functions and support for Vectors and Matrices. Includes packages for symbolic differentiation, numeric integration, equation solving, unit conversions and much more.

### Functions

The Function class provides a common interface into many packages of this library. A Function models an equation of one variable that maps to a resulting Y value. To create a basic Function object use the constructor that takes a single String argument representing the desired equation:

    Function function = new Function("x^2 + 8*x + 12");
    
To create more customised Functions, use the overloaded constructors where you can define the variable and the angle units that will be used when calculating any trigonometric values in the function:

    Function function = new Function("x^2 + 8*x + 12", "x", AngleUnit.Radians);
    
By default the Function will have a variable of 'x' and will use Radians if they are not explicitly specified in the constructor.

Once and instance of the Function class is created, you also have the ability to evaluate the function at a specified point (any instances of the variable will become the specified value during evaluation). For example:

    Function function = new Function("x^2 + 8*x + 12");
    double result = function.evaluateAt(12.5);
    
    OUTPUT -
    268.25
    
You can also evaluate the expression at another expression. This is better explained in an example:


    Function function = new Function("x^2 + 8*x + 12");
    double result = function.evaluateAt("6 + 6.5");
    
    OUTPUT -
    268.25
    
### Differential

The differential package includes classes to both symbolically and numerically differentiate Function instances. Numeric differentiation will only allow you to differentiate at a certain point (giving the value of the derivative at this point). Conversely symbolic differentiation will return a new Function object representing the exact derivative to the Function. This resulting Function can then be evaluated as above to get the a value at a certain point (as in numeric differentiation).

#### Numeric

    Function function = new Function("x^2 + 8*x + 12");
    DividedDifferenceMethod method = new DividedDifferenceMethod(function, DifferencesDirection.Central);
    
In this example a Function is first created by which the deriviatives values will be calculated. Then a new instance of a DividedDifferenceMethod differentiator is created that uses the the [Finite Difference method](http://en.wikipedia.org/wiki/Finite_difference) with central equations. A result of the first derivative at the point 3.5 can be obtained by:

    method.setTargetPoint(3.5);
    double derivative = method.deriveFirst();

    OUTPUT - 
    15.000000000000213
    
Values can change depending on the supplied value of h (the difference). By default the value is 0.01. Other classes are available that can yield more accurate results in certain circumstances are also available which include the ExtendedCentralDifferenceMethod (more accurate than standard differences as above) and [RichardsonExtrapolationMethod](http://en.wikipedia.org/wiki/Richardson_extrapolation) (which again can yield more accurate results).

The numerical methods only give estimates of the derivatives and in some cases may be wildly inaccurate. However for most well formed, continuous functions, and a sensible value of h, a reasonably accurate answer can be obtained. For exact results consider the symbolic package.

#### Symbolic

The symbolic differentiation package offers support to obtain exact derivatives of Function objects. Instead of returning a double value of the derivative at a certain point (as in the numeric package), the symbolic package returns a new Function object representing the exact derivative function. This can then be evaluated to get the derivative at a certain point:

    Function function = new Function("x^2 + 8*x + 12");
    Differentiator differentiator = new Differentiator();
    Function derivative = differentiator.differentiate(function, true);
    derivative.evaluateAt(3.5);
    
    OUTPUT -
    f(x) = 2*x+8
    15.0
    
As you can see the symbolic differentiator returns a Function representing the equation of the derivative, which in this case is '2*x + 8'. This function is then evaluated at the some point 3.5 to get an answer of 15. Note how this result is exact and without the small error retrieved from the numeric method of differentiation.

### Distributions

The distributions package offers classes to evaluate both discrete and continuous probability distributions.

#### Discrete distributions -
 - Binomial

#### Continuous distributions -
 - Beta
 - Exponential
 - F
 - Logistic
 - Normal
 - Student T

Both the cumulative and density functions of each of these distributions can be evaluated by created the relevant object and calling either the .density or .cumulative methods. Here is an example using the Normal distribution:

    NormalDistribution normal = new NormalDistribution(15, 2.6);
    double density = normal.density(15.7);
    double cumulative = normal.cumulative(15.7);
    
    OUTPUT - 
    0.14797786432400178
    0.6061238859734724
    
### GUI

The gui package includes graphical user interfaces to various packages in the library, along with other helpful controls.

The Converter class provides a JFrame of a unit conversion program using the corresponding package in the library. Users can can select a unit from the two drop down menus and convert a value from one unit to the other. The frame also offers a way to interface with the string conversion capability of the package. For example entering the string:

    12 mph in kph
    
will automatically parse the string and use the preset list of units aliases to match a conversion. This gives an answer of:

    7.456304193410567
    
which is 12 miles per hour in kilometres per hour.

The HistoricalTextField offers a custom text-field control which remembers all previous input. This emulates the likes of a terminal window where the arrow keys can be used to navigate a list of previous input. This control is then used in the MainFrame class.

The MainFrame class provides a graphical user interface that allows the user to use the parser package. Users can simply type in their expression and the parser will evaluate and return a result which will be displayed in the frame. All supported result types are displayed including fractions, vectors, matrices and functions.

### Integral

The integral package provides multiple methods to numerically integrate functions between two points. The classes provide a numerical estimate of the integral and do not offer exact integrals. There therefore may be some error in the results.

To integrate a function, an object of the chosen IntegrationMethod should be instantiated, the lower and upper points of integration set, and the number of iterations to use specified. For example the TrapeziumIntegrator which sums the areas of trapezia under the function to estimate the integral. The more iterations, the more trapezia, and therefore the more accurate the result will be (for continuous functions):

    Function function = new Function("x^2 + 8*x + 12");
	TrapeziumIntegrator integrator = new TrapeziumIntegrator(function);
	integrator.setLower(0.5);
	integrator.setUpper(5);
	integrator.setIterations(100);
	double result = integrator.integrate();
        
    OUTPUT - 
    194.62651875000006
    
The exact integral of this function between 0.5 and 5 is:

    194.625

The SimpsonIntegrator, which fits the function to a quadratic equation, will tend to give the most accurate results.

### Linear Algebra

The linear algebra package contains classes that model Vectors and Matrices. Each class allows users to perform various common arithmetic and mathematical functions on the underlying data.

#### Vectors

To create a new Vector object, simply use one of the constructors which can take one value, a size of the vector, an array of values or even a string representing the vector of the form "{num1, num2, num3, numx}".

    double[] data = new double[] { 21.05, 23.51, 24.23, 27.71, 30.86, 45.85, 52.12, 55.98 };
    Vector vector = new Vector(data);
    
Once an object has been created, the methods can be called that perform the various mathematical operations. For example:

    Vector result = vector.multiply(new Vector("{1,2,3,4,5,6,7,8}"));
    
    OUTPUT - 
    {21.05, 47.02, 72.69, 110.84, 154.3, 275.1, 364.84, 447.84}
    
    
    Vector result = new Vector("{8,3,4}").crossProduct(new Vector("{23,6,84}"));
    
    OUTPUT - 
    {228.0, -580.0, -21.0}

When performing arithmetic on Vectors with different sizes, the smaller vector fill be padded with zeroes until it is the same size as the larger Vector:

    Vector result = vector.add(new Vector("{1,2,3}"));
    
    OUTPUT - 
    {22.05, 25.51, 27.23, 27.71, 30.86, 45.85, 52.12, 55.98}

Note how the first three numbers were changed, however the rest remained unchanged. In fact behind the scenes zero is being added to each of the numbers.

#### Matrix

To create a new Matrix object, simply use one of the constructors which can take a range of values including the number of rows and columns, a unit vector or a two dimensional double array:

    double[][] data = new double[][] {{1,2,3}, {4,5,6}, {7,8,9}};
    Matrix matrix = new Matrix(data);
    
    OUTPUT - 
    1.0    2.0	3.0
    4.0    5.0	6.0
    7.0    8.0	9.0
    
Again just like with the Vector class, once an object is created, there are various methods that can be called to carry out mathematical operations on the Matrix:

    double[][] newData = new double[][] {{10, 11, 12}, {13,14,15}, {16,17,18}};
    Matrix result = matrix.multiply(new Matrix(newData));
    
    OUTPUT - 
    84.0    90.0	96.0
    201.0	216.0	231.0
    318.0	342.0	366.0
    
    double[][] data = new double[][] { { 3, 10, -1 }, { 8, -2, 5 }, { -7, 1, 3 } };
    Matrix inverse = new Matrix(data).inverse();
    
    OUTPUT - 
    0.017828200972447323    0.05024311183144247	-0.07779578606158834
    0.09562398703403564	-0.0032414910858995145	0.03727714748784441
    0.00972447325769854	0.11831442463533225	0.1393841166936791
    
The linear algebra package also includes classes to obtain a least squares solution for two matrices through the the QRDecomposition class, and a solution to the equation A*X = B where A and B are known and X is to be found:

    Matrix A = new Matrix(new double[][] { { 3, 7 }, { 4, 12 } });
	Matrix B = new Matrix(new double[][] { { -4, 5 }, { 8, 1 } });
	Matrix X = A.solve(B);
        
    OUTPUT - 
    -13.0   6.625
    5.0     -2.125
    
And to check that the solution is valid (returns the B matrix):

    A.multiply(X)
    
    OUTPUT - 
    -4.0    5.0
    8.0     1.0