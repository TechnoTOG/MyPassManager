import java.awt.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SuppressWarnings("serial")
public class LoginForm extends JFrame implements ActionListener{
	
	@SuppressWarnings("unused")
	private Connection con;
	private Statement runadminquery,runbasicquery;
	int vdefault,uspc = 0,pspc = 0,nullcount = 0,echar;
	private String nametext,passtext,specialchar = "[~!@%^&*()+{}:\\\"|<>?*-/.,';=-`]",errorlog = "";
	
	boolean spcuser,spcpass,ctrial;
	
	private FileWriter dbuser,dbpass;
	
	File epath;
	File elog;
	FileReader fr;
	FileWriter fw;
	
	DateTimeFormatter dtf;
	LocalDateTime now;
	
	PassManagerUI pui;
	Systemdb sdb = new Systemdb();
	AdminBasicdb abdb = new AdminBasicdb();
	
	JOptionPane sqlerror = new JOptionPane();
	JOptionPane exit = new JOptionPane();
	
	JLabel head = new JLabel("LOGIN");
	JLabel username = new JLabel("Username");
	JLabel passw = new JLabel("Password");
	JLabel nerror = new JLabel("*Please enter a value");
	JLabel perror = new JLabel("*Please enter a value");
	
	JTextField logbox = new JTextField(15);
	JPasswordField passbox = new JPasswordField(15);
	
	JButton login = new JButton("LOGIN");
	JButton nuser = new JButton("NEW USER");
	JButton pview = new JButton();
	JButton submit = new JButton("Login");
	JButton create = new JButton("Create");
	
	JPanel pan = new JPanel();
	
	InputStream ilog = LoginForm.class.getResourceAsStream("/login.png");
	InputStream nlog = LoginForm.class.getResourceAsStream("/sign-up.png");
	InputStream openeye = LoginForm.class.getResourceAsStream("/seen-black.png");
	InputStream closeeye = LoginForm.class.getResourceAsStream("/eye-black.png");
	InputStream ico = LoginForm.class.getResourceAsStream("/key.png");
	
	public LoginForm() throws IOException
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
				
		pan.setBackground(new Color(0x2d446a));
		pan.setBounds(0, 0, 300, 500);
		pan.setLayout(null);
		
		login.setIcon(new ImageIcon(ImageIO.read(ilog)));
		login.setFocusable(false);
		login.setBackground(new Color(0xFFBA99));
		login.setBorder(null);
		login.setForeground(Color.BLACK);
		login.setFont(new Font("Montserrat",Font.BOLD,14));
		login.addActionListener(this);
		login.setBounds(20,120,280,100);
		
		nuser.setIcon(new ImageIcon(ImageIO.read(nlog)));
		nuser.setFocusable(false);
		nuser.setBackground(new Color(0x2d446a));
		nuser.setBorder(null);
		nuser.setForeground(Color.WHITE);
		nuser.setFont(new Font("Montserrat",Font.BOLD,14));
		nuser.addActionListener(this);
		nuser.setBounds(20,220,280,100);
		
		head.setForeground(new Color(0x4DABD9));
		head.setFont(new Font("Montserrat",Font.BOLD,35));
		head.setBounds(465,20,211,100);
		
		username.setForeground(Color.BLACK);
		username.setFont(new Font("Roboto",Font.PLAIN,12));
		username.setBounds(415,100,211,100);
		
		passw.setForeground(Color.BLACK);
		passw.setFont(new Font("Roboto",Font.PLAIN,12));
		passw.setBounds(415,140,211,100);
		
		logbox.setBounds(515,140,100,20);
		logbox.setBorder(null);
		
		passbox.setBounds(515,180,100,20);
		passbox.setBorder(null);
		
		pview.setIcon(new ImageIcon(ImageIO.read(closeeye)));
		pview.setFocusable(false);
		pview.setBackground(new Color(0xFFBA99));
		pview.setBorder(null);
		pview.addActionListener(this);
		pview.setBounds(625,180,20,20);
		vdefault = 0;
		
		submit.setFocusable(false);
		submit.setForeground(Color.WHITE);
		submit.setBackground(new Color(0x2d446a));
		submit.setBorder(null);
		submit.addActionListener(this);
		submit.setBounds(500,220,50,25);
		
