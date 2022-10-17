/**
 * TCSS 342
 * Instructor: Paulo Barretto
 * Assignment 3 - Main.java
 */

//import java.io.*;

import java.io.*;
import java.util.*;

/**
 * can compress a number of files and includes methods used 
 * to test components of your program.
 *
 * @author Angelynna Pyeatt
 * @author Amtoj Kaur
 * @author Khoi Nguyen
 * @version May 6 2022
 */

public class Main {

    /**
     * this method  will carry out compression of a file using the
     * CodingTree class. It will:
     * Read in from a text file. The input file is to be passed as
     * a command line argument, becoming available to your program
     * as args[0].
     * Output to two files. Again, feel free to hard code the names
     * of these files into your program. These are the codes text
     * file and the compressed binary file.
     * Display statistics. You must output the original size (in
     * kibibytes), the compressed size (in kibibytes), the compression
     * ratio (as a percentage) and the elapsed time for compression.
     *
     * @param theArgs
     * @throws Exception
     */
    public static void main(String[] theArgs) {
        //testCodingTree
        File input = new File(theArgs[0]);
        File compressed = new File("compressed.txt");

        //start timing for runtime
        long start = System.currentTimeMillis();

        //compress and encode
        compress(input, compressed);

        //stop timing for runtime
        long stop = System.currentTimeMillis();
        double time = (double) stop - start;


        System.out.println(String.format("Runtime: %.3f ms", time));
        System.out.println(String.format("Orginal file size: %d bits",
                input.length() * 8));
        System.out.println(String.format("Compressed file size: %d bits",
                compressed.length() * 8));
        System.out.println(String.format("Compression ratio: %.2f%%",
                (double) compressed.length() / input.length() * 100));


    }

    /**
     * this method tests the coding tree with a test file
     */
    public static void testCodingTree() {
        File input = new File("BeeMovie.txt");
        File compressed = new File("compressed.txt");
        //start timing for runtime
        long start = System.currentTimeMillis();

        //compress and encode
        compress(input, compressed);

        //stop timing for runtime
        long stop = System.currentTimeMillis();
        double time = (double) stop - start;


        System.out.println(String.format("Runtime: %.3f ms", time));
        System.out.println(String.format("Orginal file size: %d bits",
                input.length() * 8));
        System.out.println(String.format("Compressed file size: %d bits",
                compressed.length() * 8));
        System.out.println(String.format("Compression ratio: %.2f%%",
                (double) compressed.length() / input.length() * 100));
    }

    //private helpers:
    /**
     * method fills compressed.txt
     *
     * @param input : File
     * @param output : File
     */
    private static void compress(File input, File output) {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        try {
            br = new BufferedReader(new FileReader(input));

            while(br.ready()) {
                sb.append((char) br.read());
            }
            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        CodingTree ct = new CodingTree(sb.toString());
        fillCodes(ct, sb);
        String encoded = encodeText(ct, sb);
    }

    /**
     * finishes filling compressed.txt
     *
     * @param ct : CodingTree
     * @param sb : StringBuilder
     * @return str.toString() : String
     */
    private static String encodeText(CodingTree ct, StringBuilder sb) {
        String str = ct.encodeText(sb);

        //convert length of string to bytes
        int bytes = str.length() / 8;
        ArrayList<Integer> bin = new ArrayList<Integer>();
        String iS;

        for(int i = 0; i < bytes; i++) {
            iS = str.substring((i*8), (i+1) * 8);
            bin.add(Integer.parseUnsignedInt(iS, 2));
        }

        //write to compressed.txt
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new
                    FileOutputStream("compressed.txt"));
            for(int i : bin) {
                bos.write(i);
            }
            bos.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return str.toString();
    }

    /**
     * method fills codes.txt
     *
     * @param ct : CodingTree
     * @param sb : StringBuilder
     */
    private static void fillCodes(CodingTree ct, StringBuilder sb) {
        StringBuilder codes = new StringBuilder();
        codes.append("{");

        //write to codes.txt
        try {
            BufferedWriter b = new
                    BufferedWriter(new FileWriter("codes.txt"));
            for(Character code : ct.codes.keySet()) {
                if(code != 'z') {
                    codes.append(code + "=" + ct.codes.get(code)
                            + ", ");
                } else {
                    codes.append(code + "=" + ct.codes.get(code));
                }

            }
            codes.append("}");
            b.write(codes.toString());
            b.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}