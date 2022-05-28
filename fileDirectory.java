package locker.lockedme.com;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList ;
import java.util.Iterator; ;

public class fileDirectory //extends ArrayList<File>
{
	ArrayList<File> directory_list = new ArrayList<File>() ;
	String RootDir = "." ;
	boolean case_sensitive = false ;
	public void setCaseSensitive( ) { setCaseSensitive( true ) ; } 
	public void setCaseSensitive( boolean sense  ) 
	{
		case_sensitive = sense ;
	}
	public void LoadDirectoryList( ) 
	{
			  // try-catch block to handle exceptions
	    try 
	    {
	    	OpenDataDirectory() ;
	        // Create a file object
	        File f = new File( RootDir + "/Data" );
	        File[] files = f.listFiles();
	        for (int i = 0; i < files.length; i++) 
	        {
	          	if ( files[i].isDirectory()  == false )  
	        	{
	        		directory_list.add( files[i] ) ;
	        	}
	        }
	    }
	        
	    catch (Exception e) 
	    {
	    	System.err.println(e.getMessage());
	    }
	}
	void OpenDataDirectory() 
	{
		File f = new File( RootDir + "/Data" ) ;
		if( !f.exists()) 
		{
			f.mkdirs(  ) ;
		}
	}
	int getIndex( String filename )
	{
		File f = null  ;
		int i= 0  ;
		int return_value  = -1 ;
			
		Iterator<File> iter =  directory_list.iterator();;
		while( iter.hasNext()) 
		{
			f = iter.next( )  ;
			String FileName =  f.getName();
			String  stringa ,stringb ;
			if( case_sensitive == true )
			{
				stringa = f.getName()  ; stringb = filename  ;
			} else 
			{
				stringa = f.getName().toUpperCase() ;
				stringb = filename.toUpperCase() ; 
			}
			if(  stringa.equals(stringb ) )
			{
				return_value  =    i ;
			} 
			++i ;
		}
		if ( f == null ) return_value = -1 ;
		return return_value  ;
	}
	boolean findFile( String filename )
	{
		if( getIndex( filename) > -1 )
		{
			return true ;
		} else return false ;
		
	}
	File getFile( String filename )
	{
		int index = getIndex( filename ) ;
		if ( index > -1 )
			return directory_list.get( index ) ;
		return null ;			
	}
	void newFile( String filename ) 
	{
		File f = new File( RootDir + "/Data/" + filename ) ;
		try { f.createNewFile() ;	}
		catch( IOException e ) 	{ System.out.println( "IOException: "+ e.getMessage()) ; }
	}
}
