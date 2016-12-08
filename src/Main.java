
import java.util.HashMap;
import java.util.Random;
/**
 * THINGS TO DO
 * need to separate contiguous and linked to another class to handle next and index numbers better in datapiece
 * maybe make a parent class for allocation since they start the same
 * @author josuerojas
 *
 */
public class Main {
	
	DataPiece[] disk; //the disk is an array cause it's a fixed size
	HashMap<String, DataPiece[]> data; 
	final String[] fileNames; //contains all filenames
	int totalFiles;
	int numReject = 0; 
	
	static int tries = 100; //number of tries to put in the whole block in disk
	final static Random rand = new Random(); //lots of random numbers needed
	static int maxFileSize = 10; //default value
	static int minFileSize = 1; //default value
	
	
	//TESTING ITEMS CHANGE THIS TO GET DIFFERENT TEST
	static final int[] disksizes = {100,150,200,250,300,350,400,450,500};
	static final int[][] ranges = {{1,10},{10,20},{20,30}}; //ranges are arrays of min and max file sizes
	static final int numberTest = 100;
	
	/**
	 * constructor only takes one argument disk size
	 * @param diskSize the size of the emulated disk
	 */
	public Main(int diskSize, int minFileSize, int maxFileSize){
		
		this.minFileSize = minFileSize;
		this.maxFileSize = maxFileSize;
		disk = new DataPiece[diskSize];
		
		//fill the hashmap with data
		data = generateData(diskSize);
		tries = diskSize;
		
		
		//all file name
		fileNames = (data.keySet().toArray(new String[0]));
		totalFiles = fileNames.length;
		
	}
	
	/**
	 * This method generates random data files with random size but all together will fit in a disk and fill it
	 * @param diskSize the size of the disk
	 * @return a hashmap where the key is the filename and value is an array of its pieces
	 */
	public HashMap<String, DataPiece[]> generateData(int diskSize){
		HashMap<String, DataPiece[]> data = new HashMap<>();
		int total = 0; //keeps track how much data is made already
		int fileNameSuffix = 0; //the prefix of the filename is a number
		
		//add random amount of files with random number of pieces each
		while(diskSize - total > maxFileSize){
			DataPiece[] filePieces = new DataPiece[(rand.nextInt(maxFileSize -minFileSize+1))+minFileSize]; //make a random size array
			for(int pieceNum = 0; pieceNum < filePieces.length; pieceNum++){
				filePieces[pieceNum] = new DataPiece();
				if(pieceNum > 0) filePieces[pieceNum-1].setNext(filePieces[pieceNum]); //sets the next for LinkedAllocation	
			}
			data.put("file_"+(fileNameSuffix++), filePieces); //put in hashmap with proper name
			total+= filePieces.length; //update the total so it wont 
		}
		
		//add the last pieces of whatever is left
		DataPiece[] filePieces = new DataPiece[(diskSize - total)];
		for(int pieceNum = 0; pieceNum < (diskSize - total); pieceNum++){
			filePieces[pieceNum] = new DataPiece();
			if(pieceNum > 0) filePieces[pieceNum-1].setNext(filePieces[pieceNum]); //sets the next for LinkedAllocation

		}
		data.put("file_"+(fileNameSuffix++), filePieces);
		total+=filePieces.length;
		
		return data;
	}
	
	/**
	 * this helper method just replace the disk with an empty one, (let the garbage collector worry about the other one)
	 * @param disk the disk to be cleared 
	 */
	public void emptyDisk(){
		disk = new DataPiece[disk.length];
		numReject = 0; //reset here just to not make another method
	}
	
	
	/**
	 * this helper method checks all the file pieces fit in one continuous block in the disk
	 * @param index the index number it wants to be placed
	 * @param sizeFile how many blocks it takes
	 * @return true if it fits else returns false
	 */
	public boolean fitsDisk(int index, int sizeFile){
		for(int i = 0; i < sizeFile; i++){
			if(disk[(index+i) % disk.length]!=null){
				return false;
			}
		}
		return true;
	}
	
	/**
	 * This method places files in the disk array but only if they fit contiguously. 
	 * It starts randomly and if it can' t be place randomly then look sequentially through the array for continuously space. 
	 */
	public void ContiguousAllocation(){
		this.emptyDisk(); //clear the disk
		//go through all files
		for(String file: fileNames){
			DataPiece[] filePieces = data.get(file); 
			boolean inDisk = false; //tracks if the file fit in the disk
			int index = 0;
			for(int tryNum = 0; tryNum < tries; tryNum++){
				//NEEDS TO BE FIX SO IT WONT REPEAT THE SAME NUMBER BUT THIS REPETITION SHOULD BE MINIMUL SO IT SHOUDLNT MATTER
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
							disk[current++ % disk.length] = piece; 
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
				numReject++;
				
			}
		}
		
	}
	
	/**
	 * This method puts files or data on the disk array.
	 * It starts randomly placing the file in a continuous block in the array.
	 * If it can't find a contiguously block then splits the file to fit in the disk but checking the disk sequentilly.
	 */
	public void LinkedAllocation(){
		this.emptyDisk(); //clear the disk
		//SAME AS CONTIGUOUS BUT...
		//go through all files
		for(String file: fileNames){
			DataPiece[] filePieces = data.get(file); 
			boolean inDisk = false; //tracks if the file fit in the disk
			int index = 0;
			for(int tryNum = 0; tryNum < tries; tryNum++){
				//NEEDS TO BE FIX SO IT WONT REPEAT THE SAME NUMBER NOT A BIG DEAL FOR NOW
				index = rand.nextInt(disk.length); //the random index that would be checked 
				
				if(fitsDisk(index,filePieces.length)){
					inDisk = true;
					break;
				}
			}
			//if the file fits in disk contiguously then this is the right way
			if(inDisk){
				for(DataPiece piece: filePieces){
					disk[index++ % disk.length] = piece; 
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
				
				//there theoretically should not be any fragmentation since each block gets split to fit
			}
		}
		
		//compare the disk to see if it has all the files
		///check if there is empty space
		for(int i = 0; i < disk.length; i++){
			if(disk[i] == null) System.out.println("MISSING FILE"); //if all is right it should never go here
		}
				
	}
	
	
	

	public static void main(String[] args) {
		
		
		int[] missed = new int[numberTest];
		int[] allFiles = new int[numberTest];

		for(int size: disksizes){
			for(int[] range: ranges){
				Main test = new Main(size, range[0], range[1]);
				for(int i = 0; i < numberTest; i++){
					//testing is done here
					test = new Main(size, range[0], range[1]); //this here makes the number of files different each time
					
					test.ContiguousAllocation();
					missed[i] = test.numReject;
					allFiles[i] = test.totalFiles;
					test.LinkedAllocation();//linked allocation is always zero fragmentation unless it prints that statement at the end, then there is an error in the code
				
				}
				System.out.println("Disk size: " + test.disk.length + "  FileSize Range:(" + test.minFileSize + ", " + test.maxFileSize + ")");
				int j = 0;
				double total = 0;
				for(int miss: missed){
					total += ((double)miss/allFiles[j] ) * 100;
					System.out.print(((double)miss/allFiles[j++] ) * 100 + ", "); //get the percent 
					
				}
				System.out.println();
				System.out.println("Average percent missed: " +  total/numberTest);
				System.out.println();


				
			}
		}
		
	}

}
