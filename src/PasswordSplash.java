import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.awt.*;
import javax.swing.*;
import javax.imageio.ImageIO;

@SuppressWarnings("serial")
public class PasswordSplash extends JWindow {
    int level = 0,echar;
    String errorlog = "";

    File epath;
	File elog;
	FileReader fr;
	FileWriter fw;
	
	DateTimeFormatter dtf;
	LocalDateTime now;
	
	JOptionPane exit;
    
    Image psi;
    ImageIcon psii;
    JProgressBar jpb = new JProgressBar();
    JPanel pbar = new JPanel();

    private File chkfile = new File("dat/fmen.mps");
    LoginForm lf;
    FirstUser fu;
    CheckPrerequisite cp;

    @SuppressWarnings("static-access")
	public PasswordSplash() {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
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
        
        // Load the image using getResourceAsStream and ImageIO
        try (InputStream imageStream = PasswordSplash.class.getResourceAsStream("/My PassManager.png")) {
            psi = ImageIO.read(imageStream);
            psii = new ImageIcon(psi);
            setSize(psii.getIconWidth(), psii.getIconHeight());
        } catch (IOException e) {
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
				exit.showMessageDialog(this, "Encountered and error while writing log. "+logerror.getMessage(), "Log Error", JOptionPane.ERROR_MESSAGE);
				logerror.printStackTrace();
			}
			e.printStackTrace();
        }

        setLocationRelativeTo(null);

        setVisible(true);

        jpb.setStringPainted(true);
        jpb.setPreferredSize(new Dimension(getSize().width, 20));
        jpb.setForeground(Color.BLUE);
        jpb.setBackground(Color.WHITE);

        pbar.setLayout(new BorderLayout());
        pbar.add(jpb, BorderLayout.SOUTH);

        getContentPane().add(pbar);

        setVisible(true);
    }

    public void updateProgressBar(int progress) {
        jpb.setValue(progress);
    }

    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(psi, 0, 0, this);
    }

    @SuppressWarnings("static-access")
	public void startLoading() {
        Thread tpbar = new Thread(() -> {
            try 
            {
                for (int i = 0; i <= 100; i++) 
                {
                    if (i == 100) 
                    {
                        if (chkfile.exists()) 
                        {
                            lf = new LoginForm();
                        } 
                        else 
                        {
                        	cp = new CheckPrerequisite();
							if(cp.verified==true)
							{
								fu = new FirstUser();
							}
							else
							{
								System.exit(0);
							}
                        }
                    }
                    updateProgressBar(i);
                    Thread.sleep(55);
                }
                dispose();
            } 
            catch (Exception e) 
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
    				exit.showMessageDialog(this, "Encountered and error while writing log. "+logerror.getMessage(), "Log Error", JOptionPane.ERROR_MESSAGE);
    				logerror.printStackTrace();
    			}
    			e.printStackTrace();
            }
        });
        tpbar.start();
    }
}
