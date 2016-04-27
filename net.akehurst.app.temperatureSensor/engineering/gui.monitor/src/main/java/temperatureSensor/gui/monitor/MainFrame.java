package temperatureSensor.gui.monitor;

import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.print.attribute.standard.JobMessageFromOperator;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.jfree.data.time.Millisecond;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.TimeSeries;

import temperatureSensor.computational.userInterface.IUserNotification;
import temperatureSensor.computational.userInterface.IUserRequest;

public class MainFrame implements IUserNotification, Runnable {

	public MainFrame() {
		this.series = new HashMap<String, TimeSeries>();
	}
	
	GuiFrame frame;

	void buildGui() {
		this.frame = new GuiFrame();
		this.frame.userRequest = this.userRequest;
		this.frame.setVisible(true);
	}

	public IUserRequest userRequest;
	public IUserRequest getUserRequest() {
		return userRequest;
	}
	public void setUserRequest(IUserRequest userRequest) {
		this.userRequest = userRequest;
	}

	// ---------------- UserNotification ------------------
	Map<String, TimeSeries> series;
	
	@Override
	public void notifyNewPoint(String id, double value, long time) {
		try {
			if (null!=this.frame && null!= this.frame.getData()) {
				TimeSeries ts = this.series.get(id);
				if (null == ts) {
					ts = new TimeSeries(id);
					this.series.put(id, ts);
					this.frame.getData().addSeries(ts);
				}
				ts.add(new Millisecond(new Date(time)),value);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void raiseAlarm(String id) {
		JOptionPane.showMessageDialog(null, "Alert: Temperature out of range for "+id, "Alert", JOptionPane.WARNING_MESSAGE);
	}
	
	// ---------------- Runnable --------------------
	public void run() {
		this.buildGui();
	}

	Thread thread;

	public void start() {
		this.thread = new Thread(this, "MainFrame");
		this.thread.start();
	}

}
