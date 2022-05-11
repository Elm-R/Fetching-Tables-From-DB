import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Vector;

public class ConnectAndFetchTable extends JFrame implements ActionListener {


    JButton button;
   

    static Vector<String> columnNames = new Vector<String>();
    static Vector<Vector<Object>> data = new Vector<Vector<Object>>();



    ConnectAndFetchTable() throws SQLException {


        button = new JButton();
        button.setBounds(50, 100, 350, 100);
        button.addActionListener(this);
        button.setText("connect and fetch data");

        button.setFocusable(false);

        button.setHorizontalTextPosition(JButton.CENTER);
        button.setVerticalTextPosition(JButton.BOTTOM);
        button.setFont(new Font("Comic Sans", Font.BOLD, 24));
        button.setIconTextGap(-15);
        button.setForeground(Color.red);
        button.setBackground(Color.yellow);
        button.setBorder(BorderFactory.createEtchedBorder());

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        this.setSize(500, 500);
        this.setVisible(true);
        this.add(button);

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == button) {

            try {

                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/Example", "root", "Pass");

                Statement statement = connection.createStatement();

                String query = new String("select * from employees");

                ResultSet resultSet = statement.executeQuery((query));

                buildTableModel(resultSet);


            } catch (SQLException s) {
                System.out.println("SQL statement is not executed!");
            } catch (Exception ee) {
                ee.printStackTrace();
            }

        }
    }


    public static DefaultTableModel buildTableModel(ResultSet rs) throws SQLException {
        if(rs == null) return null ;

        ResultSetMetaData metaData = rs.getMetaData();

        // names of columns
        
        int columnCount = metaData.getColumnCount();
        for (int column = 1; column <= columnCount; column++) {
            columnNames.add(metaData.getColumnName(column));
        }

        // data of the table
        
        while (rs.next()) {
            Vector<Object> vector = new Vector<Object>();
            for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                vector.add(rs.getObject(columnIndex));
            }
            data.add(vector);
        }

        return Adjusting(rs);

    }



    public static  DefaultTableModel Adjusting(ResultSet rs) throws SQLException {

        DefaultTableModel model = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(model);
        table.setShowGrid(true);
        table.setShowVerticalLines(true);
        JScrollPane pane = new JScrollPane(table);
        JFrame f = new JFrame("Populate JTable from Database");
        JPanel panel = new JPanel();
        panel.add(pane);
        f.add(panel);
        f.setSize(500, 250);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setVisible(true);



       return new DefaultTableModel(data, columnNames);
    }




    public static void main(String[] args) throws SQLException {

        ConnectAndFetchTable con = new ConnectAndFetchTable();

    }

}
