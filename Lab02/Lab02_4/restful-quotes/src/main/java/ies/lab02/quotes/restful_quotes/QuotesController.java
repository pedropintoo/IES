package ies.lab02.quotes.restful_quotes;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

@RestController
public class QuotesController 
{
	public static Map<Integer,Set<String>> quotesDict = Map.of(
		1, Set.of(
			"Just when I thought I was out, they pull me back in. - The Godfather III (1990)",
			"Life moves pretty fast. If you don't stop and look around once in a while, you could miss it. - Ferris Bueller's Day Off (1986)"
		),
		2, Set.of(
			"We're goin' streaking! - Old School (2003)"
		),
		3, Set.of(
			"I'm sorry father, for you there is only death. But our destiny is life! - The Fountain (2006)",
			"Sometimes it's people closest to us who lie to us best - Arrow (2015)",
			"Hope is a good thing, maybe the best of things, and no good thing ever dies. - Shawshank Redemption (1994)"
		),
		4, Set.of(
			"It's not about what I want. It's about what's fair! - The Dark Knight (2008)"
		),
		5, Set.of(
			"Life is not the amount of breaths you take, it's the moments that take your breath away. - Hitch (2005)"
		),
		6, Set.of(
			"One ring to rule them all. - The Lord of the Rings: The Fellowship of the Ring (2001)",
			"May the Force be with you. - Star Wars (1977)"
		)
	);

    public record QuotesRecord(Set<String> contents) { }
	public record ShowsRecord(Set<Integer> showsId) { }

	@GetMapping("/api/shows")
	public ShowsRecord api_shows() 
	{
		return new ShowsRecord(QuotesController.quotesDict.keySet());
	}

	@GetMapping("/api/quote")
	public QuotesRecord api_quote() 
	{
		int randomIdx = (int) (Math.random() * quotesDict.size()) + 1;
		return new QuotesRecord(QuotesController.quotesDict.get(randomIdx));
	}

	@GetMapping("/api/quotes")
	public QuotesRecord api_quote(@RequestParam(value="show", required=false, defaultValue="-1") int showID) 
	{
		if (!QuotesController.quotesDict.containsKey(showID)) {
			return new QuotesRecord(Set.of("Show not found. Usage: /api/quotes?show=<showID>"));
		}

		return new QuotesRecord(QuotesController.quotesDict.get(showID));
	}
}
