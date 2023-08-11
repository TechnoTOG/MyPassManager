import java.awt.*;
import javax.swing.*;
import java.awt.geom.GeneralPath;

public class CustomCheckBoxIcon implements Icon {
    private static final int ICON_WIDTH = 16;
    private static final int ICON_HEIGHT = 16;
    public static Color BORDER_COLOR = new Color(0x3D3D3D);
    private static final Color CHECK_COLOR = new Color(0xeea47f);
    int quality = 1;

	@Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        AbstractButton button = (AbstractButton) c;
        ButtonModel model = button.getModel();

        Graphics2D g2d = (Graphics2D) g;
        
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
        
        g2d.setColor(new Color(0x656470));
        g2d.fillRect(x, y, ICON_WIDTH - 1, ICON_HEIGHT - 1);
        g2d.setColor(BORDER_COLOR);
        g2d.drawRect(x, y, ICON_WIDTH - 1, ICON_HEIGHT - 1);

        if (model.isSelected()) {
            g2d.setColor(CHECK_COLOR);

            // Draw a tick sign using a GeneralPath
            GeneralPath tickPath = new GeneralPath();
            tickPath.moveTo(x + 4, y + ICON_HEIGHT / 2);
            tickPath.lineTo(x + ICON_WIDTH / 2 - 1, y + ICON_HEIGHT - 4);
            tickPath.lineTo(x + ICON_WIDTH - 4, y + 4);
            g2d.setStroke(new BasicStroke(2));
            g2d.draw(tickPath);
        }
    }

    @Override
    public int getIconWidth() {
        return ICON_WIDTH;
    }

    @Override
    public int getIconHeight() {
        return ICON_HEIGHT;
    }
    
    public void change(Color newc)
    {
    	BORDER_COLOR = newc;
    }
}