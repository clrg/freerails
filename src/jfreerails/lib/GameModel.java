package jfreerails.lib;

public interface GameModel {
	
	public static final GameModel NULL_MODEL=new GameModel(){
		public void update(){};
	};
	
	void update();

}