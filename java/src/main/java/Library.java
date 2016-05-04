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
	 * find the longest recurrence of substring in String s
	 */
	protected String longestRecurrence (String s) {
		String longest = s.substring(s.length()-1); // longest recurrence to return
		String sub = "";  // substring of s
		String rest = ""; // s - sub
		for (int i=0; i<s.length(); i++) {
			for (int j=i+1; j<s.length(); j++) {
				sub = s.substring(i, j);
				int lsub = sub.length();
				rest = (j!=s.length()-1) ? s.substring(j) : ""; 
				int lrest = rest.length();
				
				/* 
				 * sub must be less than whole s, and
				 * sub must be longer than longest, and 
				 * rest of s must be able to contain multiples of sub,
				 * and length of rest must be greater than 0:
				 */
				if ((sub.equals(s)) ||
						(lsub <= longest.length()) || 
						(lrest % lsub != 0) ||
						(lrest == 0)) continue;
				
				// check that rest of String is recurrences of sub1:
				boolean recurrence = true;
				for (int k=0; k<lrest/lsub; k++) {
					String sub2 = rest.substring(k*lsub, (k+1)*lsub);
					if (!sub2.equals(sub)) {
						recurrence = false;
						break;
					}
				}
				if (recurrence) longest = sub;
			}
		}		
		return longest;
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
	 * read a file to a String
	 */
	static String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
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
	
}
