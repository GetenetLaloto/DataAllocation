import java.util.HashMap;
import java.util.Random;

public class Main {
	
	DataPiece[] disk; //the disk is an array cause it's a fixed size
	//int diskSize; //size of disk just to stop from using .length on array
	HashMap<String, DataPiece[]> data; //
	
	/**
	 * constructor only takes one argument disk size
	 * @param diskSize the size of the emulated disk
	 */
	public Main(int diskSize){
	//	this.diskSize = diskSize;
		disk = new DataPiece[diskSize];
		
		//fill the hashmap with data
		data = generateData(diskSize);
	}
	
	/**
	 * NEEDS TO BE TESTED
	 * This method generates random data files with random size but all together will fit in a disk and fill it
	 * @param diskSize the size of the disk
	 * @return a hashmap where the key is the filename and value is an array of its pieces
	 */
	private HashMap<String, DataPiece[]> generateData(int diskSize){
		HashMap<String, DataPiece[]> data = new HashMap<>();
		int total = 0; //keeps track how much data is made already
		int fileNameSuffix = 0; //the prefix of the filename is a number
		
		//add random amount of files with random number of pieces each
		while(diskSize - total > 10){
			DataPiece[] filePieces = new DataPiece[(new Random()).nextInt(10)]; //make a random size array
			for(int pieceNum = 0; pieceNum < filePieces.length; pieceNum++){
				//in here instead of empty constructor, use the one with next to use the linked data 
				//how it's meant to be used
				filePieces[pieceNum] = new DataPiece();
			}
			
			data.put("file"+(fileNameSuffix++), filePieces); //put in hashmap with proper name
			total+= filePieces.length; //update the total so it wont 
		}
		
		
		//add the last pieces of whatever is left
		DataPiece[] filePieces = new DataPiece[(diskSize - total)];
		for(int pieceNum = 0; pieceNum < (diskSize - total); pieceNum++){
			filePieces[pieceNum] = new DataPiece();
		}
		data.put("file"+(fileNameSuffix++), filePieces);
		total+=filePieces.length;
		
		return data;
	}
	
	/**
	 * this helper method just replace the disk with an empty one, (let the garbage collector worry about the other one)
	 * @param disk the disk to be cleared 
	 */
	private void emptyDisk(){
		disk = new DataPiece[disk.length];
	}
	
	public void ContiguousAllocation(){
		this.emptyDisk(); //clear the disk
		
	}
	
	public void LinkedAllocation(){
		this.emptyDisk(); //clear the disk
		
	}
	
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}