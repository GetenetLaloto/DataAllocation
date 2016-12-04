/**
 * this class represents a piece of a data from a file
 * it is just a regular node
 * @author josuerojas
 *
 */

public class DataPiece {
	
	DataPiece next; //the next DataPiece
	int index; //what index is it located in
	
	public DataPiece(DataPiece next){
		this.next = next;
		//this.index = index;
	}
	
	public DataPiece(){
	}

	
}
