
import java.util.HashMap;
import java.util.Random;
/**
 * THINGS TO DO
 * add counter on contiguosuallocation
 * finish Linked allocation
 * need to separate contiguous and linked to another class to handle next and index numbers better in datapiece
 * @author josuerojas
 *
 */
public class Main {
	DataPiece[] disk; //the disk is an array cause it's a fixed size
	//int diskSize; //size of disk just to stop from using .length on array
	HashMap<String, DataPiece[]> data; 
	final String[] fileNames; //contains all filenames
	
	final static int tries = 4; //number of tries to put in the whole block in disk
	final static Random rand = new Random(); //lots of random numbers needed
	final static int maxFileSize = 10;
	
	/**
	 * constructor only takes one argument disk size
	 * @param diskSize the size of the emulated disk
	 */
	public Main(int diskSize){
		
		disk = new DataPiece[diskSize];
		
		//fill the hashmap with data
		data = generateData(diskSize);
		
		//all file name
		fileNames = (String[]) data.keySet().toArray();
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
			DataPiece[] filePieces = new DataPiece[rand.nextInt(maxFileSize)]; //make a random size array
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
	
	/**
	 * this helper method checks all the file pieces fit in one continuous block in the disk
	 * @param index the index number it wants to be placed
	 * @param sizeFile how many blocks it takes
	 * @return true if it fits else returns false
	 */
	private boolean fitsDisk(int index, int sizeFile){
		for(int i = 0; i < sizeFile; i++){
			if(disk[(index+i) % disk.length]!=null){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 
	 */
	public void ContiguousAllocation(){
		this.emptyDisk(); //clear the disk
		//go through all files
		for(String file: fileNames){
			DataPiece[] filePieces = data.get(file); 
			boolean inDisk = false; //tracks if the file fit in the disk
			int index = 0;
			for(int tryNum = 0; tryNum < tries; tryNum++){
				//NEEDS TO BE FIX SO IT WONT REPEAT THE SAME NUMBER
				index = rand.nextInt(disk.length); //the random index that would be checked 
				
				if(fitsDisk(index,filePieces.length)){
					inDisk = true;
					break;
				}
			}
			//if the file fits in disk contiguously then this is the right way
			if(inDisk){
				for(DataPiece piece: filePieces){
					disk[index++ % disk.length] = piece; //NEED TO TEST THIS PART
				}
			}
			
			//else check in order to find a spot
			else{
				int current = 0;
				while(current < disk.length+maxFileSize){
					if(fitsDisk(current%disk.length, filePieces.length)){
						for(DataPiece piece: filePieces){
							disk[current++ % disk.length] = piece; //NEED TO TEST THIS PART
						}
						break; //break cause you file was added to the disk
					}
					else{
						current+=filePieces.length;
					}
				}
				
				//if all else fails that means you are out off the while loop 
				//and there is no space for it at least not together
				//this is where you count how many pieces failed
				
			}
		}
		
	}
	
	public void LinkedAllocation(){
		this.emptyDisk(); //clear the disk
		//SAME AS CONTIGUOUS BUT...
		//go through all files
		for(String file: fileNames){
			DataPiece[] filePieces = data.get(file); 
			boolean inDisk = false; //tracks if the file fit in the disk
			int index = 0;
			for(int tryNum = 0; tryNum < tries; tryNum++){
				//NEEDS TO BE FIX SO IT WONT REPEAT THE SAME NUMBER
				index = rand.nextInt(disk.length); //the random index that would be checked 
				
				if(fitsDisk(index,filePieces.length)){
					inDisk = true;
					break;
				}
			}
			//if the file fits in disk contiguously then this is the right way
			if(inDisk){
				for(DataPiece piece: filePieces){
					disk[index++ % disk.length] = piece; //NEED TO TEST THIS PART
				}
			}
			//..IT DOES NOT GO IN ORDER TRYING TO FIND A PLACE FOR THE WHOLE BLOCK
			//INSTEAD SPLITS THE BLOCK TO FIT IN THE SPACE
			else{
				index = 0;
				for(DataPiece piece: filePieces){
					while(disk[index] != null) index++;
					if(disk[index] == null) disk[index] = piece;
				}
			}
		}
		
		
	}
	
	
	

	public static void main(String[] args) {
		//testing goes here
	}

}
