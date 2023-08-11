import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URISyntaxException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SuppressWarnings("serial")
public class PassManagerUI extends JFrame implements ActionListener
{
	private int dbuchar,dbpchar,dbconnection,curerror,newerror,conerror,echar,sbselected = 1,pbselected = 0,quality = 1,trigger = 0;
	private String dbuser = "",dbpass = "",errorlog,searchdata;
	private boolean hascondition = false;
	
	private File login;
	private FileReader dbu,dbp;
	
    private Connection admin,basic;
    private Statement runadminquery,runbasicquery;
    
    String cap[] = {"8","9","10","11","12","13","14","15"};
    
    File epath;
    File elog;
	FileReader fr;
	FileWriter fw;
	
	DateTimeFormatter dtf;
	LocalDateTime now;
    
    LoginForm lf;
    DGraphics dg;
    PassChange pcf = new PassChange();
    
    JOptionPane vexit = new JOptionPane();
    
    RoundedBorder rb = new RoundedBorder(10);
    
	JFrame aboutframe = new JFrame();
	
	JLabel curpass = new JLabel("Current Password:");
	JLabel newpass = new JLabel("New Password:");
	JLabel conpass = new JLabel("Confirm Password:");
	JLabel changepasserror = new JLabel("Enter a Value");
	JLabel welcometext = new JLabel("Welcome User,let's protect some passwords.");
	JLabel connection = new JLabel();
	JLabel shead = new JLabel("Save/Search Password");
	JLabel ghead = new JLabel("Password Generator");
	JLabel userlabel = new JLabel("Username/E-mail",SwingConstants.CENTER);
	JLabel passwordlabel = new JLabel("Password",SwingConstants.CENTER);
	JLabel awnlabel = new JLabel("App/Website name",SwingConstants.CENTER);
	JLabel pgsizetext = new JLabel("Password Size");
	
	JTextField curbox = new JTextField();
	JTextField newbox = new JTextField();
	JTextField conbox = new JTextField();
	JTextField username = new JTextField();
	JTextField password = new JTextField();
	JTextField awn = new JTextField();
	JTextField passgen = new JTextField();
	
	JCustomCheckBox caps = new JCustomCheckBox("Characters A - Z");
	JCustomCheckBox small = new JCustomCheckBox("Characters a - z");
	JCustomCheckBox numbers = new JCustomCheckBox("Numbers 0 - 9");
	JCustomCheckBox special = new JCustomCheckBox("Special Characters");
	
	RoundedCornerComboBox<Integer> pgsize = new RoundedCornerComboBox<>();
	
	CustomTableModel displaytable = new CustomTableModel();
	JTable display = new JTable(displaytable);
	JScrollPane displaypanel = new JScrollPane(display);
	ResultSet result;
	Object[] rowData;
	
	JButton savebutton = new JButton();
	JButton passgenbutton = new JButton();
	
	JRoundedButton ccan = new JRoundedButton("Cancel");
	JRoundedButton cok = new JRoundedButton("Ok");
	JRoundedButton aok = new JRoundedButton("Ok");
	JRoundedButton dsave = new JRoundedButton("Save");
	JRoundedButton dsearch = new JRoundedButton("Search");
	JRoundedButton d_delete = new JRoundedButton("Delete");
	JRoundedButton dclear = new JRoundedButton("Clear");
	JRoundedButton generate = new JRoundedButton("Genarate");
	
	JTextPane aboutwin = new JTextPane();
	JScrollPane aboutpane = new JScrollPane(aboutwin);
	
	JOptionPane eexit;
    
	JMenuBar jmu = new JMenuBar();
	JMenu app = new JMenu("App");
	JMenu user = new JMenu("User");
	JMenu graphics = new JMenu("Graphics");
	
	JMenuItem save = new JMenuItem("Save Changes");
	JMenuItem off = new JMenuItem("Off");
	JMenuItem low = new JMenuItem("Low");
	JMenuItem balance = new JMenuItem("Balanced");
	JMenuItem high = new JMenuItem("High");
	JMenuItem reconnect = new JMenuItem("Reconnect");
	JMenuItem disconnect = new JMenuItem("Disconnect");
	JMenuItem exit = new JMenuItem("Exit");
	JMenuItem changepass = new JMenuItem("Change Password");
	JMenuItem logout = new JMenuItem("Logout");
	JMenuItem about = new JMenuItem("About");
	
