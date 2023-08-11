import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.sound.sampled.*;
import javax.swing.JOptionPane;

public class PasswordManagerMain 
{
    @SuppressWarnings("static-access")
	public static void main(String args[]) 
    {
    	int echar;
    	String errorlog = "";
    	
    	File epath;
    	File elog = null;
    	FileReader fr;
    	FileWriter fw;
    	
    	DateTimeFormatter dtf;
    	LocalDateTime now;
    	
    	JOptionPane vexit = null;
    	
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
			else
			{
				fr=new FileReader(elog);
				while((echar=fr.read())!=-1)
				{
					errorlog = errorlog+(char)echar;
				}
				fr.close();
			}
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
    	
        PasswordSplash ps = new PasswordSplash();
        ps.startLoading();
        try {
            // Load the WAV file using getResourceAsStream
            InputStream inputStream = PasswordManagerMain.class.getResourceAsStream("/interface-welcome-trial-131917.wav");
            if (inputStream != null) {
                AudioInputStream ais = AudioSystem.getAudioInputStream(new BufferedInputStream(inputStream));
                Clip sound = AudioSystem.getClip();
                sound.open(ais);
                sound.start();
            } else {
                // Handle the case when the asset is not found.
                System.err.println("Error: WAV file not found.");
            }
        } catch (Exception e) {
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
				vexit.showMessageDialog(null, "Encountered and error while writing log. "+logerror.getMessage(), "Log Error", JOptionPane.ERROR_MESSAGE);
				logerror.printStackTrace();
			}
			e.printStackTrace();
        }
    }
}
