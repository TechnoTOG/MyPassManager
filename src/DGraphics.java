import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

@SuppressWarnings("serial")
public class DGraphics extends JPanel
{
	int pgbutton = 0,connection = 0,quality = 1,friends = -1,meme = 0,echar;
	int desiredWidth = 256;
    int desiredHeight = 256;
    String errorlog ="";
	
	String friendsimg[]= {"39c1ce13-495d-4039-8a8d-1b9f4f0748d6.png","5d95576b-86bb-4e62-b63d-61073c0b4908.png","bf0ac683-4049-4327-9ec9-54a9a4bbfe79.png","58fb1496-d904-44cd-90ce-944984d9c3a1.png",
						  "8b54b24f-2aa3-4e7b-ae50-6704899e8add.png","070b6b99-2398-4ab5-928a-45aff478a628.png","1e4b6c8f-dc9b-4cc6-bf08-18cd268d116d.png","e89006da-464a-40d4-aaca-755dd7ec97ae.png",
						  "95b674c0-6106-4b10-b678-e65c071669ae.png","d5c0743f-7b4e-4544-9fa1-35bc2b1a4e5c.png","50095e68-bc42-49a5-8f4d-778de9284ba1.png","597a4418-7416-4c44-a81e-af5f94a2a6ef.png",
						  "b9230509-2c29-41df-bfa4-7403b084c63d.png","dfa28108-8e0d-4c10-83b7-0a3f745bab61.png","904210f0-fab2-47f6-bf5a-9af75c1d987b.png","0aebc9c6-e17a-4766-8934-ded0e9e159a6.png",
						  "4a5a23f2-1326-4ca5-ad4c-222bc48350b6.gif","81f01c9b-73c9-4ed5-bd8b-3a9573964000.gif","d568c98d-e614-4f6a-a249-488b1bfbb381.png","0f977132-b119-4e80-8a6f-c257ab8ed347.png",
						  "77b36dbb-8df3-415e-9377-cfea4f37790c.gif","","94ae4587-fc2d-43a8-bb0e-eaee62431390.gif","b4dfda93-dafe-4567-9dbf-bfd75d715379.png",""};
	
	String memes[] = {"supermeme_22h24_36.png","supermeme_22h27_14.png","supermeme_22h27_20.png","supermeme_22h30_31.gif","supermeme_22h30_47.gif","supermeme_22h33_4.png","supermeme_22h35_12.gif","supermeme_22h36_44.png","supermeme_22h36_57.png"};
	
	InputStream sconnected = DGraphics.class.getResourceAsStream("/server connected 32px.png");
	InputStream sdisconnected = DGraphics.class.getResourceAsStream("/server disconnected 32px.png");
	
	JOptionPane vexit;
	
	File epath;
    File elog;
	FileReader fr;
	FileWriter fw;
	
	DateTimeFormatter dtf;
	LocalDateTime now;
	
	Image connected;
	Image disconnected;
	
	Image pop;
	
	DGraphics() throws IOException
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
		
		connected = ImageIO.read(sconnected);
		disconnected = ImageIO.read(sdisconnected);
		
		setDoubleBuffered(true);
		SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
		
		this.setBackground(new Color(0x2D4369));
		this.setForeground(null);
		this.setLayout(null);
		this.setPreferredSize(new Dimension(950,650));
	}
	@SuppressWarnings("static-access")
	public void paint(Graphics g)
	{
		super.paint(g);
		
		Graphics2D g2d = (Graphics2D) g;
		
		// Set rendering hints for better graphics quality
        if(quality == 0)
        {
        	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        }
        else if(quality == 1)
        {
        	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        }
        else if(quality == 2)
        {
        	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_DEFAULT);
        }
        else if(quality == 3)
        {
        	g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        }
		
		g2d.setPaint(new Color(0xEEA47F));
		//g2d.setStroke(new BasicStroke(1));
		g2d.drawLine(0, 65, 950, 65);
		g2d.drawLine(75, 65, 75, 650);
		g2d.fillOval(893,11,45,45);
		
		if(pgbutton == 1)
		{
			g2d.drawLine(540,85,540,350);
		}
		
		if(connection == 1)
		{
			g2d.drawImage(connected, 900, 18, null);		
		}
		else
		{
			g2d.drawImage(disconnected, 900, 18, null);
		}
		
		if(friends != -1)
		{
			pop = new ImageIcon(friendsimg[friends]).getImage();
			try 
			{
	            BufferedImage originalImage = ImageIO.read(DGraphics.class.getResourceAsStream("/friends/"+friendsimg[friends]));

	            g2d.drawImage(originalImage, 605, 85, desiredWidth, desiredHeight, null);
			}
			catch (IOException e) 
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
					vexit.showMessageDialog(this, "Encountered and error while writing log. "+logerror.getMessage(), "Log Error", JOptionPane.ERROR_MESSAGE);
					logerror.printStackTrace();
				}
				e.printStackTrace();
	        }
		}
		
		if(meme == 1)
		{
			try 
			{
				BufferedImage originalImage = ImageIO.read(DGraphics.class.getResourceAsStream("/meme/"+memes[(int)(Math.random() * memes.length) + 0]));
				
				 g2d.drawImage(originalImage, 605, 85, desiredWidth, desiredHeight, null);
			} 
			catch (IOException e) 
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
					vexit.showMessageDialog(this, "Encountered and error while writing log. "+logerror.getMessage(), "Log Error", JOptionPane.ERROR_MESSAGE);
					logerror.printStackTrace();
				}
				e.printStackTrace();
			}
		}
	}
}