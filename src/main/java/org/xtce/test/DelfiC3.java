package org.xtce.test;

import java.io.File;
import java.util.BitSet;
import java.util.List;
import org.xtce.toolkit.XTCEContainerContentEntry;
import org.xtce.toolkit.XTCEContainerContentModel;
import org.xtce.toolkit.XTCEContainerEntryValue;
import org.xtce.toolkit.XTCEDatabase;
import org.xtce.toolkit.XTCEFunctions;
import org.xtce.toolkit.XTCESpaceSystem;
import org.xtce.toolkit.XTCETMContainer;

/**
 * Example Delfi-C3 XTCE telemetry extractor
 *
 */
public class DelfiC3 
{
    
    public static void main( String[] args ) 
    {
        
        byte[] hk = new byte[] {(byte)0xA8, (byte)0x98, (byte)0x9A, (byte)0x40, (byte)0x40, (byte)0x40, 
				(byte)0x00, (byte)0x88, (byte)0x98, (byte)0x8C, (byte)0x92, (byte)0x86, 
				(byte)0x66, (byte)0x01, (byte)0x03, (byte)0xF0, (byte)0xE1, (byte)0x08, 
				(byte)0xFA, (byte)0x01, (byte)0xDE, (byte)0x84, (byte)0xF4, (byte)0xFF, 
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, 
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, 
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0xFF, (byte)0x3F, 
				(byte)0x97, (byte)0x96, (byte)0x55, (byte)0x00, (byte)0x00, (byte)0x1F, 
				(byte)0xB6, (byte)0xC0, (byte)0x00, (byte)0x20, (byte)0x02, (byte)0x00, 
				(byte)0x7E, (byte)0x3C, (byte)0x76, (byte)0x07, (byte)0x00, (byte)0xD5, 
				(byte)0x00, (byte)0x80, (byte)0x02, (byte)0x00, (byte)0xD4, (byte)0x03, 
				(byte)0x40, (byte)0x18, (byte)0x00, (byte)0x90, (byte)0x01, (byte)0x00, 
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, 
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, 
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, 
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, 
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, 
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, 
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, 
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x5E, (byte)0x7F, 
				(byte)0x0A, (byte)0x8D};
								
	byte[] p = new byte[]  {(byte)0xA8, (byte)0x98, (byte)0x9A, (byte)0x40, (byte)0x40, (byte)0x40, 
				(byte)0x00, (byte)0x88, (byte)0x98, (byte)0x8C, (byte)0x92, (byte)0x86, 
				(byte)0x66, (byte)0x01, (byte)0x03, (byte)0xF0, (byte)0xE1, (byte)0x08, 
				(byte)0xF7, (byte)0x01, (byte)0x01, (byte)0x00, (byte)0x00, (byte)0x00, 
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, 
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, 
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, 
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, 
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, 
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, 
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, 
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x01, 
				(byte)0x00, (byte)0x01, (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x01, 
				(byte)0x00, (byte)0x01, (byte)0x00, (byte)0x01, (byte)0x00, (byte)0x01, 
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0xA5, (byte)0xB2, (byte)0x00, 
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x5E, (byte)0x7F, (byte)0x0A, 
				(byte)0x8A, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, 
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, 
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00, 
				(byte)0x00, (byte)0x00, (byte)0x00, (byte)0x00};
    	
        String file = "Delfi-C3.xml";
        
        try 
    	{
            System.out.println( "Loading " + file + " database" );

            XTCEDatabase db_ = new XTCEDatabase( new File( file ), true, false, true );
            
            String containerName = "/Delfi-C3/Downlink";
                        
            XTCESpaceSystem ss = db_.getSpaceSystem( "Delfi-C3" );
            XTCETMContainer container = db_.getContainer( containerName );
            BitSet bits  = XTCEFunctions.getBitSetFromStreamByteArray( p );
        
            XTCEContainerContentModel model = db_.processContainer( container, bits );
                
            List<XTCEContainerContentEntry> entries = model.getContentList();

            for ( XTCEContainerContentEntry entry : entries ) 
            {
            	System.out.print(entry.getName());
                
                XTCEContainerEntryValue val = entry.getValue();
                
                if (val == null)
                {
                    System.out.println();
                }
                else
                {
                    System.out.println(": " + val.getCalibratedValue()+ " " + entry.getParameter().getUnits());
                }
            }
            System.out.println();
            
        } catch ( Throwable ex ) 
        {
            ex.printStackTrace();
        }        
    }
}
