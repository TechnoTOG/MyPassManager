import javax.swing.JCheckBox;

@SuppressWarnings("serial")
public class JCustomCheckBox extends JCheckBox
{
    int quality = 1;
    CustomCheckBoxIcon ccbi = new CustomCheckBoxIcon();
    
	JCustomCheckBox(String text)
	{
		super(text);
		setIcon(ccbi);
		
		ccbi.quality = this.quality;
	}
	
	public void crepaint()
	{
		ccbi = new CustomCheckBoxIcon();
		ccbi.quality = this.quality;
		setIcon(ccbi);
	}
}