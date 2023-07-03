import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class FingersToEntropy {
    
    static double getWorstEntropy(int listSize) {
        double N = listSize;
        double n = Math.ceil(Math.sqrt(N));
        return -(n * n - 5.0 * n + 3.0) / N;
    }
    
    private static final class FingerConfigurationData
            implements Comparable<FingerConfigurationData> {
        
        public int[] fingerConfiguration;
        public double entropy;

        public FingerConfigurationData(int[] fingerConfiguration,
                                       double entropy) {
            this.fingerConfiguration = fingerConfiguration;
            this.entropy = entropy;
        }
        
        @Override
        public String toString() {
            return new StringBuilder()
                    .append(Arrays.toString(fingerConfiguration))
                    .append(" ")
                    .append(entropy)
                    .toString();
        }

        @Override
        public int compareTo(FingerConfigurationData o) {
            return Double.compare(entropy, o.entropy);
        }
    }

    private static List<FingerConfigurationData> 
            getNegativeFingerConfigurations() {
                
        Fingers fingers = new Fingers(26);
        List<FingerConfigurationData> fingerConfigurations = new ArrayList<>();
        
        do {
            double entropy = fingers.getEntropy();
            
            if (entropy < 0.0) {
                FingerConfigurationData data = 
                        new FingerConfigurationData(
                                Arrays.copyOf(
                                        fingers.getArray(), 
                                        fingers.getArray().length), 
                                entropy);
                
                fingerConfigurations.add(data);
            }
        } while (fingers.advanceFingers());
        
        return fingerConfigurations;
    }
    
    private static double runFingersTest(int listSize) {
        
        Fingers fingers = new Fingers(listSize);
        long theoreticalNumberOfFingerConfigurations = 
                fingers.getNumberOfTheoreticalFingerConfigurations();
        
        long actualNumberOfFingerConfigurations = 0L;
        double smallestEntropy = Double.MAX_VALUE;
        int[] smallestEntropyConfiguration = null;
        
        do {
            actualNumberOfFingerConfigurations++;
            
            double entropy = fingers.getEntropy();
            
            if (entropy >= 0.0) {
                continue;
            }
            
            if (smallestEntropy > entropy) {
                smallestEntropy = entropy;
                smallestEntropyConfiguration =
                        Arrays.copyOf(fingers.getArray(), 
                                      fingers.getArray().length);
            }
        } while (fingers.advanceFingers());
        
        if (actualNumberOfFingerConfigurations 
                != theoreticalNumberOfFingerConfigurations) {
            throw new IllegalStateException();
        }
        
        System.out.print(
                listSize 
                        + " & " 
                        + fingers.getArray().length 
                        + " & " 
                        +  " \\texttt{"
                        + Arrays.toString(smallestEntropyConfiguration) 
                        + "} & ");
        
        System.out.println(
                String.format("%.3f \\\\", smallestEntropy).replace(',', '.'));
        
//        System.out.println("--- Smallest entropy: " + smallestEntropy + ", list size = " + listSize);
//        System.out.println(
//                ">>> Smallest finger configuration: " 
//                        + Arrays.toString(smallestEntropyConfiguration));
        
        return smallestEntropy;
    }
    
    public static void main(String[] args) {
        
//        for (int listSize = 17; listSize <= 30; listSize++) {
//            worstEntropies(listSize);
//            System.out.println("----------------");
//        }
//        
//        System.out.println("Done!");
//        System.exit(0);
//        List<FingerConfigurationData> fingerConfigurations = 
//                getNegativeFingerConfigurations();
//        
//        Collections.sort(fingerConfigurations);
//        
//        for (FingerConfigurationData data : fingerConfigurations) {
//            System.out.println(data);
//        }
//        
//        Fingers fingersAux = new Fingers(1_000_000);
//        int[] conf = new int[1_000];
//        
//        for (int i = 0; i < 500; i++) {
//            conf[i] = i;
//        }
//        
//        for (int i = 0; i < 500; i++) {
//            conf[i + 500] = 1000_000 - 500 + i;
//        }
//        
//        for (int i = 0; i < conf.length; i++) {
//            fingersAux.getArray()[i] = conf[i];
//        }
//        
//        System.out.println(Arrays.toString(conf));
//        System.out.println("yeah = " + fingersAux.getEntropy());
//        System.out.println("worst case = " + getWorstEntropy(1_000_000));
//        
//        System.exit(0);
        

        List<Double> smallestEntropies = new ArrayList<>();
        
        for (int listSize = 17; listSize <= 50; listSize++) {
            double smallestEnropy = runFingersTest(listSize);
//            smallestEntropies.add(smallestEnropy);
//            System.out.println(smallestEnropy + " " + listSize);
        }
//        
//        System.out.println("--------");
//        
//        for (int i = 1; i < smallestEntropies.size(); i++) {
//            double ratio = smallestEntropies.get(i - 1) 
//                         / smallestEntropies.get(i );
//            
//            System.out.println(ratio);
//        }
        
        System.exit(0);
//        Fingers fingers2 =  new Fingers(5);
//        int column = 0;
//        
//        do {
//            System.out.println(
//                    String.format(
//                            "%-3d %f",  
//                            column, 
//                            fingers2.getEntropy()).replace(',', '.'));
//            column++;
//        } while (fingers2.advanceFingers());
//        
//        System.exit(0);
//        System.out.println("<<< Demo >>>");
        
        Fingers fingers = new Fingers(5);
        int actualFingerTuples = 0;
        List<Double> entropyList = new ArrayList<>();
        
        do {
            double entropy = fingers.getEntropy();
            entropyList.add(entropy);
            System.out.println(
                    Arrays.toString(fingers.getArray()) + ", " + entropy);
            
            actualFingerTuples++;
        } while (fingers.advanceFingers());
        
        System.out.println("Actual finger tuples: " + actualFingerTuples);
        System.out.println("Theoretical finger tuples: " +
                           fingers.getNumberOfFingerTuples());
        
        Collections.sort(entropyList);
        entropyList.forEach(System.out::println);
    }
    
    private static void worstEntropies(int listSize) {
        System.out.println("list size = " + listSize);
        Fingers fingers = new Fingers(listSize);
        List<FingerConfigurationData> data = new ArrayList<>();
        
        int n = fingers.getArray().length;
        
        for (int left = 1; left < n; left++) {
            for (int i = 0; i < left; ++i) {
                fingers.getArray()[i] = i;
            }
            
            for (int i = left; i < n; i++) {
                fingers.getArray()[i] = listSize - n + i;
            }
           
            double currentEntropy = fingers.getEntropy();
            
            data.add(
                    new FingerConfigurationData(
                            Arrays.copyOf(
                                    fingers.getArray(), 
                                    fingers.getArray().length), 
                            currentEntropy));
        }
        
        System.out.println("Total catches: " + data.size());
        
        for (FingerConfigurationData fcd : data) {
            System.out.println(fcd);
        }
        
        System.out.println("Theoretical entropy: " + getWorstEntropy(listSize));
    }
}

