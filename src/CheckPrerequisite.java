import java.io.File;
import javax.swing.JOptionPane;

public class CheckPrerequisite 
{
	int warning = 0;
	String username = System.getProperty("user.name"),loc ="C:/app/"+username+"/product/21c/dbhomeXE";
	boolean verified;
	
	File checkdb = new File(loc);
	
	JOptionPane ask;
	
	@SuppressWarnings("static-access")
	CheckPrerequisite()
	{
		check();
		while(verified==false)
		{
			loc = ask.showInputDialog("Please Enter the Directory/Location of Oracle 21c XE (if installed)");
			loc = loc+"\\app/"+username+"/product/21c/dbhomeXE";
			if(loc.equals("\\app/"+username+"/product/21c/dbhomeXE"))
			{
				warning++;
			}
			else
			{
				checkdb = new File(loc);
				check();
			}
			
			if(warning>=4)
			{
				ask.showMessageDialog(null, "Please install Oracle DB 21c XE or higher", "Exiting", JOptionPane.OK_OPTION);
				verified = false;
				System.exit(0);
			}
		}
	}
	
	public void check()
	{
		if(checkdb.exists()==true)
		{
			verified = true;
		}
		else
		{
			verified = false;
			warning++;
		}
	}
}
