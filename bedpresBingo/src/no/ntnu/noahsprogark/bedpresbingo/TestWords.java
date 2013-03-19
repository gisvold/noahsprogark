package no.ntnu.noahsprogark.bedpresbingo;

public class TestWords implements IServerCommunication {

	@Override
	public String[] getWordsFromServer() {
		String[] words = { "Kvinneandel", "Sommerjobb", "Fagdag",
				"Lønningspils", "Sosialt", "Java", "Norges beste arbeidsplass",
				"Toppturer", "Internasjonalt selskap", "Kvinneandel",
				"Sommerjobb", "Fagdag", "Lønningspils", "Sosialt", "Java",
				"Norges beste arbeidsplass", "Toppturer",
				"Internasjonalt selskap", "Kvinneandel", "Sommerjobb",
				"Fagdag", "Lønningspils", "Sosialt", "Java",
				"Norges beste arbeidsplass" };
		// String[] words = new String[25];
		// for (int i = 0; i < words.length; i++) {
		// words[i] = "Hello " + i;
		// }
		return words;
	}

	@Override
	public void joinGame() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getSessionID() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getWordsForPlayer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean registerWord() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void getStatus() {
		// TODO Auto-generated method stub
		
	}

}
