package main.java;

/*
 * collection of methods that could be reused across problems
 */

import java.util.TreeSet;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public abstract class Library {
	
	/*
	 * alphabetical score of a word
	 * eg: COLIN = 3 + 15 + 12 + 9 + 14 = 53
	 */
	protected int alphaScore(String s) {
		int score = 0;
		s = s.toUpperCase(); // P022 only has uppercase names, so this line isn't needed
		for (int i=0; i<s.length(); i++) {
			score += (int)s.charAt(i) - 64; // because s is uppercase
		}		
		return score;
	}
	
	/*
	 * Binomial Coefficient in combinatorics:
	 * C(n,k) = "n choose k" = n!/(k!(n-k)!)
	 */
	protected long binomialCoefficient(int n, int k) {
		long coefficient = 1;
		k = Math.min(k, n-k); // C(n,k) == C(n,n-k) 
		
		// using multiplicative formula for binomial coefficient:
		for (int i=1; i<=k; i++) {
			coefficient *= (n+1-i);
			coefficient /= i;
		}		
		return coefficient;
	}
	
	/*
	 * return the cyclic permutations of a number
	 * eg: 1234, 2341, 3412, 4123
	 */
	protected ArrayList<Integer> cyclicallyPermute(int n) {
		String p = Integer.toString(n);
		int count = p.length();
		ArrayList<Integer> permutations = new ArrayList<Integer>(count);
		while (count>0) {
			permutations.add(Integer.parseInt(p));
			p = p.substring(1) + p.charAt(0);
			count--;
		}		
		return permutations;
	}
	
	/*
	 * decimal number system to factorial number system
	 * TODO: NOT USED
	 */
	protected int decimalToFactorial(int n) {
		int factorial = 0;  // number in factorial system, to be returned
		int placeValue = 1; // place value of digit 
		while (n > 0) {
			factorial += (n%placeValue)*(Math.pow(10, placeValue-1));
			n /= placeValue;
			placeValue++;              // increment place value
		}
		return factorial;
	}
	
	/*
	 * n factorial = n! = n x (n-1) x (n-2) x ... x 3 x 2 x 1
	 * 
	 * using prime factorization of n:
	 * eg: 10! 
	 * primes below 10 are; 2, 3, 5, and 7
	 * 10! = 2^8 x 3^4 x 5^2 x 7^1
	 */
	protected BigInteger factorial(int n) {
		BigInteger answer = new BigInteger("1");
		
		// get prime factors of n:
		ArrayList<Integer> primesBelow = getPrimesBelow(n);
		if (isPrime(n)) primesBelow.add(n); // have to add n if it is prime itself
		for (Integer p : primesBelow) {
			int exponent = 0; // exponent of prime p to get prime factor
			for (int i=1; true; i++) { 
				int e = (int) (n/Math.pow(p, i));
				if (e < 1) { 
					break;
				} else {
					exponent += e;
				}
			}
			// multiply by each prime factor:
			answer = answer.multiply(new BigInteger(String.valueOf(p)).pow(exponent));
		}
		return answer;
	}
	
	/*
	 * factorial number system to decimal number system
	 * TODO: NOT USED
	 */
	protected int factorialToDecimal(int n) {
		int decimal = 0;    // number in decimal system, to be returned
		int placeValue = 1; // factorial digit place value
		int digit;          // current digit of factorial number
		n /= 10;            // cut off last 0 of all numbers in factorial system
		
		while (n > 0) {
			// take last digit and multiply by place value as a factorial
			digit = n%10;
			for (int i=1; i<=placeValue; i++) { 
				digit *= i;
			}
			decimal += digit;
			placeValue++;                 // increment place value for next digit
			n /= 10;                      // cut off last digit
		}		
		return decimal;
	}
	
	/*
	 * return the index of the first Fibonacci number that is 
	 * equal to or larger than the argument 'fib'
	 */
	protected int fibonacciIndexOf(BigInteger limit) {
		BigInteger[] f = new BigInteger[3]; // store previous two fibs and the next
		int count = 2;                      // index count so far

		// first two fib numbers are 1:
		f[0] = BigInteger.ONE;
		f[2] = BigInteger.ONE;

		int i = 0;
		// calculate fibs while current fib is less than limit:
		while ((f[i]).compareTo(limit) < 0) { 
			i = (i+1)%3; // index to store next fib in the array of 3
			f[i] = f[(i+1)%3].add(f[(i+2)%3]); // store next fib
			count++; // increment count of fibs so far
		}
		return count;
	}
	
	/*
	 * greatest common divisor of two numbers
	 * (returns largest integer that divides both a and b without a remainder)
	 */
	protected long gcd(long a, long b) {
		if (b == 0) {
			return a;
		} else {
			return gcd(b, a%b);
		}
	}
	
	/*
	 * Get the unique digits of a number.
	 * Repeated digits are ignored.
	 * eg: getDigits(4242) = [2,4]
	 */
	protected TreeSet<Integer> getDigitsUnique(long n) {
		TreeSet<Integer> digits = new TreeSet<Integer>();
		while (n > 0) {
			int digit = (int)(n % 10);
			digits.add(Integer.valueOf(digit));
			n /= 10;
		}
		if (digits.isEmpty()) digits.add(Integer.valueOf(0)); // add 0 if n=0
		return digits;
	}
	
	/*
	 * get the factors of a number
	 */
	protected TreeSet<Long> getFactors(long n) {
		TreeSet<Long> factors = new TreeSet<Long>();
		factors.add(n);  // n is always a factor of n
		factors.add(1L); // 1 is always a factor of n
		long max = (long) Math.sqrt(n) + 1; // largest first number of the factor pairs
		for (long i = 2; i <= max; i++) {
			if (n % i == 0) {
				factors.add(i);
				factors.add(n / i);
			}
		}
		return factors;
	}
	
	/*
	 * get the nth hexagonal number
	 * H(n) = n(2n-1)
	 */
	protected long getHexagonal(int n) {
		if (n<0) return 0;
		long m = (long) n;
		return 2*m*m - m;
	}
	
	/*
	 * get the nth pentagonal number
	 * P(n) = n(3n-1)/2
	 */
	protected int getPentagonal(int n) {
		if (n<1) return 0;
		return ((3*n*n)-n)/2;
	}
	
	/*
	 * get the nth prime
	 */
	protected long getPrime(int n) {
		if (n<1) { return 0; } 
		int numPrimes = 0; // number of primes so far
		long prime = 1;
		while (numPrimes < n) {
			prime++;
			if (isPrime(prime)) {
				numPrimes++;
			}
		}		
		return prime;
	}
	
	/*
	 * get all the prime numbers below a value (n),
	 * using Sieve of Eratosthenes
	 */
	protected ArrayList<Integer> getPrimesBelow(int n) {
		ArrayList<Integer> primes = new ArrayList<Integer>();
		if (n<=2) { return primes; } // no primes below 2
		
		boolean[] sieve = new boolean[n]; // false = prime, true = composite
		sieve[0] = true;
		sieve[1] = true; 
		sieve[2] = false; // prime

		// sieve out even numbers
		for(int i=4; i<n; i+=2) { 
			sieve[i] = true; 
		}

		int limit = (int)Math.sqrt(n)+1;
		for(int i=3; i<limit; i+=2) { // iterate through odd numbers
			if(!sieve[i]) { // if prime
				for(int j=i*i; j<n; j+=i) { // sieve out multiples of i
					sieve[j] = true; 
				}
			}
		}
		
		// get primes as list
		for (int i=0; i<sieve.length; i++) {
			if (!sieve[i]) {
				primes.add(i);
			}
		}
		return primes;
	}
	
	/*
	 * get the nth triangular number
	 * = 1 + 2 + 3 + ... + (n-2) + (n-1) + n
	 * = (n(n+1))/2
	 */
	protected long getTriangularNumber(long n) {
		if (n<1) { return 0L; }
		return (n*(n+1))/2;
	}
	
	/*
	 * slightly faster for positive integers than Math.pow(n,exp)
	 */
	protected int intPow(int n, int exp) {
		int pow = 1;
		for (int i=1; i<=exp; i++) {
			pow *= n;
		}
		return pow;
	}
	
	/*
	 * Inverse function checks if: 
	 * x == n*i*i, where i is an integer > 0
	 */
	protected boolean isNSquare(int x, int n) {
		double test = Math.sqrt(Integer.valueOf(x).doubleValue()/n);
		if (test == (int)test) return true;
		return false;
	}
	
	/*
	 * checks if a String is a palindrome or not
	 * (reads the same forwards and backwards)
	 */
	protected boolean isPalindrome(String s) {
		for (int i=0; i<s.length()/2; i++) {
			if (Character.toLowerCase(s.charAt(i)) != 
					Character.toLowerCase(s.charAt(s.length()-1-i))) {
				return false;
			}
		}
		return true;
	}
	
	/*
	 * n is a number, it is pandigital if:
	 * it makes use of every number from 1 to the number of digits in n,
	 * exactly once
	 */
	protected boolean isPandigital(int n) {
		if (n==0) return false;
		int numDigits = 0;                                // number of digits in n
		HashSet<Integer> digits = new HashSet<Integer>(); // store unique digits from n
		while (n > 0) {
			digits.add(n%10); // get last digit
			n /= 10;          // remove last digit
			numDigits++;      // increment number of digits in n
		}
		for (int i=1; i<=numDigits; i++) {
			if (!digits.contains(Integer.valueOf(i))) return false;
		}
		return true;
	}
	
	/*
	 * Determines if x is a pentagonal number or not.
	 * P(n) = n(3n-1)/2
	 * The inverse function:
	 * n = (sqrt(24x + 1) + 1)/6
	 * where x is a pentagonal number iff n is a natural number
	 */
	protected boolean isPentagonal(long x) {
		double test = (Math.sqrt((24 * x) + 1) + 1.0) / 6.0;
		if (test == (int)test) return true;
		return false;
	}
	
	/*
	 * is a number a prime number or not?
	 * through trial division
	 */
	protected boolean isPrime(long n) {
		if (n == 2) return true;                   // 2 is prime
		if ((n < 2) || (n % 2 == 0)) return false; // not prime if less than 2 or a multiple of 2
		long max = (long) Math.sqrt(n); 
		for (long i=3; i<=max; i+=2) {
			if (n % i == 0) return false;          // not prime if multiple of a number
		}
		return true;
	}
	
	/*
	 * checks if whole list is prime
	 */
	protected boolean isPrime(ArrayList<Integer> list) {
		for (Integer i : list) {
			if (!isPrime(i.intValue())) return false;
		}
		return true;
	}
	
	/*
	 * for triangle side lengths a, b, and c, does Pythagorean Theorem hold?
	 * a^2 + b^2 = c^2 
	 * a, b, and c are integers
	 */
	protected boolean isRightTriangle(int a, int b, int c) {
		double _c = Math.sqrt((a*a)+(b*b));
		if (_c != Math.floor(_c)) return false; // check that calculated c is whole number
		if(((int)_c) == c) return true;
		return false;
	}
	
	/*
	 * least common multiple of the numbers [1,n]
	 * (returns smallest integer divisible by all numbers [1,n])
	 */
	protected long lcm(long n) {
		long lcm = 1;
		for (long i=2; i<=n; i++) {
	    	lcm *= i/gcd(i,lcm);
		}
		return lcm;	
	}
	
	/*
	 * find the size of the longest recurrence in 1/d
	 * uses long division
	 */
	protected int longestRecurrence (int d) {
		int stop = 1;
		int[] cycles = new int[d+1];
		int cycle = 0;
		while ((stop > 0) && (cycles[stop] == 0)) {
			cycle++;
			cycles[stop] = cycle;
			stop = (stop * 10) % d;
		}
		if (stop == 0) return 1;
		return cycle - cycles[stop] + 1; 
	}
	
	/*
	 * calculate maximum sum of two rows in a triangle
	 * eg:
	 *    7 4
	 * + 2 4 6
	 * --------
	 * = 11 10
	 */
	protected int[] maxRowsSum(int[] top, int[] bottom) {
		for (int i=0; i<top.length; i++) {
			top[i] += Math.max(bottom[i], bottom[i+1]);
		}
		return top;
	}
	
	/*
	 * next number in Collatz Chain
	 */
	protected long nextCollatz(long n) {
		if (n%2 == 0) { // n is even
			return n/2;
		} else {        // n is odd
			return (3*n)+1;
		}
	}
	
	/*
	 * get the number of digits in positive integer n
	 */
	protected int numberOfDigits(long n) {
		int count = 0;
		while (n > 0) {
			n /= 10;
			count++;
		}
		return count;
	}
	
	/*
	 * estimate how many fibonacci numbers <= value, using Binet's formula
	 */
	protected double numberOfFibonaccis(double value) {
		double phi = (1 + Math.sqrt(5))/2;
		return 1 + (Math.log((Math.sqrt(5))*(value+0.5)))/(Math.log(phi));
	}
	
	/*
	 * returns the word representation of a number in the range [-1000,1000]
	 */
	protected String numberToWords(int n) {
		String words = "";
		if (n>1000) { return words; } // not yet implemented
		
		if (n < 0) {
			n = Math.abs(n);
			words += "negative";
		} else if (n == 0) {
			return "zero";
		} else if (n == 1000) {
			return "onethousand";
		}
		
		String[] onesAndTeens = {"", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine", 
				"ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"};
		String[] tens = {"", "ten", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"};
		
		while (n != 0) {
			if (n < 20) {
				words += onesAndTeens[n % 20];
				break;
			} else if (n < 100) {
				words += tens[n / 10];
				n %= 10;
			} else if (n < 1000) {
				words += onesAndTeens[n / 100] + "hundred";
				if (n % 100 != 0) {
					words += "and";
				}
				n %= 100;
			} 
		}
		
		return words;
	}
	
	/*
	 * Get a list of all permutations of a number.
	 * Uses Heap's algorithm.
	 * n = number of digits
	 * p = number converted to int[]
	 * perms = permutations to be computed (call with an empty ArrayList) 
	 */
	protected void permutations(int n, int[] p, ArrayList<Long> perms) {
		if (n == 1) {
			Long permutation = new Long(0);
			int numDigits = p.length;
			for (int i=0; i<numDigits; i++) { // convert digits in int[] to number
				permutation += Long.valueOf(p[i])*Long.valueOf(intPow(10, numDigits-(i+1))); 
			}
			perms.add(permutation); // add permutation
		} else {
			for (int i=0; i<n; i++) {
				permutations(n-1, p, perms);
				if (n % 2 == 1) { // if n is odd, swap(0, n-1)
					int temp = p[0];
					p[0] = p[n-1];
			        p[n-1] = temp;
				} else {          // if n is even, swap (i, n-1)
					int temp = p[i];
					p[i] = p[n-1];
			        p[n-1] = temp;
				}
			}
		}
	}
	
	/*
	 * read a file to a String
	 */
	protected static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
	
	/*
	 * replaces the ith digit of the number n with digit d
	 */
	protected int replaceDigit(int i, int n, int d) {
		if ((i < 0) || (d < 0) || (d > 9)) return n; // index out of range, or not digit
		int oldN = n;
		ArrayList<Integer> digits = new ArrayList<Integer>();		
		while (n > 0) { 
			digits.add(n % 10); // get last digit
			n /= 10;
		}
		if (i > digits.size()-1) return oldN; // index is out of range
		digits.set(digits.size()-i-1, Integer.valueOf(d)); // replace digit
		
		for (int digit=0; digit<digits.size(); digit++) {
			n += digits.get(digit)*intPow(10, digit);
		}	
		return n;
	}
	
	/*
	 * square of the sum of the numbers [a,b]
	 * eg: [(a) + (a+1) + (a+2) + ... + (b-2) + (b-1) + (b)]^2
	 */
	protected long squareOfSum(int a, int b) {
		long sum = 0;
		for (int i=a; i<b+1; i++) {
			sum += i;
		}
		return sum*sum;
	}
	
	/*
	 * Return a piece of a number.
	 * 
	 * num    = the starting number to get a piece of
	 * start  = the starting index
	 * offset = the number of digits to include 
	 * 
	 * eg: subNum(123456L, 2, 3) = 345
	 */
	protected long subNum(long num, int start, int offset) {
		long sub = 0L;
		ArrayList<Integer> digits = new ArrayList<Integer>();		
		while (num > 0) { 
			digits.add((int) (num % 10)); // get last digit
			num /= 10;
		}
		Collections.reverse(digits);
		int pow = offset-1;
		for (int i=0; i<offset; i++) {
			sub += digits.get(start + i)*intPow(10, pow);
			pow--;
		}
		return sub;
	}
	
	/*
	 * sum the amicable numbers in interval [1,n]
	 * (a and b are amicable numbers if:
	 * a != b, and
	 * sum of proper divisors of a = b, and
	 * sum of proper divisors of b = a)
	 */
	protected int sumAmicables(int n) {
		int result = 0; // sum of amicable numbers to return
		int b = 0;      // possible amicable number
		for (int a = 4; a <= n; a++) {
			b = sumPropDivs(a);
			if ((b > a) && (b <= n) && (sumPropDivs(b) == a)){
				result += a + b;
			}
		}
		return result;
	}
		
	/*
	 * add together the digits of a number n,
	 * ignore's negative sign of negative numbers
	 * eg: sumDigits(123) = sumDigits(-123) = 1+2+3 = 6
	 */
	protected long sumDigits(BigInteger n) {
		n = n.abs();
		long sum = 0;
		while (!n.equals(BigInteger.ZERO)) {
			sum += n.mod(BigInteger.TEN).longValue(); // add last digit (in one's place)
			n = n.divide(BigInteger.TEN);             // cut off last digit
		}
		return sum;
	}
	
	/*
	 * sum of the squares of the numbers [a,b]
	 * eg: (a)^2 + (a-1)^2 + (a-2)^2 + ... + (b-2)^2 + (b-1)^2 + (b)^2
	 */
	protected long sumOfSquares(int a, int b) {
		long sum = 0;
		for (int i=a; i<b+1; i++) {
			sum += i*i;
		}
		return sum;
	}

	/*
	 * sum of proper divisors of n
	 */
	protected int sumPropDivs(int n) {
		if (n<2) return 0;
		int sum = 1; // 1 is always a divisor
		int max = (int)Math.sqrt(n); // only need to check up to sqrt(n)
		for (int i = 2; i <= max; i++) {
			if (n % i == 0) {
				int j = n/i;
				if (i == j) {
					sum += i;
				} else {
					sum += i + j;
				}
			}
		}
		return sum;
	}
	
	/*
	 * remove the rightmost most digit of n
	 * eg: 1234 = 123
	 */
	protected int truncateLeft(int n) {
		return n/10;
	}
	
	/*
	 * remove the leftmost most digit of n
	 * eg: 1234 = 234
	 */
	protected int truncateRight(int n) {
		return (int)(n % Math.pow(10, (int)Math.log10(n)));
	}
	
	
}
