import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.*;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class FirstUser extends JFrame implements ActionListener,ItemListener
{
	int page = 0,vdefault,generate,alimiter,blimiter,climiter,dlimiter,nullcount,echar,uspc = 0,pspc = 0;
	String a = "Making required changes........", b = "Creating Data Sets 1........", c = "Creating Instance User........", d = "Creating Data Sets 2........";
	private String pass,usergen,passtext,nametext,errorlog = "",specialchar = "[~!@%^&*()+{}:\\\"|<>?*-/.,';=-`]";
	boolean spcuser = false,spcpass = false;
	
	Systemdb sdb = new Systemdb();
	AdminBasicdb abdb = new AdminBasicdb();
	
	Statement runsystemquery,runadminquery,runbasicquery;
	
	File epath;
	File elog;
	FileReader fr;
	FileWriter fw;
	FileWriter privacypolicy;
	FileWriter readme;
	
	DateTimeFormatter dtf;
	LocalDateTime now;
	
	File dpath;
	FileOutputStream fos;
	ObjectOutputStream oos;
	LoginForm lf;
	
	File errorsound;
	AudioInputStream ais;
	Clip sound;
	
	JTextArea jta = new JTextArea();
	JTextPane policy = new JTextPane();
	JScrollPane policypane = new JScrollPane(policy);
	
	JCheckBox jcb = new JCheckBox("I, Accept to the Privacy Policy");
	
	JPanel buttonPanel = new JPanel();
	
	JLabel head = new JLabel("CREATE NEW USER");
	JLabel username = new JLabel("Username:");
	JLabel password = new JLabel("Password:");
	JLabel version = new JLabel("Version: pre-release");
	JLabel nerror = new JLabel("Please enter a Username");
	JLabel perror = new JLabel("Please enter a Password");
	
	JTextField namebox = new JTextField(15);
	JPasswordField passbox = new JPasswordField(15);
	
	JButton next = new JButton("Next >");
	JButton back = new JButton("< Back");
	JButton cancel = new JButton("Cancel");
	JButton create = new JButton("Create");
	JButton pview = new JButton();
	
	JOptionPane sqlerror = new JOptionPane();
	private JOptionPane sqlpass = new JOptionPane();
	JOptionPane exit = new JOptionPane();
	
	JProgressBar jpb = new JProgressBar();
	
	Font font = new Font("Arial", Font.PLAIN, 12);
	
	InputStream openeye = FirstUser.class.getResourceAsStream("/seen.png");
	InputStream closeeye = FirstUser.class.getResourceAsStream("/eye.png");
	InputStream ico = FirstUser.class.getResourceAsStream("/agreement.png");
	InputStream serror = FirstUser.class.getResourceAsStream("/server-error.png");
	
	@SuppressWarnings("static-access")
	public FirstUser() throws IOException 
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
		
		try (InputStream inputStream = FirstUser.class.getResourceAsStream("/system-error-notice-trial-132470.wav")) {
            if (inputStream != null) {
                AudioInputStream ais = AudioSystem.getAudioInputStream(inputStream);
                sound = AudioSystem.getClip();
                sound.open(ais);
            }
        } 
		catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) 
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
		
		policy.setEditable(false);
		policy.setFont(font);
		policy.setFocusable(false);
		policy.setContentType("text/html");
        policy.setText("<html><body><b><font size=8>Privacy Policy for My PassManager</font></b><br>"
                + "<br>"
                + "Effective Date: 06/07/2023<br>"
                + "<br>"
                + "This Privacy Policy describes how My PassManager (\"we,\" \"us,\" or \"our\") collects, uses, stores, and shares information obtained from users (\"you\" or \"users\") of our password manager application (\"My PassManager\").<br>"
                + "<br>"
                + "<b><font size=5>1. Information We Collect</font></b><br>"
                + "1.1. User-Provided Information: When you use our Application, you may provide certain information voluntarily, such as account credentials, passwords, and other sensitive data necessary for the functioning of the password manager.<br>"
                + "<br>"
                + "<b><font size=5>2. Use of Information</font></b><br>"
                + "2.1. We use the information we collect to provide, maintain, and improve the functionality and security of the Application, including managing your passwords and related data.<br>"
                + "<br>"
                + "2.2. We may use aggregated and anonymized data for statistical and analytical purposes to understand user behavior, improve our services, and enhance the user experience.<br>"
                + "<br>"
                + "<b><font size=5>3. Data Security</font></b><br>"
                + "3.1. We take reasonable measures to protect the security of your information. However, please note that no method of transmission over the internet or electronic storage is 100% secure. Therefore, while we strive to use commercially acceptable means to protect your information, we cannot guarantee its absolute security.<br>"
                + "<br>"
                + "<b><font size=5>4. Data Sharing</b></font><br>"
                + "4.1. We may share your information with third parties only in the following circumstances:<br>"
                + "- With your explicit consent.<br>"
                + "- If required by law, regulation, or legal process.<br>"
                + "- To enforce our rights, protect our property, or prevent fraud or illegal activities.<br>"
                + "- In the event of a merger, acquisition, or sale of all or a portion of our assets, where your information may be transferred as part of the transaction.<br>"
                + "<br>"
                + "<b><font size=5>5. Third-Party Services</font></b><br>"
                + "5.1. The Application may integrate with third-party services or include links to third-party websites or resources. We are not responsible for the privacy practices or content of these third parties. We encourage you to review the privacy policies of these third parties before using their services or providing any personal information.<br>"
                + "<br>"
                + "<b><font size=5>6. Children's Privacy</font></b><br>"
                + "6.1. The Application is not intended for use by individuals under the age of 13. We do not knowingly collect personal information from children. If you are a parent or guardian and believe your child has provided us with personal information, please contact us, and we will take steps to remove that information from our systems.<br>"
                + "<br>"
                + "<b><font size=5>7. Changes to the Privacy Policy</font></b><br>"
                + "7.1. We reserve the right to modify this Privacy Policy at any time. Any changes will be effective immediately upon posting the revised Privacy Policy. We encourage you to review this Privacy Policy periodically for any updates.<br>"
                + "<br>"
                + "<b><font size=5>8. Contact Us</font></b><br>"
                + "8.1. If you have any questions, concerns, or requests regarding this Privacy Policy or our data practices, please contact us at(yet to be updated).</body></html>");
		
		policypane.setBorder(null);
		policypane.setBounds(15,15,455,300);
		policypane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		jcb.setBackground(new Color(0x2D4369));
		jcb.setForeground(new Color(0xEEA47F));
		jcb.addItemListener(this);
		jcb.setFocusable(false);
		jcb.setBounds(15,340,200,15);
		
		back.setFocusable(false);
		back.setVisible(false);
		back.addActionListener(this);
		back.setForeground(new Color(0xEEA47F));
		back.setBackground(new Color(0x656470));
		back.setFont(new Font("Arial",Font.PLAIN,12));
		back.setBounds(200,380,71,20);
		
		next.setFocusable(false);
		next.setEnabled(false);
		next.addActionListener(this);
		next.setForeground(new Color(0xEEA47F));
		next.setBackground(new Color(0x656470));
		next.setFont(new Font("Arial",Font.PLAIN,12));
		next.setBounds(280,380,71,20);
		
		cancel.setFocusable(false);
		cancel.addActionListener(this);
		cancel.setForeground(new Color(0xEEA47F));
		cancel.setBackground(new Color(0x656470));
		cancel.setFont(new Font("Arial",Font.PLAIN,12));
		cancel.setBounds(377,380,73,20);
		
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(back);
		buttonPanel.add(next);
		buttonPanel.add(cancel);
		
		version.setForeground(new Color(0xEEA47F));
		version.setFont(new Font("Consolas",Font.PLAIN,11));
		version.setBounds(15,350,211,100);
		
		head.setForeground(new Color(0xEEA47F));
		head.setFont(new Font("Montserrat",Font.BOLD,35));
		head.setBounds(25,20,350,50);
		
		username.setForeground(new Color(0xEEA47F));
		username.setFont(new Font("Roboto",Font.PLAIN,12));
		username.setBounds(30,40,211,100);
		
		password.setForeground(new Color(0xEEA47F));
		password.setFont(new Font("Roboto",Font.PLAIN,12));
		password.setBounds(30,80,211,100);
		
		namebox.setForeground(new Color(0xEEA47F));
		namebox.setBackground(new Color(0x656470));
		namebox.setBounds(120,80,100,20);
		namebox.setBorder(null);
		
		passbox.setForeground(new Color(0xEEA47F));
		passbox.setBackground(new Color(0x656470));
		passbox.setBounds(120,120,100,20);
		passbox.setBorder(null);
		
		pview.setIcon(new ImageIcon(ImageIO.read(closeeye)));
		pview.setFocusable(false);
		pview.setBackground(new Color(0x2D4369));
		pview.setBorder(null);
		pview.addActionListener(this);
		pview.setBounds(230,120,20,20);
		vdefault = 0;
		
		nerror.setForeground(new Color(0x2D4369));
		nerror.setFont(new Font("Arial", Font.PLAIN, 11));
		nerror.setBounds(260,40,411,100);
		
		perror.setForeground(new Color(0x2D4369));
		perror.setFont(new Font("Arial", Font.PLAIN, 11));
		perror.setBounds(260,80,411,100);
		
		create.setFocusable(false);
		create.addActionListener(this);
		create.setForeground(new Color(0xEEA47F));
		create.setBackground(new Color(0x656470));
		create.setFont(new Font("Arial",Font.PLAIN,12));
		create.setBounds(110,160,73,20);
		
		jta.setBounds(30,230,420,100);
		jta.setEditable(false);
		
		jpb.setVisible(true);
		jpb.setStringPainted(true);
        jpb.setBounds(30,200,420,15);
        jpb.setForeground(new Color(0x447D06));
        jpb.setBackground(new Color(0xFFFFFF));
        
        
        
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(policypane, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		
		add(next);
		add(cancel);
		add(jcb);
		add(version);
		getContentPane().add(policypane); 
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(ImageIO.read(ico));
		setLayout(null);
		setSize(500,450);
		setTitle("Welcome");
		setLocationRelativeTo(null);
		getContentPane().setBackground(new Color(0x2D4369));
		setResizable(false);
		setVisible(true);
	}
	
	@SuppressWarnings("static-access")
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource()==next)
		{
			page++;
			if(page==1)
			{
				generate = 0;
				
				back.setVisible(true);
				next.setEnabled(false);
				
				remove(policypane);
				remove(jcb);
				
				add(back);
				add(head);
				add(username);
				add(namebox);
				add(password);
				add(passbox);
				add(pview);
				add(create);
				add(nerror);
				add(perror);
				add(jta);
				
				repaint();
			}
			
			if(page>=2 && generate==1) 
			{
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
						exit.showMessageDialog(this, "Encountered and error while writing log. "+logerror.getMessage(), "Log Error", JOptionPane.ERROR_MESSAGE);
						logerror.printStackTrace();
					}
					e.printStackTrace();
				}
				dispose();
			}
		}
		else if(ae.getSource()==back)
		{
			page--;
			if(page==0)
			{
				back.setVisible(false);
				next.setEnabled(true);
				
				remove(head);
				remove(username);
				remove(namebox);
				remove(password);
				remove(passbox);
				remove(pview);
				remove(create);
				remove(nerror);
				remove(perror);
				remove(jta);
				
				add(policypane);
				add(jcb);
				
				repaint();
			}
			
		}
		
		if(ae.getSource()==cancel)
		{
			System.exit(0);
		}
		
		if(ae.getSource()==pview)
		{
			if(vdefault==0)
			{
				try 
				{
					openeye = FirstUser.class.getResourceAsStream("/seen.png");
					pview.setIcon(new ImageIcon(ImageIO.read(openeye)));
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
						exit.showMessageDialog(this, "Encountered and error while writing log. "+logerror.getMessage(), "Log Error", JOptionPane.ERROR_MESSAGE);
						logerror.printStackTrace();
					}
					e.printStackTrace();
				}
				passbox.setEchoChar((char)0);
				vdefault = 1;
			}
			else
			{
				try 
				{
					closeeye = FirstUser.class.getResourceAsStream("/eye.png");
					pview.setIcon(new ImageIcon(ImageIO.read(closeeye)));
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
						exit.showMessageDialog(this, "Encountered and error while writing log. "+logerror.getMessage(), "Log Error", JOptionPane.ERROR_MESSAGE);
						logerror.printStackTrace();
					}
					e.printStackTrace();
				}
				passbox.setEchoChar('•');
				vdefault = 0;
			}
		}
		
		if(ae.getSource()==create)
		{
			page++;
			
			add(jpb);
			
			repaint();
			
			if(page==2) 
			{
				page--;
				
				if(namebox.getText().equals("") || String.valueOf(passbox.getPassword()).equals(""))
				{
					if(namebox.getText().equals(""))
					{
						nerror.setText("Please enter a Username.");
						namebox.setBorder(BorderFactory.createLineBorder(Color.RED));
						nerror.setForeground(Color.RED);
					}
					else
					{
						namebox.setBorder(null);
						nerror.setForeground(new Color(0x2D4369));
					}
					
					if(String.valueOf(passbox.getPassword()).equals(""))
					{
						perror.setText("Please enter a Password.");
						passbox.setBorder(BorderFactory.createLineBorder(Color.RED));
						perror.setForeground(Color.RED);
					}
					else
					{
						passbox.setBorder(null);
						perror.setForeground(new Color(0x2D4369));
					}
				}
				else
				{
					nametext = namebox.getText();
					passtext = new String(passbox.getPassword());
					for(int i=0;i<nametext.length();i++)
					{
						if(specialchar.indexOf(nametext.charAt(i)) != -1)
						{
							spcuser = true;
							break;
						}
						else
						{
							spcuser = false;
						}
					}
					
					if(spcuser)
					{
						uspc = 1;
						nerror.setText("Special character not allowed.");
						namebox.setBorder(BorderFactory.createLineBorder(Color.RED));
						nerror.setForeground(Color.RED);
					}
					else
					{
						uspc = 0;
						namebox.setBorder(null);
						nerror.setForeground(new Color(0x2D4369));
					}
					
					for(int j=0;j<passtext.length();j++)
					{
						if(specialchar.indexOf(passtext.charAt(j)) != -1)
						{
							spcpass = true;
							break;
						}
						else
						{
							spcpass = false;
						}
					}
					
					if(spcpass)
					{
						pspc = 1;
						perror.setText("Special character not allowed.");
						passbox.setBorder(BorderFactory.createLineBorder(Color.RED));
						perror.setForeground(Color.RED);
					}
					else
					{
						pspc = 0;
						passbox.setBorder(null);
						perror.setForeground(new Color(0x2D4369));
					}
					
					if(uspc<1 && pspc<1)
					{
						do
						{
							nullcount++;
							pass = sqlpass.showInputDialog(this, "Enter password for Oracle DB account system");
							
							if(pass==null)
							{
								exit.showMessageDialog(this, "Could not continue setup. Exting", "Exiting", JOptionPane.ERROR_MESSAGE);
								System.exit(0);
							}
							
							if(nullcount==3)
							{
								int a = exit.showConfirmDialog(this, "Would you like to cancel the setup?", "Cancel setup", exit.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
								if(a==0)
								{
									System.exit(0);
								}
								
							}
							else if(nullcount>4)
							{
								exit.showMessageDialog(this, "Exiting from the application due to multiple empty input.", "Exiting", JOptionPane.ERROR_MESSAGE);
								System.exit(0);
							}
							if(!pass.equals(""))
							{
								break;
							}
						}
						while(pass!=null);
						
						try 
						{
							sdb.connectsystemdb(pass);
						} 
						catch (HeadlessException | IOException e) 
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
						establish();
					}
				}
			}
		}
	}
	
	public void establish()
	{
		@SuppressWarnings("static-access")
		Thread progressbar = new Thread(() -> 
		{
			
			try
			{
				alimiter = blimiter = climiter = dlimiter = 0;
				
				for (int i = 0; i <= 100; i++) 
				{
					updateProgressBar(i);
					Thread.sleep(55);
					
					if(i>=0 && i<25)
					{
						if(i==12)
						{
							
							try 
							{
								runsystemquery = sdb.system.createStatement();
								runsystemquery.executeUpdate("alter session set \"_ORACLE_SCRIPT\"=true");
								runsystemquery.executeUpdate("create user pman identified by mpm879");
								runsystemquery.executeUpdate("grant create session to pman");
								runsystemquery.executeUpdate("grant all privileges to pman");
							} 
							catch (SQLException e) 
							{
								now = LocalDateTime.now();
								errorlog = errorlog+"\n"+dtf.format(now)+" Log: (System DB): "+e.getMessage();
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
								exit.showMessageDialog(this, "Encountered with an error. "+e.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
								e.printStackTrace();
							}
							
						}
						if(alimiter==0) 
						{
							jta.setText(jta.getText()+a);
							alimiter = 1;
						}
					}
					
					if(i>=26 && i<50)
					{
						if(i==38)
						{
							abdb.connectadmindb();
							try 
							{
								runadminquery = abdb.admin.createStatement();
								abdb.admin.setAutoCommit(false);
								
								runadminquery.executeUpdate("create table password_manager_users(user_id NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY,username varchar(25))");
								runadminquery.executeUpdate("insert into password_manager_users (username) values('"+namebox.getText()+"')");
							} 
							catch (SQLException e) 
							{
								now = LocalDateTime.now();
								errorlog = errorlog+"\n"+dtf.format(now)+" Log: (Admin DB): "+e.getMessage();
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
								exit.showMessageDialog(this, "Encountered with an error. "+e.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
								e.printStackTrace();
							}
							
							try
							{
								abdb.admin.commit();
							}
							catch(Exception save)
							{
								now = LocalDateTime.now();
								errorlog = errorlog+"\n"+dtf.format(now)+" Log: "+save.getMessage();
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
								
								exit.showMessageDialog(this, "Encountered with an error. "+save.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
								save.printStackTrace();
							}
						}
						if(blimiter==0) 
						{
							jta.setText(jta.getText()+"\n"+b);
							blimiter = 1;
						}
					}
					
					if(i>=51 && i<75)
					{
						if(i==63)
						{
							passtext = new String(passbox.getPassword());
							usergen = "create user "+namebox.getText()+" identified by "+passtext;
							
							try 
							{
								runsystemquery.executeUpdate(usergen);
								runsystemquery.executeUpdate("grant create session to "+namebox.getText());
								runsystemquery.executeUpdate("grant all privileges to "+namebox.getText());
								runsystemquery.executeUpdate("alter session set \"_ORACLE_SCRIPT\"=false");
							} 
							catch (SQLException e) 
							{
								now = LocalDateTime.now();
								errorlog = errorlog+"\n"+dtf.format(now)+" Log: (System DB): "+e.getMessage();
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
								exit.showMessageDialog(this, "Encountered with an error. "+e.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
								e.printStackTrace();
							}
							
						}
						if(climiter==0) 
						{
							jta.setText(jta.getText()+"\n"+c);
							climiter = 1;
						}
					}
					
					if(i==99)
					{
						try 
						{
							runsystemquery.close();
							runadminquery.close();
							runbasicquery.close();
							
							sdb.system.close();
							abdb.admin.close();
							abdb.fbasic.close();
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
								exit.showMessageDialog(this, "Encountered and error while writing log. "+logerror.getMessage(), "Log Error", JOptionPane.ERROR_MESSAGE);
								logerror.printStackTrace();
							}
							exit.showMessageDialog(this, "Encountered with an error. "+e.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
							e.printStackTrace();
						}
						
						
						sdb.closeconnection();
					}
					
					if(i>=76 && i<=100)
					{
						if(i==80)
						{
							abdb.firstconnectbasicdb(namebox.getText(),passtext);
							try 
							{
								runbasicquery = abdb.fbasic.createStatement();
								abdb.fbasic.setAutoCommit(false);
								
								runbasicquery.executeUpdate("create table passwords(password_id NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY,mail_or_username varchar(30),password varchar(30),app_web_name varchar(20),primary key(password_id))");
							} 
							catch (SQLException e)
							{
								now = LocalDateTime.now();
								errorlog = errorlog+"\n"+dtf.format(now)+" Log: (Basic DB): "+e.getMessage();
								try
								{
									fw = new FileWriter(elog);
									fw.write(errorlog);
									fw.close();
								}
								catch(IOException logerror)
								{
									exit.showMessageDialog(this, "Encountered an error while writing log. "+logerror.getMessage(), "Log Error", JOptionPane.ERROR_MESSAGE);
									logerror.printStackTrace();
								}
								exit.showMessageDialog(this, "Encountered with an error. "+e.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
								e.printStackTrace();
							}
							
							
							try
							{
								abdb.fbasic.commit();
							}
							catch(SQLException com)
							{
								now = LocalDateTime.now();
								errorlog = errorlog+"\n"+dtf.format(now)+" Log: (Basic DB): "+com.getMessage();
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
								exit.showMessageDialog(this, "Encountered with an error. "+com.getMessage(), "DB Error", JOptionPane.ERROR_MESSAGE);
								com.printStackTrace();
							}
						}
						if(i==88)
						{
							try 
							{
								dpath = new File("dat");
								dpath.mkdir();
								privacypolicy = new FileWriter("dat/Privacy Policy.txt");
								readme = new FileWriter("dat/----ReadMe----.txt");
								
								Firstuserfile ob = new Firstuserfile(true);
								
								privacypolicy.write("Privacy Policy for My PassManager\r\n"
										+ "\r\n"
										+ "Effective Date: 06/07/2023\r\n"
										+ "\r\n"
										+ "This Privacy Policy describes how My PassManager (\"we,\" \"us,\" or \"our\") collects, uses, stores, and shares information obtained from users (\"you\" or \"users\") of our password manager application (\"the Application\").\r\n"
										+ "\r\n"
										+ "Information We Collect\r\n"
										+ "1.1. User-Provided Information: When you use our Application, you may provide certain information voluntarily, such as account credentials, passwords, and other sensitive data necessary for the functioning of the password manager.\r\n"
										+ "\r\n"
										+ "Use of Information\r\n"
										+ "2.1. We use the information we collect to provide, maintain, and improve the functionality and security of the Application, including managing your passwords and related data.\r\n"
										+ "\r\n"
										+ "2.2. We may use aggregated and anonymized data for statistical and analytical purposes to understand user behavior, improve our services, and enhance the user experience.\r\n"
										+ "\r\n"
										+ "Data Security\r\n"
										+ "3.1. We take reasonable measures to protect the security of your information. However, please note that no method of transmission over the internet or electronic storage is 100% secure. Therefore, while we strive to use commercially acceptable means to protect your information, we cannot guarantee its absolute security.\r\n"
										+ "\r\n"
										+ "Data Sharing\r\n"
										+ "4.1. We may share your information with third parties only in the following circumstances:\r\n"
										+ "- With your explicit consent.\r\n"
										+ "- If required by law, regulation, or legal process.\r\n"
										+ "- To enforce our rights, protect our property, or prevent fraud or illegal activities.\r\n"
										+ "- In the event of a merger, acquisition, or sale of all or a portion of our assets, where your information may be transferred as part of the transaction.\r\n"
										+ "\r\n"
										+ "Third-Party Services\r\n"
										+ "5.1. The Application may integrate with third-party services or include links to third-party websites or resources. We are not responsible for the privacy practices or content of these third parties. We encourage you to review the privacy policies of these third parties before using their services or providing any personal information.\r\n"
										+ "\r\n"
										+ "Children's Privacy\r\n"
										+ "6.1. The Application is not intended for use by individuals under the age of 13. We do not knowingly collect personal information from children. If you are a parent or guardian and believe your child has provided us with personal information, please contact us, and we will take steps to remove that information from our systems.\r\n"
										+ "\r\n"
										+ "Changes to the Privacy Policy\r\n"
										+ "7.1. We reserve the right to modify this Privacy Policy at any time. Any changes will be effective immediately upon posting the revised Privacy Policy. We encourage you to review this Privacy Policy periodically for any updates.\r\n"
										+ "\r\n"
										+ "Contact Us\r\n"
										+ "8.1. If you have any questions, concerns, or requests regarding this Privacy Policy or our data practices, please contact us at (yet to be updated).");
								privacypolicy.close();
								
								readme.write("Deleting this Folder or Contents in this folder could cause instability of the application");
								readme.close();
								
								fos = new FileOutputStream("dat/fmen.mps");
								oos = new ObjectOutputStream(fos);
								oos.writeObject(ob);
								oos.flush();
								oos.close();
							}
							catch (Exception file) 
							{
								now = LocalDateTime.now();
								errorlog = errorlog+"\n"+dtf.format(now)+" Log: "+file.getMessage();
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
								file.printStackTrace();
							}
						}
						
						if(dlimiter==0) 
						{
							jta.setText(jta.getText()+"\n"+d);
							dlimiter = 1;
						}
						
						if(i==100)
						{
							jta.setText(jta.getText()+"\nFinished");
							next.setText("Finish");
							next.setEnabled(true);
							generate = 1;
						}
					}
				}
			} 
			catch (InterruptedException e) 
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
				exit.showMessageDialog(this, "Encountered an exception in Thread", "Thread error", JOptionPane.ERROR_MESSAGE);
				e.printStackTrace();
			} 
			
		});
		progressbar.start();
	}
	
	public void itemStateChanged(ItemEvent ie)
	{
		if(ie.getStateChange()==1)
		{
			next.setEnabled(true);
		}
		else
		{
			next.setEnabled(false);
		}
	}
	
	public void updateProgressBar(int progress)
	{
		jpb.setValue(progress);
	}
}