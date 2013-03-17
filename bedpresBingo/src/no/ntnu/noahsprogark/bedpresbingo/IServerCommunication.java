package no.ntnu.noahsprogark.bedpresbingo;

public interface IServerCommunication {
	
	/**
	 * Method to call for getting the word list from the server
	 * @return A String array containing the words.
	 */
	public String[] getWordsFromServer();
	
	public void joinGame();
	
	public String getSessionID();
	
	public String[] getWordsForPlayer();
	
	public Boolean registerWord();
	
	public void getStatus();
}
