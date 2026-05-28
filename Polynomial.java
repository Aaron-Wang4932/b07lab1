import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
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

            for(int i = 0; i < terms.length; i++) {
                if(!terms[i].contains("x")) {
                    coefficients[i] = Double.parseDouble(terms[i]);
                    exponents[i] = 0;
                } else {
                    String[] coeffAndExpStr = terms[i].split("x");
                    if(coeffAndExpStr[0] == "") coefficients[i] = 1;
                    else if(coeffAndExpStr[0].equals("-")) coefficients[i] = -1;
                    else coefficients[i] = Double.parseDouble(coeffAndExpStr[0]);
                    
                    if(coeffAndExpStr.length == 1) exponents[i] = 1;
                    else exponents[i] = Integer.parseInt(coeffAndExpStr[1]);

                }

            }
        } catch (Exception e) {
            System.out.println("Unable to read provided file " + file.getPath() + "\nUsing default value f(x)=0.");
            coefficients = new double[1];
            exponents = new int[1];
        }
    }

    @Override
    public String toString() {
        String out = "";
        for(int i = 0; i < this.coefficients.length; i++) {
            if(i == 0) {
                out += this.coefficients[i];
                if(this.exponents[i] != 0) out += "x" + this.exponents[i];
            } else {
                if(this.coefficients[i] > 0) out += "+";
                out += this.coefficients[i] + "x" + this.exponents[i];
            }
        }
        return out;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;

        if(obj == null || !(obj instanceof Polynomial)) return false;

        Polynomial comp = (Polynomial) obj;
        return Arrays.equals(this.coefficients, comp.coefficients) && Arrays.equals(this.exponents, comp.exponents);
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
            outC[m] = tempCoeffs[m];
            outE[m] = tempExps[m];
        }
        return new Polynomial(outC, outE);
	}
    public Polynomial multiply(Polynomial p) {
        // Take (this) = a1 + a2 + ... + an, (p) = b1 + b2 + ... + bk ---> (this) and (p) will both be guaranteed to have ascending degree order
        // Either from initial assumption or because add function guarantees the output to be in ascending order
        // Then, (this)(p) = (a1b1 + a1b2 + ... + a1bk) + (a2b1 + a2b2 + ... + a2bk) + ... + (anb1 + anb2 + ... + anbk)
        // Just add each monomial to an output Polynomial.

        double[] pCoeffs = p.coefficients;
        int[] pExps = p.exponents;

        int n = this.coefficients.length, k = pCoeffs.length;
        
        Polynomial out = new Polynomial(new double[0], new int[0]); // want completely empty poly! not one that is the zero func.

        for(int i = 0; i < n; i++) {
            for(int j = 0; j < k; j++) {
                double[] newCoeff = {this.coefficients[i] * pCoeffs[j]};
                int[] newExp = {this.exponents[i] + pExps[j]};

                Polynomial kthTermInN = new Polynomial(newCoeff, newExp);
                out = out.add(kthTermInN);
            }
        }

        // hacky check to see if out is 0.
        if(out.coefficients.length == 0 || out.exponents.length == 0) out = new Polynomial();
        return out;
    }

    public double evaluate(double x) {
        int len = coefficients.length;
        double out = 0;
        for(int i = 0; i < len; i++) {
            double term = coefficients[i] * Math.pow(x, exponents[i]);
            out += term;
        }
        return out;
    }

    public boolean hasRoot(double x) {
        return evaluate(x) == 0;
    }

    public void saveToFile(String path) {
        try (BufferedWriter w = new BufferedWriter(new FileWriter(path))) {
            w.write(this.toString());
        } catch (Exception e) {
            System.out.println("could not write polynomial to file");
            e.printStackTrace();
        }
    }
}
