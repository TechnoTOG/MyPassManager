import javax.swing.table.DefaultTableModel;

@SuppressWarnings("serial")
public class CustomTableModel extends DefaultTableModel {
    private Object[][] hiddenData;

    CustomTableModel() {
        super();
        hiddenData = new Object[0][0];
    }

    public void setHiddenValueAt(Object value, int row, int col) {
        // Resize the hidden data array if needed
        if (row >= hiddenData.length) {
            Object[][] newHiddenData = new Object[row + 1][getColumnCount()];
            System.arraycopy(hiddenData, 0, newHiddenData, 0, hiddenData.length);
            hiddenData = newHiddenData;
        }

        hiddenData[row][col] = value;
    }

    public Object getHiddenValueAt(int row, int col) {
        return hiddenData[row][col];
    }
}