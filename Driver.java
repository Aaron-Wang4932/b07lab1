import java.io.File;

public class Driver {
    public static void main(String [] args) {
        //¹
        //²
        //³
        //⁴
        //⁵
        //⁶
        //⁷
        //⁸
        //⁹
        //⁰
        Polynomial poly1 = new Polynomial(new double[]{1, 2.5, 3}, new int[]{0,2,3}); // 1 + 2.5x² + 3x³
        Polynomial poly2 = new Polynomial(new double[]{2, 3, 4.2}, new int[]{0, 1, 3}); // 2 + 3x + 4.2x³

        Polynomial add_1_2 = poly1.add(poly2); // Want 3 + 3x + 2.5x² + 7.2x³
        System.out.println(add_1_2.toString());
        add_1_2.saveToFile("sum.txt");

        Polynomial mult_1_2 = poly1.multiply(poly2); // Want 2 + 3x + 5x² + 17.7x³ + 9x⁴ + 10.5x⁵ + 12.6x⁶
        System.out.println(mult_1_2.toString());
        mult_1_2.saveToFile("product.txt");

        System.out.println(new Polynomial(new File("product.txt")).equals(mult_1_2));
        System.out.println("\nLet f(x)=2 + 3x + 5x² + 17.7x³ + 9x⁴ + 10.5x⁵ + 12.6x⁶. Then:");
        System.out.println("f(2)=1456 (function output yields "+mult_1_2.evaluate(2)+")");
        System.out.println("\nLet g(x) = -10 + x + x³. Then:");
        System.out.println("g(x) has a root at x=2? "+new Polynomial(new double[]{-10,1,1}, new int[]{0,1,3}).hasRoot(2));

    }
}
