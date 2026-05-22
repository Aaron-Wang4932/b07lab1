public class Polynomial{
    // Field to store coefficeints. Assumed to be in the form a_0 + a_1\cdot x^1 + ... + a_{n-1}x^{n-1}
	private double[] coefficients;

    // No-arg constructor to set the polynomial to 0.
	public Polynomial() {
		this.coefficients = new double[0];
	}

    // Constructor which takes array of double and sets coefficients accordingly. 
	public Polynomial(double[] coefficients) {
		this.coefficients = coefficients;
	    
    }

	public Polynomial add(Polynomial p) {
        int len1 = p.coefficients.length;
        int len2 = this.coefficients.length;
        int shorter = Math.min(len1, len2), longer = Math.max(len1, len2);
        
		double[] outputCoeffs = new double[longer];
		
        int i;

		for(i = 0; i < shorter; i++)
			outputCoeffs[i] = p.coefficients[i] + this.coefficients[i];
		if(len1 >= len2) 
			for(; i < len1; i++)
                outputCoeffs[i] = p.coefficients[i];
		else 
            for(; i < len2; i++)
                outputCoeffs[i] = this.coefficients[i];
			
		return new Polynomial(outputCoeffs);

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
