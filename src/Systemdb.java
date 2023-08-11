import java.awt.HeadlessException;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

public class Systemdb 
{
	int nullcount = 0,echar;
	String errorlog = "";
	
	Connection system;
	
	private JOptionPane sqlpass = new JOptionPane();
	JOptionPane sqlerror = new JOptionPane();
	JOptionPane exit = new JOptionPane();
	
	InputStream serror = Systemdb.class.getResourceAsStream("/server-error.png");
	
	InputStream verrorsound;
	AudioInputStream vais;
	Clip vsound;
	
	File epath;
	File elog;
	FileReader fr;
	FileWriter fw;
	
	DateTimeFormatter dtf;
	LocalDateTime now;
	
	@SuppressWarnings("static-access")
	public void connectsystemdb(String password) throws HeadlessException, IOException
	{
		dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		
		try
		{
			epath = new File("elog");
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
		
		try
		{
			verrorsound = Systemdb.class.getResourceAsStream("/system-error-notice-trial-132471.wav");
			vais = AudioSystem.getAudioInputStream(verrorsound);
			vsound = AudioSystem.getClip();
			vsound.open(vais);
		}
		catch(Exception e)
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
		
		try 
		{
			Class.forName("oracle.jdbc.OracleDriver");
			system = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","system",password);
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
			now = LocalDateTime.now();
			errorlog = errorlog+dtf.format(now)+" Log (DB: System): "+e.getMessage()+"\n";
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
			
			vsound.start();
			nullcount++;
			String[] opt= {"OK"};
			sqlerror.showOptionDialog(null, e.getMessage(), "SQL Error",JOptionPane.OK_OPTION, JOptionPane.INFORMATION_MESSAGE, new ImageIcon(ImageIO.read(serror)), opt, null);
			e.printStackTrace();
			password = sqlpass.showInputDialog(null, "Invalid password. Enter the correct password for Oracle DB account system.");
			
			if(nullcount==2)
			{
				exit.showMessageDialog(null, "Exiting from the application due to multiple empty/worng password.", "Exiting", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
			connectsystemdb(password);
		}
	}
	
	@SuppressWarnings("static-access")
	public void closeconnection()
	{
		try 
		{
			system.close();
		} 
		catch (SQLException e) 
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
	}
}