class Fingers {
    private final int[] fingers;
    private final int listSize;
    
    Fingers(int listSize) {
        int numberOfFingers = computeNumberOfFingers(listSize);
        this.listSize = listSize;
        this.fingers = new int[numberOfFingers];
        initializeFingers();
    }
    
    boolean advanceFingers() {
        for (int i = fingers.length - 1; i >= 0; i--) {
            if (fingers[i] - i + fingers.length < listSize) {
                fingers[i]++;
                
                for (int j = i + 1; j < fingers.length; j++) {
                    fingers[j] = fingers[j - 1] + 1;
                }
                
                return true;
            }
        }
        
        return false;
    }
    
    int[] getArray() {
        return fingers;
    }
    
    BigInteger getNumberOfFingerTuples() {
        BigInteger ls = BigInteger.valueOf(listSize);
        BigInteger f  = BigInteger.valueOf(fingers.length);
        
        BigInteger numerator = factorial(ls);
        BigInteger denominator = 
                factorial(f).multiply(factorial(ls.subtract(f)));
        
        return numerator.divide(denominator);
    }
    
    long getNumberOfTheoreticalFingerConfigurations() {
        BigInteger bigIntegerListSize = BigInteger.valueOf(listSize);
        BigInteger bigIntegerNumberOfFingers = 
                BigInteger.valueOf(fingers.length);
        
        BigInteger bigIntegerNumerator = factorial(bigIntegerListSize);
        BigInteger bigIntegerDenominator =
                factorial(bigIntegerNumberOfFingers)
                        .multiply(
                                factorial(
                                        bigIntegerListSize.subtract(
                                                bigIntegerNumberOfFingers)));
        
        return bigIntegerNumerator.divide(bigIntegerDenominator).longValue();
    }
    
    double getEntropy() {
        double sum = 0.0;
        
        for (int i = 0; i < fingers.length - 1; i++) {
            sum += Math.abs(fingers[i + 1] - fingers[i] - fingers.length);
        }
            
        return 1.0 - sum / listSize;
    }
    
    private void initializeFingers() {
        for (int i = 0; i < fingers.length; i++) {
            fingers[i] = i;
        }
    }
    
    private static int computeNumberOfFingers(int listSize) {
        return (int) Math.ceil(Math.sqrt(listSize));
    }
    
    private static BigInteger factorial(BigInteger n) {
        if (n.equals(BigInteger.ZERO) || n.equals(BigInteger.ONE)) {
            return BigInteger.ONE;
        }
        
        return n.multiply(factorial(n.subtract(BigInteger.ONE)));
    }
}
