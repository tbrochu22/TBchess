// MovePiece.java
// Tyler Brochu
package ser120.TBchess.game;

public abstract class MovePiece{
	protected int row;
	protected int col;

	public MovePiece(int row, int col){
		this.row = row;
		this.col = col;
	}

	public int getRow(){
		return row;
	}

	public int getCol(){
		return col;
	}

	public void setPosition(int row, int col){
		this.row = row;
		this.col = col;
	}

	protected int[] convert(String alg) {
		if(alg == null || alg.length() != 2)
			return new int[]{-1, -1};

		char alph = alg.charAt(0); // a-h
		char num = alg.charAt(1); // 1-8

		if(alph < 'a' || alph > 'h' || num < '1' || num > '8')
			return new int[]{-1, -1};

		int col = alph - 'a';
		int row = 8 - (num - '0');

		return new int[]{row, col};
	}

	protected boolean updatePosition(int targetRow, int targetCol){
		row = targetRow;
		col = targetCol;
		return true;
	}

	public abstract boolean move(String algebraic);
}
