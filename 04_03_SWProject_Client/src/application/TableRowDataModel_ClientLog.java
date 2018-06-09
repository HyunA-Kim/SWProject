package application;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.StringProperty;

public class TableRowDataModel_ClientLog {

	private IntegerProperty Index;			//����
	private StringProperty Time;			//�ð�
	private StringProperty LogMsg;			//�α׸޼���
	
	public TableRowDataModel_ClientLog(IntegerProperty Index,StringProperty Time,StringProperty LogMsg) {
		this.Index=Index;
		this.Time=Time;
		this.LogMsg=LogMsg;
	}
	
	public IntegerProperty IndexProperty() {
		return Index;
	}//���� �޾ƿ���
	public StringProperty TimeProperty() {
		return Time;
	}//�ð� �޾ƿ���
	public StringProperty MsgProperty() {
		return LogMsg;
	}//�޼��� �޾ƿ���
	
}//Client Logfile �Լ� ���� ��
