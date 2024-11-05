import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class SecretSharing {

    // Method to convert a value from a specified base to decimal
    public static long decodeValue(String value, int base) {
        long result = 0;
        for (char ch : value.toCharArray()) {
            result = result * base + (Character.isDigit(ch) ? ch - '0' : ch - 'a' + 10);
        }
        return result;
    }

    // Calculate the product of (x - xi) / (xj - xi) for all i != j
    public static double calculateLagrangeTerm(List<int[]> points, int j, int k) {
        double result = 1;
        for (int i = 0; i < k; i++) {
            if (i != j) {
                result *= points.get(i)[0];
                result /= (points.get(j)[0] - points.get(i)[0]);
            }
        }
        return result;
    }

    // Calculate the constant term using Lagrange Interpolation
    public static long calculateConstantTerm(List<int[]> points, int k) {
        double constant = 0;
        for (int j = 0; j < k; j++) {
            double term = points.get(j)[1] * calculateLagrangeTerm(points, j, k);
            constant += term;
        }
        return Math.round(constant);
    }

    public static void main(String[] args) {
        try {
            // Read JSON input from file
            JSONParser parser = new JSONParser();
            JSONObject root = (JSONObject) parser.parse(new FileReader("input.json"));
            
            JSONObject keys = (JSONObject) root.get("keys");
            int n = ((Long) keys.get("n")).intValue();
            int k = ((Long) keys.get("k")).intValue();
            List<int[]> points = new ArrayList<>();

            // Parse JSON and decode values
            for (Object key : root.keySet()) {
                if (key.equals("keys")) continue;

                int x = Integer.parseInt((String) key);
                JSONObject data = (JSONObject) root.get(key);
                int base = Integer.parseInt((String) data.get("base"));
                String value = (String) data.get("value");
                
                long y = decodeValue(value, base);
                points.add(new int[] {x, (int) y});
            }

            // Calculate the constant term (c) using Lagrange interpolation
            long constantTerm = calculateConstantTerm(points, k);
            System.out.println("The constant term (c) is: " + constantTerm);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
