package temperatureSensor.gui.monitor;

import java.awt.GridLayout;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.EmptyBorder;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.time.TimeSeriesCollection;

import temperatureSensor.computational.userInterface.IUserRequest;

public class GuiFrame extends JFrame {

	private JPanel contentPane;
	public IUserRequest userRequest;

	JLabel lblMyAddr;
	void setMyAddressText(String text) {
		this.lblMyAddr.setText(text);
	}
	
	TimeSeriesCollection data;
	public TimeSeriesCollection getData() {
		return this.data;
	}
	
	/**
	 * Create the frame.
	 */
	public GuiFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new GridLayout(1, 0, 0, 0));
		
		JSplitPane splitPane = new JSplitPane();
		contentPane.add(splitPane);
		
		this.data = new TimeSeriesCollection();
		JFreeChart chart = ChartFactory.createTimeSeriesChart(
				"Sensor Values",
				"Time",
				"Temperature",
				this.getData(),
				true,
				true,
				false
				);
		ChartPanel chartPanel = new ChartPanel(chart);
		splitPane.setRightComponent(chartPanel);

		JPanel dummy = new JPanel();
		splitPane.setLeftComponent(dummy);

	}
	
}
