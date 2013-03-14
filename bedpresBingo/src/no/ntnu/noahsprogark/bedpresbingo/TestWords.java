package no.ntnu.noahsprogark.bedpresbingo;

public class TestWords implements IServerCommunication {

	@Override
	public String[] getWordsFromServer() {
		String[] words = new String[16];
		for (int i = 0; i < words.length; i++) {
			words[i] = "Hello " + i;
		}
		return words;
	}

}
