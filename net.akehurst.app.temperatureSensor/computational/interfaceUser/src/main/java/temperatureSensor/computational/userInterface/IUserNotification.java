package temperatureSensor.computational.userInterface;

public interface IUserNotification {

	void notifyNewPoint(String id, double time, long value);

	void raiseAlarm(String id);

}