		create.setFocusable(false);
		create.setForeground(Color.WHITE);
		create.setBackground(new Color(0x2d446a));
		create.setBorder(null);
		create.addActionListener(this);
		create.setBounds(500,220,50,25);
		
		pan.add(login);
		pan.add(nuser);
		
		nerror.setFont(new Font("Arial", Font.PLAIN, 12));
		nerror.setForeground(new Color(0xFFBA99));
		nerror.setBounds(450, 285, 175, 25);
		
		perror.setFont(new Font("Arial", Font.PLAIN, 12));
		perror.setForeground(new Color(0xFFBA99));
		perror.setBounds(450, 300, 175, 25);
		
		add(pan);
		add(head);
		add(username);
		add(passw);
		add(logbox);
		add(passbox);
		add(pview);
		add(submit);
		add(nerror);
		add(perror);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(ImageIO.read(ico));
		setLayout(null);
		setSize(750,500);
		setTitle("Login/New User");
		setLocationRelativeTo(null);
		getContentPane().setBackground(new Color(0xFFBA99));
		setResizable(false);
		setVisible(true);
	}
	
	@SuppressWarnings("static-access")
	public void actionPerformed(ActionEvent ae)
	{
		if(ae.getSource()==login)
		{
			login.setBackground(new Color(0xFFBA99));
			login.setForeground(Color.BLACK);
			
			nuser.setForeground(Color.WHITE);
			nuser.setBackground(new Color(0x2d446a));
			
			submit.setVisible(true);
			create.setVisible(false);
			add(submit);
			repaint();
			
			head.setText("LOGIN");
			head.setBounds(465,20,211,100);
		}
		
		if(ae.getSource()==nuser)
		{
			login.setBackground(new Color(0x2d446a));
			login.setForeground(Color.WHITE);
			
			nuser.setForeground(Color.BLACK);
			nuser.setBackground(new Color(0xFFBA99));
			
			submit.setVisible(false);
			create.setVisible(true);
			add(create);
			repaint();
			
			head.setText("NEW USER");
			head.setBounds(425,20,211,100);
		}
		
		if(ae.getSource()==submit)
		{
			if(logbox.getText().equals("") || String.valueOf(passbox.getPassword()).equals(""))
			{
				if(logbox.getText().equals(""))
				{
					nerror.setText("*Please enter a Username.");
					logbox.setBorder(BorderFactory.createLineBorder(Color.RED));
					nerror.setForeground(Color.RED);
				}
				else
				{
					logbox.setBorder(null);
					nerror.setForeground(new Color(0xFFBA99));
				}
				
				if(String.valueOf(passbox.getPassword()).equals(""))
				{
					perror.setText("*Please enter a Password.");
					passbox.setBorder(BorderFactory.createLineBorder(Color.RED));
					perror.setForeground(Color.RED);
				}
				else
				{
					passbox.setBorder(null);
					perror.setForeground(new Color(0xFFBA99));
				}
			}
			else
			{
				logbox.setBorder(null);
				passbox.setBorder(null);
				
				nerror.setForeground(new Color(0xFFBA99));
				perror.setForeground(new Color(0xFFBA99));
				
				nametext = logbox.getText();
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
					nerror.setText("*Special character not allowed.");
					logbox.setBorder(BorderFactory.createLineBorder(Color.RED));
					nerror.setForeground(Color.RED);
				}
				else
				{
					uspc = 0;
					logbox.setBorder(null);
					nerror.setForeground(new Color(0xFFBA99));
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
					perror.setText("*Special character not allowed.");
					passbox.setBorder(BorderFactory.createLineBorder(Color.RED));
					perror.setForeground(Color.RED);
				}
				else
				{
					pspc = 0;
					passbox.setBorder(null);
					perror.setForeground(new Color(0xFFBA99));
				}
				
				if(uspc<1 && pspc<1)
				{
					passtext = new String(passbox.getPassword());
					try
					{
						dbuser = new FileWriter("dat/utemp.temp");
						dbpass = new FileWriter("dat/ptemp.temp");
						dbuser.write(logbox.getText());
						dbpass.write(passtext);
						
						dbuser.close();
						dbpass.close();
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
					ctrial = abdb.vconnectbasicdb(logbox.getText(),passtext);
					if(ctrial)
					{
						dispose();
						try
						{
							pui = new PassManagerUI();
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
					}
				}
			}
		}
		
		if(ae.getSource()==create)
		{
			if(logbox.getText().equals("") || String.valueOf(passbox.getPassword()).equals(""))
			{
				if(logbox.getText().equals(""))
				{
					nerror.setText("*Please enter a Username.");
					logbox.setBorder(BorderFactory.createLineBorder(Color.RED));
					nerror.setForeground(Color.RED);
				}
				else
				{
					logbox.setBorder(null);
					nerror.setForeground(new Color(0xFFBA99));
				}
				
				if(String.valueOf(passbox.getPassword()).equals(""))
				{
					perror.setText("*Please enter a Password.");
					passbox.setBorder(BorderFactory.createLineBorder(Color.RED));
					perror.setForeground(Color.RED);
				}
				else
				{
					passbox.setBorder(null);
					perror.setForeground(new Color(0xFFBA99));
				}
			}
			else
			{
				logbox.setBorder(null);
				passbox.setBorder(null);
				
				nerror.setForeground(new Color(0xFFBA99));
				perror.setForeground(new Color(0xFFBA99));
				
				nametext = logbox.getText();
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
					nerror.setText("*Special character not allowed.");
					logbox.setBorder(BorderFactory.createLineBorder(Color.RED));
					nerror.setForeground(Color.RED);
				}
				else
				{
					uspc = 0;
					logbox.setBorder(null);
					nerror.setForeground(new Color(0xFFBA99));
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
					perror.setText("*Special character not allowed.");
					passbox.setBorder(BorderFactory.createLineBorder(Color.RED));
					perror.setForeground(Color.RED);
				}
				else
				{
					pspc = 0;
					passbox.setBorder(null);
					perror.setForeground(new Color(0xFFBA99));
				}
				
				if(uspc<1 && pspc<1)
				{
					abdb.connectadmindb();
					nametext = logbox.getText();
					passtext = new String(passbox.getPassword());
					try 
					{
						runadminquery = abdb.admin.createStatement();
						abdb.admin.setAutoCommit(false);				
						runadminquery.executeUpdate("alter session set \"_ORACLE_SCRIPT\"=true");
						runadminquery.executeUpdate("create user "+nametext+" identified by "+passtext);
						runadminquery.executeUpdate("grant create session to "+nametext);
						runadminquery.executeUpdate("grant all privileges to "+nametext);
						runadminquery.executeUpdate("insert into password_manager_users (username) values('"+logbox.getText()+"')");
						abdb.admin.commit();
						runadminquery.close();
						abdb.admin.close();
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
						e.printStackTrace();
					}
					
					abdb.connectbasicdb(logbox.getText(),passtext);
					try 
					{
						runbasicquery = abdb.vbasic.createStatement();
						abdb.vbasic.setAutoCommit(false);
						
						runbasicquery.executeUpdate("create table passwords(password_id NUMBER GENERATED BY DEFAULT ON NULL AS IDENTITY,mail_or_username varchar(30),password varchar(30),app_web_name varchar(20),primary key(password_id))");
						abdb.vbasic.commit();
						runbasicquery.close();
						abdb.vbasic.close();
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
						e.printStackTrace();
					}
					
					passtext = new String(passbox.getPassword());
					try
					{
						dbuser = new FileWriter("dat/utemp.temp");
						dbpass = new FileWriter("dat/ptemp.temp");
						dbuser.write(logbox.getText());
						dbpass.write(passtext);
						
						dbuser.close();
						dbpass.close();
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
					try 
					{
						pui = new PassManagerUI();
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
				}
			}
		}
		
		if(ae.getSource()==pview)
		{
			if(vdefault==0)
			{
				try 
				{
					openeye = FirstUser.class.getResourceAsStream("/seen-black.png");
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
					closeeye = FirstUser.class.getResourceAsStream("/eye-black.png");
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
	}
}
