package simpledb.stats;


//DAVIDE

public class BasicFileStats {

	
	private int blockRead;
	private int blockWritten;
	
	
	public BasicFileStats(int blockRead, int blockWritten){
		
		this.blockRead = blockRead;
		this.blockWritten = blockWritten;
		
	}


	public BasicFileStats() {


		this.blockRead = 0;
		this.blockWritten = 0;
	}


	public void incrementerRead(){
		
		setBlockRead(this.getBlockRead() + 1);
	}
	
	public void incrementerWrite(){
		

		setBlockWritten(this.getBlockWritten() + 1);
	}
	
	
	public void resetBlockStats() {
		
		this.blockRead = 0;
		this.blockWritten = 0;
		
		
	}
	
	
	@Override
	public String toString() {
		return "blockRead = " + blockRead + ", blockWritten = " + blockWritten;
	}
	
	//Getters & Setters
	
	public int getBlockRead() {
		return blockRead;
	}


	public void setBlockRead(int blockRead) {
		this.blockRead = blockRead;
	}


	public int getBlockWritten() {
		return blockWritten;
	}


	public void setBlockWritten(int blockWritten) {
		this.blockWritten = blockWritten;
	}
	
	
	
}