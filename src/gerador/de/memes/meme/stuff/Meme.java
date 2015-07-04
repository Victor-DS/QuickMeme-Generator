package gerador.de.memes.meme.stuff;

public class Meme {

	private String id, title, link;

	public Meme(String id, String title, String link) {
		this.id = id;
		this.title = title;
		this.link = link;
	}
	
	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getLink() {
		return link;
	}
	
}
