package edu.isi.bmkeg.pdf.text;

import java.io.*;
import java.util.Scanner;

/** 
 Read and write a file using an explicit encoding.
 Removing the encoding from this code will simply cause the 
 system's default encoding to be used instead.  
*/
public final class ReadWriteTextFileWithEncoding {

  /** Requires two arguments - the file name, and the encoding to use.  */
  public static void main(String... aArgs) throws IOException {
   
  }
  
 
  
  /** Write fixed content to the given file. */
  public static void  write(String fFileName, String fEncoding, String text) throws IOException  {

    Writer out = new OutputStreamWriter(new FileOutputStream(fFileName), fEncoding);
    try {
      out.write(text);
    }
    finally {
      out.close();
    }
  }
  
  /** Read the contents of the given file. */
  public static String read(String fFileName, String fEncoding) throws IOException {
    StringBuilder text = new StringBuilder();
    Scanner scanner = new Scanner(new FileInputStream(fFileName), fEncoding);
    try {
      while (scanner.hasNextLine()){
        text.append(scanner.nextLine());
      }
    }
    finally{
      scanner.close();
    }
    return text.toString();
  }
  

}
 