	InputStream ico = PassManagerUI.class.getResourceAsStream("/locked.png");
	InputStream aboutico = PassManagerUI.class.getResourceAsStream("/about.png");
	InputStream bsave = PassManagerUI.class.getResourceAsStream("/social-engineering 64px.png");
	InputStream gp = PassManagerUI.class.getResourceAsStream("/padlock 64px.png");
	
	
	@SuppressWarnings({ "static-access" })
	PassManagerUI() throws IOException
	{
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
		
		try 
		{
			dbu = new FileReader("dat/utemp.temp");
			dbp = new FileReader("dat/ptemp.temp");
		} 
		catch (FileNotFoundException e) 
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
		identify();
		dg = new DGraphics();
		connect(dbuser,dbpass);
		
		curbox.setBounds(200,15,120,20);
		curbox.setForeground(new Color(0xeea47f));
		curbox.setBackground(new Color(0x656470));
		curbox.setBorder(new LineBorder(new Color(0x3D3D3D),1));
		curbox.setFont(new Font("Prompt",Font.PLAIN,12));
		
		newbox.setBounds(200,55,120,20);
		newbox.setForeground(new Color(0xeea47f));
		newbox.setBackground(new Color(0x656470));
		newbox.setBorder(new LineBorder(new Color(0x3D3D3D),1));
		newbox.setFont(new Font("Prompt",Font.PLAIN,12));
		
		conbox.setBounds(200,95,120,20);
		conbox.setForeground(new Color(0xeea47f));
		conbox.setBackground(new Color(0x656470));
		conbox.setBorder(new LineBorder(new Color(0x3D3D3D),1));
		conbox.setFont(new Font("Prompt",Font.PLAIN,12));
		
		curpass.setBounds(40,15,120,20);
		curpass.setForeground(new Color(0xeea47f));
		curpass.setFont(new Font("Prompt",Font.PLAIN,12));
		
		newpass.setBounds(40,55,100,20);
		newpass.setForeground(new Color(0xeea47f));
		newpass.setFont(new Font("Prompt",Font.PLAIN,12));
		
		conpass.setBounds(40,95,120,20);
		conpass.setForeground(new Color(0xeea47f));
		conpass.setFont(new Font("Roboto",Font.PLAIN,12));
		
		changepasserror.setBounds(160,125,120,20);
		changepasserror.setForeground(new Color(0x2D4369));
		changepasserror.setFont(new Font("Prompt", Font.PLAIN, 12));
		
		cok.setBounds(180,150,35,25);
		cok.setFocusable(false);
		cok.setForeground(new Color(0xeea47f));
		cok.setBackground(new Color(0x656470));
		cok.setBorder(new LineBorder(new Color(0x3D3D3D),1));
		cok.addActionListener(this);
		
		aboutwin.setEditable(false);
		aboutwin.setFont(new Font("Arial", Font.PLAIN, 12));
		aboutwin.setFocusable(false);
		aboutwin.setContentType("text/html");
        aboutwin.setText("<html><body><b><font size=8>About My PassManager</font></b><br>"
                + "<br>"
                + "The provided Java application is a simple password manager called \"My PassManager.\" Its main purpose is to allow users to create a new user account, store their passwords securely, and manage their password data sets.\r\n"
                + "\r\n<br>"
                + "<br>"
                + "My PassManager provides a basic password management solution, enabling users to securely store and manage their passwords within the app. However, it's worth noting that real-world password managers usually have more advanced features like encryption, master password protection, browser integration, and cross-platform compatibility.<br>"
                + "<br>"
                + "The Memes and Icons used in this application were taken from <a href=\"https://www.supermeme.ai/\">Supermeme.ai</a> and <a href=\"https://www.flaticon.com/\">Flaticon.com</a><br></body></html>");
        aboutwin.addHyperlinkListener(e -> {
            if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) 
            {
                if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) 
                {
                    try 
                    {
                        Desktop.getDesktop().browse(e.getURL().toURI());
                    } 
                    catch (IOException | URISyntaxException ex) 
                    {
                    	now = LocalDateTime.now();
						errorlog = errorlog+"\n"+dtf.format(now)+" Log: "+ex.getMessage();
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
						ex.printStackTrace();
                    }
                }
            }
        });
        
        aboutpane.setBounds(15,15,350,300);
		aboutpane.setBorder(null);
		aboutpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		save.setBackground(new Color(0x656470));
		save.setForeground(new Color(0xeea47f));
		save.addActionListener(this);
		save.setMnemonic(KeyEvent.VK_S);
		
		graphics.setBackground(new Color(0x656470));
		graphics.setForeground(new Color(0xeea47f));
		graphics.setMnemonic(KeyEvent.VK_G);
		
		graphics.add(off);
		graphics.add(low);
		graphics.add(balance);
		graphics.add(high);
		
		off.setBackground(new Color(0x656470));
		off.setForeground(new Color(0xeea47f));
		off.addActionListener(this);
		off.setMnemonic(KeyEvent.VK_O);
		
		low.setBackground(new Color(0x656470));
		low.setForeground(new Color(0xeea47f));
		low.addActionListener(this);
		low.setEnabled(false);
		low.setMnemonic(KeyEvent.VK_W);
		
		balance.setBackground(new Color(0x656470));
		balance.setForeground(new Color(0xeea47f));
		balance.addActionListener(this);
		balance.setMnemonic(KeyEvent.VK_B);
		
		high.setBackground(new Color(0x656470));
		high.setForeground(new Color(0xeea47f));
		high.addActionListener(this);
		high.setMnemonic(KeyEvent.VK_H);
		
		reconnect.setBackground(new Color(0x656470));
		reconnect.setForeground(new Color(0xeea47f));
		reconnect.addActionListener(this);
		reconnect.setEnabled(false);
		reconnect.setMnemonic(KeyEvent.VK_R);
		
		disconnect.setBackground(new Color(0x656470));
		disconnect.setForeground(new Color(0xeea47f));
		disconnect.addActionListener(this);
		disconnect.setMnemonic(KeyEvent.VK_D);
		
		about.setBackground(new Color(0x656470));
		about.setForeground(new Color(0xeea47f));
		about.addActionListener(this);
		about.setMnemonic(KeyEvent.VK_A);
		
		exit.setBackground(new Color(0x656470));
		exit.setForeground(new Color(0xeea47f));
		exit.addActionListener(this);
		exit.setMnemonic(KeyEvent.VK_E);
		
		changepass.setBackground(new Color(0x656470));
		changepass.setForeground(new Color(0xeea47f));
		changepass.addActionListener(this);
		changepass.setMnemonic(KeyEvent.VK_C);
		
		logout.setBackground(new Color(0x656470));
		logout.setForeground(new Color(0xeea47f));
		logout.addActionListener(this);
		logout.setMnemonic(KeyEvent.VK_L);
		
		app.setForeground(new Color(0xeea47f));
		user.setForeground(new Color(0xeea47f));
		
		app.add(save);
		app.add(graphics);
		app.addSeparator();
		app.add(reconnect);
		app.add(disconnect);
		app.addSeparator();
		app.add(about);
		app.add(exit);
		
		user.add(changepass);
		user.add(logout);
		
		jmu.setBackground(new Color(0x656470));
		
		jmu.add(app);
		jmu.add(user);
		
		welcometext.setBounds(15,24,905,15);
		welcometext.setText("Welcome "+dbuser+", Let's protect some passwords.");
		welcometext.setForeground(new Color(0xeea47f));
		welcometext.setFont(new Font("Arial", Font.BOLD, 16));
		
		ToolTipManager.sharedInstance().setInitialDelay(100);
        ToolTipManager.sharedInstance().setDismissDelay(5000);
        ToolTipManager.sharedInstance().setReshowDelay(0);
        
        UIManager.put("ToolTip.foreground", new Color(0xeea47f));
        UIManager.put("ToolTip.background", new Color(0x656470));
        UIManager.put("ToolTip.border", BorderFactory.createLineBorder(new Color(0x656470), 1, true));
		
		savebutton.setBounds(0,120,75,75);
		savebutton.setIcon(new ImageIcon(ImageIO.read(bsave)));
		savebutton.setFocusable(false);
		savebutton.setBorder(null);
		savebutton.setToolTipText("Save/Search Password");
		savebutton.setBackground(new Color(0x656470));
		savebutton.addActionListener(this);
		savebutton.addMouseListener(new MouseAdapter() {
		    public void mouseEntered(MouseEvent e){
		        if (sbselected == 1 || pbselected == 0) {
		            savebutton.setBackground(new Color(0x7e7d8a));
		        } else {
		            savebutton.setBackground(new Color(0x3C5A8D));
		        }
		    }

		    public void mouseExited(MouseEvent e) {
		        if (sbselected == 1 || pbselected == 0) {
		            savebutton.setBackground(new Color(0x656470));
		        } else {
		            savebutton.setBackground(new Color(0x2D4369));
		        }
		    }
		});
		
		passgenbutton.setBounds(0,195,75,75);
		passgenbutton.setIcon(new ImageIcon(ImageIO.read(gp)));
		passgenbutton.setFocusable(false);
		passgenbutton.setBorder(null);
		passgenbutton.setToolTipText("Password Generator");
		passgenbutton.setBackground(new Color(0x2D4369));
		passgenbutton.addActionListener(this);
		passgenbutton.addMouseListener(new MouseAdapter() {
		    public void mouseEntered(MouseEvent e) {
		        if (pbselected == 0 || sbselected == 1) {
		            passgenbutton.setBackground(new Color(0x3C5A8D));
		        } else {
		            passgenbutton.setBackground(new Color(0x7E7D8A));
		        }
		    }

		    public void mouseExited(MouseEvent e) {
		        if (pbselected == 0 || sbselected == 1) {
		            passgenbutton.setBackground(new Color(0x2D4369));
		        } else {
		            passgenbutton.setBackground(new Color(0x656470));
		        }
		    }
		});
		
		connection.setBounds(900,12,45,45);
        
        shead.setBounds(370,100,290,20);
        shead.setForeground(new Color(0xEEA47F));
        shead.setFont(new Font("Arial",Font.BOLD,26));
        
        ghead.setBounds(620,100,260,20);
        ghead.setForeground(new Color(0xEEA47F));
        ghead.setFont(new Font("Arial",Font.BOLD,26));
        
        userlabel.setBounds(340, 160, 120, 20);
        userlabel.setForeground(new Color(0xEEA47F));
        userlabel.setFont(new Font("Arial",Font.PLAIN,14));
        
        passwordlabel.setBounds(340, 200, 120, 20);
        passwordlabel.setForeground(new Color(0xEEA47F));
        passwordlabel.setFont(new Font("Arial",Font.PLAIN,14));
        
        awnlabel.setBounds(340, 240, 120, 20);
        awnlabel.setForeground(new Color(0xEEA47F));
        awnlabel.setFont(new Font("Arial",Font.PLAIN,14));
        
        username.setBounds(520,160,170,20);
        username.setForeground(new Color(0xEEA47F));
        username.setBackground(new Color(0x656470));
        username.setBorder(new LineBorder(new Color(0x3D3D3D),1));
        username.getDocument().addDocumentListener(new DocumentListener() {
        	String friends[] = {"zzz","jeri","sando","crimson","been","fiyevu","aedru","aashi","nolan","dayevu","adithya","kara maza","neha","diya","merlin","nanu","kira","deva","gp","niranjana","meenu","gopi","jesine","chandra","sabhyam"};
        	
			@Override
			public void insertUpdate(DocumentEvent e) {
				if(username.getText().equals(""))
				{
					dclear.setEnabled(false);
				}
				else
				{
					dclear.setEnabled(true);
				}
				
				for(int i = 0; i<friends.length; i++) {
					if(trigger == 0 && pbselected == 0)
					{
						if(username.getText().equalsIgnoreCase(friends[i])){
							trigger = 1;
							dg.friends = i;
							dg.pgbutton = 1;
							dg.repaint();
							
							shead.setBounds(170,100,290,20);
							userlabel.setBounds(140, 160, 120, 20);
							passwordlabel.setBounds(140, 200, 120, 20);
							awnlabel.setBounds(140, 240, 120, 20);
							username.setBounds(320,160,170,20);
							password.setBounds(320,200,170,20);
							awn.setBounds(320,240,170,20);
							dsave.setBounds(135,290,65,25);
							dsearch.setBounds(233,290,65,25);
							dclear.setBounds(328,290,65,25);
							d_delete.setBounds(425,290,65,25);
							
							break;
						}
					}
				}
				
				if(trigger == 0 && pbselected == 0)
				{
					if(username.getText().equalsIgnoreCase("stonks") || username.getText().equalsIgnoreCase("capybara")){
						trigger = 1;
						dg.friends = -1;
						dg.meme = 1; 
						dg.pgbutton = 1;
						dg.repaint();
						
						shead.setBounds(170,100,290,20);
						userlabel.setBounds(140, 160, 120, 20);
						passwordlabel.setBounds(140, 200, 120, 20);
						awnlabel.setBounds(140, 240, 120, 20);
						username.setBounds(320,160,170,20);
						password.setBounds(320,200,170,20);
						awn.setBounds(320,240,170,20);
						dsave.setBounds(135,290,65,25);
						dsearch.setBounds(233,290,65,25);
						dclear.setBounds(328,290,65,25);
						d_delete.setBounds(425,290,65,25);
					}
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				
				if(username.getText().equals(""))
				{
					dclear.setEnabled(false);
				}
				else
				{
					dclear.setEnabled(true);
				}
				
				for(int i = 0; i<friends.length; i++) {
					if(trigger == 1 && pbselected == 0)
					{
						if(!username.getText().equalsIgnoreCase(friends[i])) {
							trigger = 0;
							dg.friends = -1;
							dg.meme = 0;
							dg.pgbutton = 0;
							dg.repaint();
							
							shead.setBounds(370,100,290,20);
							userlabel.setBounds(340, 160, 120, 20);
							passwordlabel.setBounds(340, 200, 120, 20);
							awnlabel.setBounds(340, 240, 120, 20);
							username.setBounds(520,160,170,20);
							password.setBounds(520,200,170,20);
							awn.setBounds(520,240,170,20);
							dsave.setBounds(335,290,65,25);
							dsearch.setBounds(433,290,65,25);
							dclear.setBounds(528,290,65,25);
							d_delete.setBounds(625,290,65,25);
						}
					}
				}
				
				if(trigger == 1 && pbselected == 0)
				{
					if(!username.getText().equalsIgnoreCase("stonks") || !username.getText().equalsIgnoreCase("capybara")){
						trigger = 0;
						dg.friends = -1;
						dg.meme = 0;
						dg.pgbutton = 0;
						dg.repaint();
						
						shead.setBounds(370,100,290,20);
						userlabel.setBounds(340, 160, 120, 20);
						passwordlabel.setBounds(340, 200, 120, 20);
						awnlabel.setBounds(340, 240, 120, 20);
						username.setBounds(520,160,170,20);
						password.setBounds(520,200,170,20);
						awn.setBounds(520,240,170,20);
						dsave.setBounds(335,290,65,25);
						dsearch.setBounds(433,290,65,25);
						dclear.setBounds(528,290,65,25);
						d_delete.setBounds(625,290,65,25);
				}
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {}
        });
        
        password.setBounds(520,200,170,20);
        password.setForeground(new Color(0xEEA47F));
        password.setBackground(new Color(0x656470));
        password.setBorder(new LineBorder(new Color(0x3D3D3D),1));
        password.getDocument().addDocumentListener(new DocumentListener() {
        	
			@Override
			public void insertUpdate(DocumentEvent e) {
				if(password.getText().equals(""))
				{
					dclear.setEnabled(false);
				}
				else
				{
					dclear.setEnabled(true);
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				if(password.getText().equals(""))
				{
					dclear.setEnabled(false);
				}
				else
				{
					dclear.setEnabled(true);
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {}
        });
        
        awn.setBounds(520,240,170,20);
        awn.setForeground(new Color(0xEEA47F));
        awn.setBackground(new Color(0x656470));
        awn.setBorder(new LineBorder(new Color(0x3D3D3D),1));
        awn.getDocument().addDocumentListener(new DocumentListener() {
        	
			@Override
			public void insertUpdate(DocumentEvent e) {
				if(awn.getText().equals(""))
				{
					dclear.setEnabled(false);
				}
				else
				{
					dclear.setEnabled(true);
				}
			}

			@Override
			public void removeUpdate(DocumentEvent e) {
				if(awn.getText().equals(""))
				{
					dclear.setEnabled(false);
				}
				else
				{
					dclear.setEnabled(true);
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {}
        });
        
        dsave.setBounds(335,290,65,25);
		dsave.setFocusable(false);
		dsave.setForeground(new Color(0xeea47f));
		dsave.setBackground(new Color(0x656470));
		dsave.setBorder(new LineBorder(new Color(0x3D3D3D),1));
		dsave.addActionListener(this);
		dsave.addMouseListener(new MouseAdapter() {
			    public void mouseEntered(MouseEvent e) {
			    		dsave.setBackground(new Color(0x7e7d8a));
			    }
			    public void mouseExited(MouseEvent e) {
			    		dsave.setBackground(new Color(0x656470));
			    }
		});
		
		dsearch.setBounds(433,290,65,25);
		dsearch.setFocusable(false);
		dsearch.setForeground(new Color(0xeea47f));
		dsearch.setBackground(new Color(0x656470));
		dsearch.setBorder(new LineBorder(new Color(0x3D3D3D),1));
		dsearch.addActionListener(this);
		dsearch.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
		    	 dsearch.setBackground(new Color(0x7e7d8a));
		    }
		    public void mouseExited(MouseEvent e) {
		    	dsearch.setBackground(new Color(0x656470));
		    }
		});
		
		dclear.setBounds(528,290,65,25);
		dclear.setFocusable(false);
		dclear.setEnabled(false);
		dclear.setForeground(new Color(0xeea47f));
		dclear.setBackground(new Color(0x656470));
		dclear.setBorder(new LineBorder(new Color(0x3D3D3D),1));
		dclear.addActionListener(this);
		dclear.addMouseListener(new MouseAdapter() {
			 public void mouseEntered(MouseEvent e) {
			    	if(dclear.isEnabled())
			    	{
			    		dclear.setBackground(new Color(0x7e7d8a));
			    	}
			    }
			    public void mouseExited(MouseEvent e) {
			    	if(dclear.isEnabled())
			    	{
			    		dclear.setBackground(new Color(0x656470));
			    	}
			    }
		});
		
		d_delete.setBounds(625,290,65,25);
		d_delete.setFocusable(false);
		d_delete.setForeground(new Color(0xeea47f));
		d_delete.setBackground(new Color(0x656470));
		d_delete.setBorder(new LineBorder(new Color(0x3D3D3D),1));
		d_delete.addActionListener(this);
		d_delete.addMouseListener(new MouseAdapter() {
		    public void mouseEntered(MouseEvent e) {
		    	 d_delete.setBackground(new Color(0x7e7d8a));
		    }
		    public void mouseExited(MouseEvent e) {
		    	d_delete.setBackground(new Color(0x656470));
		    }
		});
		
		caps.setBounds(580, 150, 150, 25);
		caps.setBackground(new Color(0x2D4369));
		caps.setForeground(new Color(0xeea47f));
		caps.setFont(new Font("Arial",Font.PLAIN,14));
		caps.setFocusable(false);
		caps.setSelected(true);
		caps.addMouseListener(new MouseAdapter() {
		    public void mouseEntered(MouseEvent e) {
		    	 caps.setBackground(new Color(0x3C5A8D));
		    }
		    public void mouseExited(MouseEvent e) {
		    	caps.setBackground(new Color(0x2D4369));
		    }
		});
		
		small.setBounds(580, 180, 150, 25);
		small.setBackground(new Color(0x2D4369));
		small.setForeground(new Color(0xeea47f));
		small.setFont(new Font("Arial",Font.PLAIN,14));
		small.setFocusable(false);
		small.setSelected(true);
		small.addMouseListener(new MouseAdapter() {
		    public void mouseEntered(MouseEvent e) {
		    	 small.setBackground(new Color(0x3C5A8D));
		    }
		    public void mouseExited(MouseEvent e) {
		    	small.setBackground(new Color(0x2D4369));
		    }
		});
		
		numbers.setBounds(580, 210, 150, 25);
		numbers.setBackground(new Color(0x2D4369));
		numbers.setForeground(new Color(0xeea47f));
		numbers.setFont(new Font("Arial",Font.PLAIN,14));
		numbers.setFocusable(false);
		numbers.setSelected(true);
		numbers.addMouseListener(new MouseAdapter() {
		    public void mouseEntered(MouseEvent e) {
		    	 numbers.setBackground(new Color(0x3C5A8D));
		    }
		    public void mouseExited(MouseEvent e) {
		    	 numbers.setBackground(new Color(0x2D4369));
		    }
		});
		
		special.setBounds(580, 240, 150, 25);
		special.setBackground(new Color(0x2D4369));
		special.setForeground(new Color(0xeea47f));
		special.setFont(new Font("Arial",Font.PLAIN,14));
		special.setFocusable(false);
		special.setSelected(true);
		special.addMouseListener(new MouseAdapter() {
		    public void mouseEntered(MouseEvent e) {
		    	 special.setBackground(new Color(0x3C5A8D));
		    }
		    public void mouseExited(MouseEvent e) {
		    	special.setBackground(new Color(0x2D4369));
		    }
		});
		
		for(int i = 8; i < 16; i++)
		{
			pgsize.addItem(i);
		}
		
		pgsize.setBounds(583,270,40,25);
		pgsize.setFocusable(false);
		pgsize.setForeground(new Color(0xEEA47F)); 
		pgsize.setBorder(rb);
		pgsize.setBackground(new Color(0x656470));
		
		pgsizetext.setBounds(635,270,120,25);
		pgsizetext.setForeground(new Color(0xEEA47F));
		pgsizetext.setFont(new Font("Arial",Font.PLAIN,14));
		
		generate.setBounds(580, 310, 85, 25);
		generate.setFocusable(false);
		generate.setForeground(new Color(0xeea47f));
		generate.setBackground(new Color(0x656470));
		generate.setBorder(new LineBorder(new Color(0x3D3D3D),1));
		generate.addActionListener(this);
		generate.addMouseListener(new MouseAdapter() {
		    public void mouseEntered(MouseEvent e) {
		    	 generate.setBackground(new Color(0x7e7d8a));
		    }
		    public void mouseExited(MouseEvent e) {
		    	generate.setBackground(new Color(0x656470));
		    }
		});
		
		passgen.setBounds(685,312,170,20);
	    passgen.setForeground(new Color(0xEEA47F));
	    passgen.setBackground(new Color(0x656470));
	    passgen.setEditable(false);
	    passgen.setBorder(new LineBorder(new Color(0x3D3D3D),1));
	    
	    displaytable.addColumn("Username/Email");
	    displaytable.addColumn("Saved Password");
	    displaytable.addColumn("App/Website Name");
	    
	    displaypanel.setBounds(105,380,825,250);
		
		pcf.getContentPane().add(curpass);
		pcf.getContentPane().add(newpass);
		pcf.getContentPane().add(conpass);
		pcf.getContentPane().add(changepasserror);
		pcf.getContentPane().add(cok);
		pcf.getContentPane().add(curbox);
		pcf.getContentPane().add(newbox);
		pcf.getContentPane().add(conbox);
		
		pcf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		pcf.setIconImage(ImageIO.read(ico));
		pcf.getContentPane().setLayout(null);
		pcf.setSize(400,220);
		pcf.setTitle("Change Password");
		pcf.setLocationRelativeTo(PassManagerUI.this);
		pcf.getContentPane().setBackground(new Color(0x2D4369));
		pcf.setResizable(false);
		
		aboutframe.getContentPane().add(aboutpane);
		
		ico = PassManagerUI.class.getResourceAsStream("/locked.png");
		
		aboutframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		aboutframe.setIconImage(ImageIO.read(ico));
		aboutframe.getContentPane().setLayout(null);
		aboutframe.setSize(395,400);
		aboutframe.setTitle("About");
		aboutframe.setLocationRelativeTo(null);
		aboutframe.getContentPane().setBackground(new Color(0x2D4369));
		aboutframe.setResizable(false);
		
		
		dg.add(welcometext);
		dg.add(savebutton);
		dg.add(passgenbutton);
		dg.add(connection);
		dg.add(shead);
		dg.add(userlabel);
		dg.add(passwordlabel);
		dg.add(awnlabel);
		dg.add(username);
		dg.add(password);
		dg.add(awn);
		dg.add(dsave);
		dg.add(dsearch);
		dg.add(dclear);
		dg.add(d_delete);
		dg.add(displaypanel);
		
		ico = PassManagerUI.class.getResourceAsStream("/locked.png");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(ImageIO.read(ico));
		setJMenuBar(jmu);
		
		getContentPane().add(dg);
		pack();
		
		setTitle("My PassManager");
		setResizable(false);
		setLocationRelativeTo(null);
		getContentPane().setBackground(new Color(0x2D4369));
		getContentPane().setLayout(null);
		setVisible(true);
	}
	
	@SuppressWarnings("static-access")
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource()==save)
		{
			try 
			{
				basic.commit();
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
					vexit.showMessageDialog(this, "Encountered and error while writing log. "+logerror.getMessage(), "Log Error", JOptionPane.ERROR_MESSAGE);
					logerror.printStackTrace();
				}
				e.printStackTrace();
			}
		}
		
		if(ae.getSource()==off)
		{
			quality = 0;
			off.setEnabled(false);
			low.setEnabled(true);
			balance.setEnabled(true);
			high.setEnabled(true);
			
			dg.quality = this.quality;
			ccan.quality = this.quality;
			cok.quality = this.quality;
			aok.quality = this.quality;
			dsave.quality = this.quality;
			dsearch.quality = this.quality;
			d_delete.quality = this.quality;
			dclear.quality = this.quality;
			caps.quality = this.quality;
			small.quality = this.quality;
			numbers.quality = this.quality;
			special.quality = this.quality;
			rb.quality = this.quality;
			
			dg.repaint();
			ccan.repaint();
			cok.repaint();
			aok.repaint();
			dsave.repaint();
			dsearch.repaint();
			d_delete.repaint();
			dclear.repaint();
			caps.crepaint();
			small.crepaint();
			numbers.crepaint();
			special.crepaint();
			pgsize.repaint();
		}
		
		if(ae.getSource()==low)
		{
			quality = 1;
			off.setEnabled(true);
			low.setEnabled(false);
			balance.setEnabled(true);
			high.setEnabled(true);
			
			dg.quality = this.quality;
			ccan.quality = this.quality;
			cok.quality = this.quality;
			aok.quality = this.quality;
			dsave.quality = this.quality;
			dsearch.quality = this.quality;
			d_delete.quality = this.quality;
			dclear.quality = this.quality;
			caps.quality = this.quality;
			small.quality = this.quality;
			numbers.quality = this.quality;
			special.quality = this.quality;
			rb.quality = this.quality;
			
			dg.repaint();
			ccan.repaint();
			cok.repaint();
			aok.repaint();
			dsave.repaint();
			dsearch.repaint();
			d_delete.repaint();
			dclear.repaint();
			caps.crepaint();
			small.crepaint();
			numbers.crepaint();
			special.crepaint();
			pgsize.repaint();
		}
		
		if(ae.getSource()==balance)
		{
			quality = 2;
			off.setEnabled(true);
			low.setEnabled(true);
			balance.setEnabled(false);
			high.setEnabled(true);
			
			dg.quality = this.quality;
			ccan.quality = this.quality;
			cok.quality = this.quality;
			aok.quality = this.quality;
			dsave.quality = this.quality;
			dsearch.quality = this.quality;
			d_delete.quality = this.quality;
			dclear.quality = this.quality;
			caps.quality = this.quality;
			small.quality = this.quality;
			numbers.quality = this.quality;
			special.quality = this.quality;
			rb.quality = this.quality;
			
			dg.repaint();
			ccan.repaint();
			cok.repaint();
			aok.repaint();
			dsave.repaint();
			dsearch.repaint();
			d_delete.repaint();
			dclear.repaint();
			caps.crepaint();
			small.crepaint();
			numbers.crepaint();
			special.crepaint();
			pgsize.repaint();
		}
		
		if(ae.getSource()==high)
		{
			quality = 3;
			off.setEnabled(true);
			low.setEnabled(true);
			balance.setEnabled(true);
			high.setEnabled(false);
			
			dg.quality = this.quality;
			ccan.quality = this.quality;
			cok.quality = this.quality;
			aok.quality = this.quality;
			dsave.quality = this.quality;
			dsearch.quality = this.quality;
			d_delete.quality = this.quality;
			dclear.quality = this.quality;
			caps.quality = this.quality;
			small.quality = this.quality;
			numbers.quality = this.quality;
			special.quality = this.quality;
			rb.quality = this.quality;
			
			dg.repaint();
			ccan.repaint();
			cok.repaint();
			aok.repaint();
			dsave.repaint();
			dsearch.repaint();
			d_delete.repaint();
			dclear.repaint();
			caps.crepaint();
			small.crepaint();
			numbers.crepaint();
			special.crepaint();
			pgsize.repaint();
		}
		
		if(ae.getSource()==reconnect)
		{
			dbconnection = 1;
			
			if(dbconnection == 1)
			{
				disconnect.setEnabled(true);
				reconnect.setEnabled(false);
			}
			else
			{
				disconnect.setEnabled(false);
				reconnect.setEnabled(true);
			}
			
			connect(dbuser,dbpass);
		}
		
		if(ae.getSource()==disconnect)
		{
			dbconnection = 0;
			
			if(dbconnection == 1)
			{
				disconnect.setEnabled(true);
				reconnect.setEnabled(false);
			}
			else
			{
				disconnect.setEnabled(false);
				reconnect.setEnabled(true);
			}
			
			disconnect();
		}
		
		if(ae.getSource()==exit)
		{
			System.exit(0);
		}
		
		if(ae.getSource()==changepass)
		{
			curerror = 0;
			newerror = 0;
			conerror = 0;
			
			curbox.setText("");
			newbox.setText("");
			conbox.setText("");
			
			changepasserror.setText("Enter a value");
			changepasserror.setForeground(new Color(0x2D4369));
			
			curbox.setBorder(new LineBorder(new Color(0x3D3D3D),1));
			newbox.setBorder(new LineBorder(new Color(0x3D3D3D),1));
			conbox.setBorder(new LineBorder(new Color(0x3D3D3D),1));
			
			pcf.setLocationRelativeTo(PassManagerUI.this);
			pcf.setVisible(true);
	
		}
		
		if(ae.getSource()==logout)
		{
			disconnect();
			dispose();
			try 
			{
				lf = new LoginForm();
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
					eexit.showMessageDialog(this, "Encountered and error while writing log. "+logerror.getMessage(), "Log Error", JOptionPane.ERROR_MESSAGE);
					logerror.printStackTrace();
				}
				e.printStackTrace();
			}
		}
		
		if(ae.getSource()==about)
		{
			aboutframe.setVisible(true);
		}
		
		if(ae.getSource()==cok)
		{
			
			if(curbox.getText().equals("") || newbox.getText().equals("") || conbox.getText().equals(""))
			{
				changepasserror.setBounds(160,125,120,20);
				
				if(curbox.getText().equals(""))
				{
					curerror = 1;
					changepasserror.setText("Enter a value");
					curbox.setBorder(BorderFactory.createLineBorder(Color.RED));
				}
				else
				{
					curerror = 0;
					curbox.setBorder(new LineBorder(new Color(0x3D3D3D),1));
				}
				
				if(newbox.getText().equals(""))
				{
					newerror = 1;
					changepasserror.setText("Enter a value");
					newbox.setBorder(BorderFactory.createLineBorder(Color.RED));
				}
				else
				{
					newerror = 0;
					newbox.setBorder(new LineBorder(new Color(0x3D3D3D),1));
				}
				
				if(conbox.getText().equals(""))
				{
					conerror = 1;
					changepasserror.setText("Enter a value");
					conbox.setBorder(BorderFactory.createLineBorder(Color.RED));
				}
				else
				{
					conerror = 0;
					conbox.setBorder(new LineBorder(new Color(0x3D3D3D),1));
				}
				
				if(curerror == 1 || newerror == 1 || conerror == 1)
				{
					changepasserror.setForeground(Color.RED);
				}
				else
				{
					changepasserror.setForeground(new Color(0x2D4369));
				}
			}
			else
			{
				
				changepasserror.setForeground(new Color(0x2D4369));
				
				curbox.setBorder(new LineBorder(new Color(0x3D3D3D),1));
				newbox.setBorder(new LineBorder(new Color(0x3D3D3D),1));
				conbox.setBorder(new LineBorder(new Color(0x3D3D3D),1));
				
				if(!curbox.getText().equals(dbpass))
				{
					curbox.setBorder(BorderFactory.createLineBorder(Color.RED));
					
					changepasserror.setText("Current Password is incorrect!");
					changepasserror.setBounds(110,125,180,20);
					pcf.repaint();
					changepasserror.setForeground(Color.RED);
				}
				else
				{
					if(changepass(dbuser,newbox.getText(),conbox.getText()))
					{
						changepasserror.setText("Changes Saved!");
						changepasserror.setBounds(160,125,120,20);
						pcf.repaint();
						changepasserror.setForeground(Color.GREEN);
					}
					else
					{
						newbox.setBorder(BorderFactory.createLineBorder(Color.RED));
						conbox.setBorder(BorderFactory.createLineBorder(Color.RED));
						
						changepasserror.setText("Passwords do not match!!");
						changepasserror.setBounds(110,125,180,20);
						pcf.repaint();
						changepasserror.setForeground(Color.RED);
					}
				}
			}
		}
		
		if(ae.getSource()==savebutton)
		{
			sbselected = 1;
			pbselected = 0;
			
			dg.pgbutton = 0;
			dg.repaint();
			
			dg.remove(ghead);
			dg.remove(caps);
			dg.remove(small);
			dg.remove(numbers);
			dg.remove(special);
			dg.remove(pgsize);
			dg.remove(pgsizetext);
			dg.remove(generate);
			dg.remove(passgen);
			
			shead.setBounds(370,100,290,20);
			userlabel.setBounds(340, 160, 120, 20);
			passwordlabel.setBounds(340, 200, 120, 20);
			awnlabel.setBounds(340, 240, 120, 20);
			username.setBounds(520,160,170,20);
			password.setBounds(520,200,170,20);
			awn.setBounds(520,240,170,20);
			dsave.setBounds(335,290,65,25);
			dsearch.setBounds(433,290,65,25);
			dclear.setBounds(528,290,65,25);
			d_delete.setBounds(625,290,65,25);
			
			savebutton.setBackground(new Color(0x2D4369));
			passgenbutton.setBackground(new Color(0x656470));
			
			if (sbselected == 1 || pbselected == 0) {
                savebutton.setBackground(new Color(0x7E7D8A));
            } else {
                savebutton.setBackground(new Color(0x3C5A8D));
            }
            if (pbselected == 0 || sbselected == 1) {
                passgenbutton.setBackground(new Color(0x2D4369));
            } else {
                passgenbutton.setBackground(new Color(0x7E7D8A));
            }
		}
		
		if(ae.getSource()==passgenbutton)
		{
			sbselected = 0;
			pbselected = 1;
			trigger = 0;
			
			dg.pgbutton = 1;
			dg.friends = -1;
			dg.meme = 0;
			dg.repaint();
			
			dg.add(ghead);
			dg.add(caps);
			dg.add(small);
			dg.add(numbers);
			dg.add(special);
			dg.add(pgsize);
			dg.add(pgsizetext);
			dg.add(generate);
			dg.add(passgen);
			
			shead.setBounds(170,100,290,20);
			userlabel.setBounds(140, 160, 120, 20);
			passwordlabel.setBounds(140, 200, 120, 20);
			awnlabel.setBounds(140, 240, 120, 20);
			username.setBounds(320,160,170,20);
			password.setBounds(320,200,170,20);
			awn.setBounds(320,240,170,20);
			dsave.setBounds(135,290,65,25);
			dsearch.setBounds(233,290,65,25);
			dclear.setBounds(328,290,65,25);
			d_delete.setBounds(425,290,65,25);
			
			
			savebutton.setBackground(new Color(0x656470));
			passgenbutton.setBackground(new Color(0x2D4369));
			
			 if (sbselected == 1 || pbselected == 0) {
	                savebutton.setBackground(new Color(0x7E7D8A));
	            } else {
	                savebutton.setBackground(new Color(0x2D4369));
	            }
	            if (pbselected == 0 || sbselected == 1) {
	                passgenbutton.setBackground(new Color(0x3C5A8D));
	            } else {
	                passgenbutton.setBackground(new Color(0x7E7D8A));
	            }
		}
		
		if(ae.getSource()==dsave)
		{
			if(!username.getText().equals("") && !password.getText().equals("") && !awn.getText().equals(""))
			{
				try {
					runbasicquery.executeUpdate("insert into passwords(MAIL_OR_USERNAME,password,app_web_name) values('"+username.getText()+"','"+password.getText()+"','"+awn.getText()+"')");
					basic.commit();
				} catch (SQLException e) {
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
		
		if(ae.getSource()==dsearch)
		{
			if(username.getText().equals("") && password.getText().equals("") && awn.getText().equals(""))
			{
				try {
					result = runbasicquery.executeQuery("select * from passwords");
					displaytable.setRowCount(0);
					
					while (result.next())
					{
						rowData = new Object[3];
			            rowData[0] = result.getString("MAIL_OR_USERNAME");
			            rowData[1] = result.getString("PASSWORD");
			            rowData[2] = result.getString("APP_WEB_NAME");
						
						displaytable.setHiddenValueAt(result.getInt("PASSWORD_ID"), displaytable.getRowCount(), 0);
						
						displaytable.addRow(rowData);
					}
				} catch (SQLException e) {
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
			else
			{
				searchdata = "select * from passwords where ";
				
				if(!username.getText().equals(""))
				{
					searchdata = searchdata+"MAIL_OR_USERNAME='"+username.getText()+"'";
					hascondition = true;
				}
				
				if(!password.getText().equals(""))
				{
					if(hascondition)
					{
						searchdata = searchdata+" and ";
					}
					
					searchdata = searchdata+"PASSWORD='"+password.getText()+"'";
					hascondition = true;
				}
				
				if(!awn.getText().equals(""))
				{
					if(hascondition)
					{
						searchdata = searchdata+" and ";
					}
					
					searchdata = searchdata+"APP_WEB_NAME='"+awn.getText()+"'";
				}
				
				try {
					result = runbasicquery.executeQuery(searchdata);
					displaytable.setRowCount(0);
					
					while (result.next())
					{
						rowData = new Object[3];
			            rowData[0] = result.getString("MAIL_OR_USERNAME");
			            rowData[1] = result.getString("PASSWORD");
			            rowData[2] = result.getString("APP_WEB_NAME");
						
						displaytable.setHiddenValueAt(result.getInt("PASSWORD_ID"), displaytable.getRowCount(), 0);
						
						displaytable.addRow(rowData);					
					}
				} catch (SQLException e) {
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
		
		if(ae.getSource()==dclear)
		{
			username.setText("");
			password.setText("");
			awn.setText("");
		}
		
		if(ae.getSource()==d_delete)
		{
			
		}
		
		if(ae.getSource()==generate)
		{
			if(caps.isSelected() || small.isSelected() || numbers.isSelected() || special.isSelected())
			{
				caps.ccbi.BORDER_COLOR = new Color(0x3D3D3D);
				small.ccbi.BORDER_COLOR = new Color(0x3D3D3D);
				numbers.ccbi.BORDER_COLOR = new Color(0x3D3D3D);
				special.ccbi.BORDER_COLOR = new Color(0x3D3D3D);
				
				caps.crepaint();
				small.crepaint();
				numbers.crepaint();
				special.crepaint();
				
				passgen.setForeground(new Color(0xEEA47F));
				PasswordGenerator pg = new PasswordGenerator((int) pgsize.getSelectedItem(),caps.isSelected(),small.isSelected(),numbers.isSelected(),special.isSelected());
				passgen.setText(pg.password);
			}
			else
			{ 
				caps.ccbi.BORDER_COLOR = Color.RED;
				small.ccbi.BORDER_COLOR = Color.RED;
				numbers.ccbi.BORDER_COLOR = Color.RED;
				special.ccbi.BORDER_COLOR = Color.RED;
				
				caps.crepaint();
				small.crepaint();
				numbers.crepaint();
				special.crepaint();
				
				passgen.setForeground(Color.RED);
				passgen.setText("Select any!!");
			}
		}
	}
	
	@SuppressWarnings("static-access")
	public void identify()
	{
		try 
		{
			login = new File("dat/utemp.temp");
			while((dbuchar=dbu.read())!=-1)
			{
				dbuser = dbuser+(char)dbuchar;
			}
			dbu.close();
			login.delete();
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
		
		try 
		{
			login = new File("dat/ptemp.temp");
			while((dbpchar=dbp.read())!=-1)
			{
				dbpass = dbpass+(char)dbpchar;
			}
			dbp.close();
			login.delete();
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
	
	@SuppressWarnings("static-access")
	public void connect(String user,String pass)
	{
		try 
		{
			Class.forName("oracle.jdbc.OracleDriver");
			basic = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", user, pass);
			runbasicquery = basic.createStatement();
			basic.setAutoCommit(false);
			
			connection.setToolTipText("Connected");
			dg.connection = 1;
			dg.repaint();
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
				vexit.showMessageDialog(this, "Encountered and error while writing log. "+logerror.getMessage(), "Log Error", JOptionPane.ERROR_MESSAGE);
				logerror.printStackTrace();
			}
			e.printStackTrace();
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
				vexit.showMessageDialog(this, "Encountered and error while writing log. "+logerror.getMessage(), "Log Error", JOptionPane.ERROR_MESSAGE);
				logerror.printStackTrace();
			}
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("static-access")
	public void disconnect()
	{
		try 
		{
			runbasicquery.close();
			basic.close();
			
			connection.setToolTipText("Disconnected");
			dg.connection = 0;
			dg.repaint();
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
				vexit.showMessageDialog(this, "Encountered and error while writing log. "+logerror.getMessage(), "Log Error", JOptionPane.ERROR_MESSAGE);
				logerror.printStackTrace();
			}
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("static-access")
	public boolean changepass(String uname,String npass,String cpass)
	{
		try 
		{
			Class.forName("oracle.jdbc.OracleDriver");
			admin = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "pman", "mpm879");
			runadminquery = admin.createStatement();
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
				vexit.showMessageDialog(this, "Encountered and error while writing log. "+logerror.getMessage(), "Log Error", JOptionPane.ERROR_MESSAGE);
				logerror.printStackTrace();
			}
			e.printStackTrace();
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
				vexit.showMessageDialog(this, "Encountered and error while writing log. "+logerror.getMessage(), "Log Error", JOptionPane.ERROR_MESSAGE);
				logerror.printStackTrace();
			}
			e.printStackTrace();
		}
		
		if(npass.equals(cpass)) 
		{
			try 
			{
				runadminquery.executeUpdate("alter session set \"_ORACLE_SCRIPT\"=true");
				runadminquery.executeUpdate("alter user "+uname+" identified by "+npass);
				runadminquery.executeUpdate("alter session set \"_ORACLE_SCRIPT\"=false");
				runadminquery.close();
				admin.close();
				dbpass = npass;
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
					vexit.showMessageDialog(this, "Encountered and error while writing log. "+logerror.getMessage(), "Log Error", JOptionPane.ERROR_MESSAGE);
					logerror.printStackTrace();
				}
				e.printStackTrace();
			}
			return true;
		}
		else
		{
			return false;
		}
	}
}