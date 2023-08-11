import javax.swing.*;
import javax.swing.plaf.basic.BasicComboBoxEditor;
import javax.swing.plaf.basic.BasicComboBoxUI;
import java.awt.*;
import java.awt.geom.Path2D;

@SuppressWarnings("serial")
public class RoundedCornerComboBox<E> extends JComboBox<E> {

    public RoundedCornerComboBox() {
        setUI(new CustomComboBoxUI());
        setRenderer(new CustomComboBoxRenderer());
        setEditable(true);
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 5));
        setBackground(new Color(0x656470)); // Background color of the closed JComboBox
        setForeground(new Color(0xEEA47F)); // Text color of the closed JComboBox
        setPreferredSize(new Dimension(200, 30)); // Set the preferred size to make the drop-down button visible
    }

    private static class CustomComboBoxUI extends BasicComboBoxUI {

        @Override
        public void installUI(JComponent c) {
            super.installUI(c);
            comboBox.setEditor(new BasicComboBoxEditor.UIResource() {
                @Override
                protected JTextField createEditorComponent() {
                    JTextField editor = new JTextField("8", 9);
                    editor.setVisible(true);
                    editor.setBorder(null);
                    editor.setFont(comboBox.getFont());
                    editor.setForeground(new Color(0xEEA47F));
                    editor.setBackground(new Color(0x656470)); // Set the editor background color
                    return editor;
                }
            });
        }

        @Override
        protected JButton createArrowButton() {
            return new CustomArrowButton();
        }
    }

	private static class CustomArrowButton extends JButton {

        private Color arrowColor = new Color(0xEEA47F); // Color of the drop-down arrow

        public CustomArrowButton() {
            setFocusPainted(false);
            setBorderPainted(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
            int arrowWidth = getHeight() / 2;
            int x = (getWidth() - arrowWidth) / 2;
            int y = (getHeight() - arrowWidth) / 2;

            // Draw the rounded rectangle for the button background
            g2.setColor(new Color(0x656470));
            g2.fillRect(0, 0, getWidth(), getHeight());

            // Draw the arrow
            g2.setColor(arrowColor);
            Path2D arrow = new Path2D.Double();
            arrow.moveTo(x, y);
            arrow.lineTo(x + arrowWidth, y);
            arrow.lineTo(x + arrowWidth / 2, y + arrowWidth);
            arrow.closePath();
            g2.fill(arrow);
        }
    }

	private static class CustomComboBoxRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setOpaque(false); // Set the background of the renderer as transparent
            if (isSelected) {
                label.setBackground(new Color(0xEEA47F)); // Selected item background color in the drop-down
                label.setForeground(new Color(0x656470)); // Selected item text color in the drop-down
            } else {
                label.setBackground(new Color(0x656470));
                label.setForeground(new Color(0xEEA47F));
            }
            return label;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
        super.paintComponent(g);
    }
}
