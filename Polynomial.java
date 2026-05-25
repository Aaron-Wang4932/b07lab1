import java.io.File;
import java.util.Scanner;

public class Polynomial{
    // Field to store coefficeints. Assumed to be in the form a_0 + a_1\cdot x^1 + ... + a_{n-1}x^{n-1}
	private double[] coefficients;
    
    private int[] exponents;

    // No-arg constructor to set the polynomial to 0.
	public Polynomial() {
		this.coefficients = new double[1];
        this.exponents = new int[1];
	}

 
	public Polynomial(double[] coefficients, int[] exponents) {
		this.coefficients = coefficients;
        this.exponents = exponents;
	    
    }

    public Polynomial(File file) {
        try {
            Scanner scanner = new Scanner(file);
            String formula = scanner.nextLine();
            scanner.close();

            String[] terms = formula.split("(?=[+-])");
            
            coefficients = new double[terms.length];
            exponents = new int[terms.length];

            int i = 0;
            if(!terms[0].contains("x")) {
                coefficients[0] = Double.parseDouble(terms[0]);
                exponents[0] = 0;
                i++;
            }


            for(; i < terms.length; i++) {
                String[] coeffAndExpStr = terms[i].split("x");
                coefficients[i] = Double.parseDouble(coeffAndExpStr[0]);
                exponents[i] = Integer.parseInt(coeffAndExpStr[1]);
            }
        } catch (Exception e) {
            System.out.println("Unable to read provided file " + file.getPath() + "\nUsing default value f(x)=0.");
            coefficients = new double[1];
            exponents = new int[1];
        }
    }

    @Override
    public String toString() {
        // If we have a constant function:
        if(this.coefficients.length == 1)
            return this.coefficients[0] + "";

        String out = this.coefficients[0] + "";
        for(int i = 1; i < this.coefficients.length; i++)
            out = out + this.coefficients[i] + "x" + this.exponents[i];
        return out;
    }

	public Polynomial add(Polynomial p) {
        double[] pCoeffs = p.coefficients;
        int[] pExps = p.exponents;
        
        double[] tempCoeffs = new double[pCoeffs.length + this.coefficients.length];
        int[] tempExps = new int[pExps.length + this.exponents.length];

        int i = 0; // index for this
        int j = 0; // index for p
        int k = 0; // index for temp
        
        // It's basically mergesort algo lol
        while(i < this.coefficients.length && j < pCoeffs.length) {
            if(this.exponents[i] == pExps[j]) { // same coeffs: add
                double sum = this.coefficients[i] + pCoeffs[j];
                if(sum != 0) {
                    tempCoeffs[k] = sum;
                    tempExps[k] = this.exponents[i];
                    k++;
                }
                i++;
                j++;
            } else if (this.exponents[i] < pExps[j]) { // this has smaller coeff: want to maintain incr. order of degree
                tempCoeffs[k] = this.coefficients[i];
                tempExps[k] = this.exponents[i];
                i++;
                k++;
            } else {
                tempCoeffs[k] = pCoeffs[j];
                tempExps[k] = pExps[j];
                j++;
                k++;
            }     
        }

        while(j < pCoeffs.length) {
            tempCoeffs[k] = pCoeffs[j];
            tempExps[k] = pExps[j];
            j++;
            k++;
        }

        while(i < this.coefficients.length) {
            tempCoeffs[k] = this.coefficients[i];
            tempExps[k] = this.exponents[i];
            i++;
            k++;
        }

        // If all the terms happened to cancel out:
        if(k == 0) return new Polynomial();
        
        double[] outC = new double[k];
        int[] outE = new int[k];
        for(int m = 0; m < k; m++) {
            outC[m] = tempCoeffs[k];
            outE[m] = tempExps[k];
        }
        return new Polynomial(outC, outE);
	}

    public double evaluate(double x) {
        int len = this.coefficients.length;
        double out = 0;
        for(int i = 0; i < len; i++) {
            out += this.coefficients[i]*Math.pow(x, i);
        }
        return out;
    }

    public boolean hasRoot(double x) {
        return evaluate(x) == 0;
    }
}
