import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.JOptionPane;

public class AdminBasicdb 
{
	int echar;
	
	String errorlog;
	
	JOptionPane tberror;
	Connection admin,basic,fbasic,vbasic;
	
	JOptionPane exit = new JOptionPane();
	
	File epath;
	File elog;
	FileReader fr;
	FileWriter fw;
	
	DateTimeFormatter dtf;
	LocalDateTime now;
	
	AdminBasicdb()
	{
		dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		
		try
		{
			File epath = new File("elog");
			if(epath.exists()==false)
			{
				epath.mkdir();
			}
			
			elog = new File("elog/elog.log");
			if(elog.exists()==false)
			{
				elog.createNewFile();
			}
			fr=new FileReader(elog);
			while((echar=fr.read())!=-1)
			{
				errorlog = errorlog+(char)echar;
			}
			fr.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("static-access")
	public void connectadmindb()
	{
		try 
		{
			Class.forName("oracle.jdbc.OracleDriver");
			admin = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","pman","mpm879");
		} 
		catch (ClassNotFoundException e) 
		{
			now = LocalDateTime.now();
			errorlog = errorlog+"\n"+dtf.format(now)+" Log: "+e.getMessage();
			try
			{
				fw = new FileWriter(elog);
				fw.write(errorlog);
				fw.close();
			}
			catch(IOException logerror)
			{
				exit.showMessageDialog(null, "Encountered and error while writing log. "+logerror.getMessage(), "Log Error", JOptionPane.ERROR_MESSAGE);
				logerror.printStackTrace();
			}
			e.printStackTrace();
		} 
		catch (SQLException e) 
		{
			tberror.showMessageDialog(null, e.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);
			now = LocalDateTime.now();
			errorlog = errorlog+"\n"+dtf.format(now)+" Log: "+e.getMessage();
			try
			{
				fw = new FileWriter(elog);
				fw.write(errorlog);
				fw.close();
			}
			catch(IOException logerror)
			{
				exit.showMessageDialog(null, "Encountered and error while writing log. "+logerror.getMessage(), "Log Error", JOptionPane.ERROR_MESSAGE);
				logerror.printStackTrace();
			}
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("static-access")
	public void firstconnectbasicdb(String username,String password)
	{
		try 
		{
			Class.forName("oracle.jdbc.OracleDriver");
			fbasic = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE",username,password);
		} 
		catch (ClassNotFoundException e) 
		{
			now = LocalDateTime.now();
			errorlog = errorlog+"\n"+dtf.format(now)+" Log: "+e.getMessage();
			try
			{
				fw = new FileWriter(elog);
				fw.write(errorlog);
				fw.close();
			}
			catch(IOException logerror)
			{
				exit.showMessageDialog(null, "Encountered and error while writing log. "+logerror.getMessage(), "Log Error", JOptionPane.ERROR_MESSAGE);
				logerror.printStackTrace();
			}
			e.printStackTrace();
		} 
		catch (SQLException e) 
		{
			tberror.showMessageDialog(null, e.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);
			now = LocalDateTime.now();
			errorlog = errorlog+"\n"+dtf.format(now)+" Log: "+e.getMessage();
			try
			{
				fw = new FileWriter(elog);
				fw.write(errorlog);
				fw.close();
			}
			catch(IOException logerror)
			{
				exit.showMessageDialog(null, "Encountered and error while writing log. "+logerror.getMessage(), "Log Error", JOptionPane.ERROR_MESSAGE);
				logerror.printStackTrace();
			}
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("static-access")
	public void connectbasicdb(String username,String password)
	{
		try 
		{
			Class.forName("oracle.jdbc.OracleDriver");
			vbasic = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE",username,password);
		} 
		catch (ClassNotFoundException e) 
		{
			now = LocalDateTime.now();
			errorlog = errorlog+"\n"+dtf.format(now)+" Log: "+e.getMessage();
			try
			{
				fw = new FileWriter(elog);
				fw.write(errorlog);
				fw.close();
			}
			catch(IOException logerror)
			{
				exit.showMessageDialog(null, "Encountered and error while writing log. "+logerror.getMessage(), "Log Error", JOptionPane.ERROR_MESSAGE);
				logerror.printStackTrace();
			}
			e.printStackTrace();
		} 
		catch (SQLException e) 
		{
			tberror.showMessageDialog(null, e.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);
			now = LocalDateTime.now();
			errorlog = errorlog+"\n"+dtf.format(now)+" Log: "+e.getMessage();
			try
			{
				fw = new FileWriter(elog);
				fw.write(errorlog);
				fw.close();
			}
			catch(IOException logerror)
			{
				exit.showMessageDialog(null, "Encountered and error while writing log. "+logerror.getMessage(), "Log Error", JOptionPane.ERROR_MESSAGE);
				logerror.printStackTrace();
			}
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("static-access")
	public boolean vconnectbasicdb(String username,String password)
	{
		int connect = 0;
		try 
		{
			Class.forName("oracle.jdbc.OracleDriver");
			basic = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE",username,password);
			connect = 1;
		} 
		catch (ClassNotFoundException e) 
		{
			connect = 0;
			now = LocalDateTime.now();
			errorlog = errorlog+"\n"+dtf.format(now)+" Log: "+e.getMessage();
			try
			{
				fw = new FileWriter(elog);
				fw.write(errorlog);
				fw.close();
			}
			catch(IOException logerror)
			{
				exit.showMessageDialog(null, "Encountered and error while writing log. "+logerror.getMessage(), "Log Error", JOptionPane.ERROR_MESSAGE);
				logerror.printStackTrace();
			}
			e.printStackTrace();
		} 
		catch (SQLException e) 
		{
			tberror.showMessageDialog(null, e.getMessage(), "Error.", JOptionPane.ERROR_MESSAGE);
			now = LocalDateTime.now();
			errorlog = errorlog+"\n"+dtf.format(now)+" Log: "+e.getMessage();
			try
			{
				fw = new FileWriter(elog);
				fw.write(errorlog);
				fw.close();
			}
			catch(IOException logerror)
			{
				exit.showMessageDialog(null, "Encountered and error while writing log. "+logerror.getMessage(), "Log Error", JOptionPane.ERROR_MESSAGE);
				logerror.printStackTrace();
			}
			e.printStackTrace();
		}
		
		if(connect == 1)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
}