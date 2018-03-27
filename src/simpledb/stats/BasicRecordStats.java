package simpledb.stats;



//DAVIDE

public class BasicRecordStats {


	private	int readRecord;
	private	int writtenRecord;
	private	int readFieldsRecord;
	private	int writtenFieldsRecord;


	public BasicRecordStats() {}


	
	
	
	
	public void incrementerReadRecord() {
		this.readRecord ++;
	}

	public void incrementerWrittenRecord() {
		this.writtenRecord ++ ;
	}

	public void incrementerReadFieldsRecord() {
		this.readFieldsRecord ++;
	}

	public void incrementerWrittenFieldsRecord() {
		this.writtenFieldsRecord ++;
	}
	
	
	
	
	@Override
	public String toString() {
		return "BasicRecordStats [readRecord=" + readRecord + ", writtenRecord=" + writtenRecord + ", readFieldsRecord="
				+ readFieldsRecord + ", writtenFieldsRecord=" + writtenFieldsRecord + "]\n";
	}

	
	

	//Getters & Setters

	public int getReadRecord() {
		return readRecord;
	}
	public void setReadRecord(int readRecord) {
		this.readRecord = readRecord;
	}
	public int getWrittenRecord() {
		return writtenRecord;
	}
	public void setWrittenRecord(int writtenRecord) {
		this.writtenRecord = writtenRecord;
	}
	public int getReadFieldsRecord() {
		return readFieldsRecord;
	}
	public void setReadFieldsRecord(int readFieldsRecord) {
		this.readFieldsRecord = readFieldsRecord;
	}
	public int getWrittenFieldsRecord() {
		return writtenFieldsRecord;
	}
	public void setWrittenFieldsRecord(int writtenFieldsRecord) {
		this.writtenFieldsRecord = writtenFieldsRecord;
	}









}
