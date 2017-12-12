
/*
* terminal_io.java
*
* Created on 30 May 2001, 21:44
*/
package jfreerails.lib;

/**
*  This class provides methods to read input from a terminal.
* @author  lindsal8
* @version 
*/
import java.io.*;
import java.text.*;


public class TerminalIO extends java.lang.Object {

    private InputStreamReader converter;

    private BufferedReader in;
    
    /** Creates new terminal_io */
    
    public TerminalIO() {
        converter = new InputStreamReader( System.in );
        in = new BufferedReader( converter );
    }
    
    public String my_read_line( java.lang.String prompt ) {
        System.out.println( prompt );
        String  text = null;
        try {
            text = in.readLine();
        }
        catch( Exception e ) {
            
        }
        return text;
    }
    
    public int my_read_int( java.lang.String prompt ) {
        System.out.println( prompt );
        String  text = null;
        int  i = 0;
        try {
            text = in.readLine();
            i = NumberFormat.getInstance().parse( text ).intValue();
        }
        catch( Exception e ) {
            
        }
        return i;
    }
    
    /**
    * @param args the command line arguments
    */
    
    public static void main( String args[] ) {
        TerminalIO  io = new TerminalIO();
        String  sti = io.my_read_line( "enter a string" );
        if( sti.equalsIgnoreCase( "aa" ) ) {
            System.out.println( sti );
        }
        int  in = io.my_read_int( "and an int" );
        System.out.println( in );
    }
}
