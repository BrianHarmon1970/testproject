package locker.lockedme.com;
import java.util.ArrayList ;
import java.io.File; 
import java.util.Scanner ;
import java.util.InputMismatchException ; 

abstract class UI_ActionElement
{
	private String ActionDescription = "" ;
	UI_ActionElement( String description ) 
	{
		ActionDescription = description ;
	}
	void SetDescription( String str ) 
	{
		ActionDescription = str ;
	}
	String getActionDescription() 
	{
		return ActionDescription ;
	}
	abstract boolean Action() ;
}

class UI_Input
{
	Scanner scanner = new Scanner( System.in ) ;
	String input_prompt  ;
	int promptInteger( String prompt )  
	{ 
		int return_value = 0 ;
		input_prompt = prompt ;
		System.out.print( input_prompt ) ;
		do	
		{		
			return_value =  getInt() ;
		} while ( !(return_value >=  0) ) ;
		return return_value ;
	}
	int getInt() 
	{
		int return_value = 0 ;
		try { return_value = scanner.nextInt(); }
		catch( InputMismatchException e ) { 
			return_value = -1 ;
			scanner = new Scanner( System.in ) ;
			System.out.print( input_prompt ); 
		}
		return return_value ;
	}
	String promptString( String prompt )
	{
		System.out.print( prompt )  ; return scanner.nextLine() ;
	}
}

class UI_Screen extends UI_ActionElement
{
	String ScreenHeader ;
	String SubHead ;
	ArrayList<UI_ActionElement> ElementList ;
	UI_Input user_input ;
	UI_Screen parentScreen = null ;
	
	public UI_Screen(String header, String shead )
	{
		super("") ; 
		ScreenHeader = new String( header ) ;
		SubHead = new String( shead ) ;
		ElementList = new ArrayList<UI_ActionElement>() ;
	}
	public UI_Screen( String header, String shead, String actionDescription ) {
		super( actionDescription) ;

		ScreenHeader = new String( header ) ;
		SubHead = new String( shead ) ;
		ElementList = new ArrayList<UI_ActionElement>() ;
	}
	public void addActionItem( UI_ActionElement e ) 
	{
		ElementList.add( e ) ;
		if ( e instanceof UI_Screen)
		{
			((UI_Screen)e).parentScreen = this ;
		}
	}
	void Display() 
	{
		System.out.println( "===============================================") ;
		System.out.println( ScreenHeader ) ;
		System.out.println( SubHead ) ;
		System.out.println( "===============================================") ;
		System.out.println("") ;
		
		for ( int i = 0 ; i < ElementList.size() ; ++i ) 
		{
			System.out.println( i+1 + ". " + ElementList.get(i).getActionDescription()) ;
		}
	}
	boolean SelectItem( int itemNumber ) 
	{
		return ElementList.get( itemNumber-1 ).Action() ;
	}
	boolean Action()
	{
		
		start( user_input ) ;
		parentScreen.Display() ;
		return true ;
	}
	void start( UI_Input uin ) 
	{
		user_input = uin ;
		int ndx = 0 ;
		Display() ; 
		do	{
			do { 
					ndx = uin.promptInteger("Selection>") ;
				} while ( ndx > ElementList.size() || ndx < 1 )  ;
			}	while ( SelectItem(  ndx ))  ;
	}
}

class ActionFindFile extends UI_ActionElement
{
	ActionFindFile( String description ) 
	{
		super(description) ;
	}
	
	boolean Action() 
	{
		UI_Input uin = new UI_Input() ;
		fileDirectory dirList ;
		dirList = new fileDirectory() ;
		dirList.LoadDirectoryList();
		dirList.setCaseSensitive();
		String searchName = uin.promptString( "Name> " ) ;
		
		if ( dirList.findFile(searchName)) 
			System.out.println( searchName + ": file found" ) ;
		else System.out.println( searchName + ": file not found" ) ;
		
		return true ; 
	}
}

class ActionDeleteFile extends UI_ActionElement
{
	ActionDeleteFile( String description ) 
	{
		super(description) ;
	}
	
	boolean Action() 
	{
		UI_Input uin = new UI_Input() ;
		fileDirectory dirList ;
		dirList = new fileDirectory() ;
		dirList.LoadDirectoryList();
		dirList.setCaseSensitive();
		String searchName = uin.promptString( "Name> " ) ;
		
		if ( dirList.findFile(searchName))
		{
			File f = dirList.getFile( searchName ) ;
			f.delete() ;
			System.out.println( searchName + ": file deleted" ) ;
		}
			
		else System.out.println( searchName + ": file not found" ) ;
		
		return true ; 
	}
}

class ActionDirectoryList extends UI_ActionElement 
{
	fileDirectory dirList ;
	ActionDirectoryList( String description ) 
	{
		super(description) ;
	}
	boolean Action() 
	{
		System.out.println( "\nDirectory List " ) ;
		dirList = new fileDirectory() ;
		dirList.LoadDirectoryList();
		for ( int  i = 0 ; i < dirList.directory_list.size() ; ++i ) 
		{
			System.out.println( dirList.directory_list.get(i).getName()) ;
		}
		return true ;	
	}
}

class ActionNewFile extends UI_ActionElement
{
	ActionNewFile( String description ) 
	{
		super( description ) ;
	}
	boolean Action()
	{
		UI_Input uin = new UI_Input() ;
		fileDirectory dirList = new fileDirectory() ;
		dirList.LoadDirectoryList();
		String newfilename = uin.promptString("New file> " ) ;
		dirList.newFile(newfilename);
			
		return true ;
	}
}

class ActionExit extends UI_ActionElement
{
	ActionExit( String description ) 
	{
		super(description) ;
	}
	boolean Action() 
	{
		return false ;
	}	
}

public class UserInterface 
{
	UI_Screen ui_screen  ;
	UI_Input ui_input ;
	
	public static void main( String[] args ) 
	{
		UI_Screen welcome = new UI_Screen( "LOCKER PVT LTD - LocekdMe.com",	"Brian Harmon - Harmon Engineering LLC" ) ;
		UI_Screen submenu = new UI_Screen( "Business Operations", 
											"Brian Harmon",	
											"Business Ops");
		UI_Input uin = new UI_Input() ;

		submenu.user_input = uin ;
		submenu.addActionItem( new ActionNewFile("Create File"));
		submenu.addActionItem( new ActionDeleteFile("Delete File"));
		submenu.addActionItem( new ActionFindFile("Find file")) ;
		submenu.addActionItem( new ActionExit("Return to Main" )) ;
		
		welcome.addActionItem( new ActionDirectoryList("Directory List" ));
		welcome.addActionItem( submenu );
		welcome.addActionItem( new ActionExit("Exit " ));
		
		welcome.start( uin ) ; // run user input
		System.out.println( "Program Exited.") ;
	}
}
	
