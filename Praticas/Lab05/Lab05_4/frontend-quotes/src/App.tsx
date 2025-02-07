import { useEffect, useState } from "react";
import "./App.css";
import {
  getAllQuotes,
  getAllMovies,
  getQuoteByMovieId,
  getRandomQuote,
  Quote,
  Movie,
  postMovie,
  postQuote
} from "./consumers/apiConsumer";
import { useSubscription } from "react-stomp-hooks";

function App() {
  const [result, setResult] = useState<Quote[] | Movie[] | never[]>([]);
  const [movieId, setMovieId] = useState<number | undefined>();

  const [quote, setQuote] = useState<string | undefined>();
  const [quoteMovieId, setQuoteMovieId] = useState<number | undefined>();

  const [title, setTitle] = useState<string | undefined>();
  const [year, setYear] = useState<number | undefined>();

  const [clickDescription, setClickDescription] = useState<string | undefined>();

  const [realTimeQuotes, setRealTimeQuotes] = useState<Quote[] | never[]>([]);
  const [isRealTime, setIsRealTime] = useState<boolean>(false);

  let limit = 5;
  useSubscription("/quotes", (message) => {
    setRealTimeQuotes((prevQuotes) => {
      const newQuotes = [...prevQuotes, JSON.parse(message.body)];
      return newQuotes.length > limit ? newQuotes.slice(-limit) : newQuotes;
    });
  });

  useEffect(() => {
    if (clickDescription === "> Real Time Quotes") {
      setIsRealTime(true);
    } else {
      setIsRealTime(false);
    }
  }, [clickDescription]);

  return (
    <>
      <div>
        <h3>Get Movies and Quotes</h3>
        <button onClick={async () => {
          setResult(await getAllMovies());
          setClickDescription("> All Movies");
          }
        }>
          Get All Movies
        </button>
        <button onClick={async () => {
          setResult(await getAllQuotes());
          setClickDescription("> All Quotes");
          }
        }>
          Get All Quotes
        </button>
        <div>
          <input
            type="number"
            value={movieId}
            onChange={(e) => setMovieId(Number(e.target.value))}
            placeholder="Enter Movie ID"
          />
          <button
            onClick={async () => {
              movieId !== undefined &&
              setResult(await getQuoteByMovieId(movieId));
              setClickDescription(`> Quotes for Movie ID: ${movieId}`);
            }
            }>
            Get Quote by movieId
          </button>
        </div>
        <button onClick={async () => {
          setResult(await getRandomQuote());
          setClickDescription("> Random Quote");
        }
        }>
          Get Random Quote
        </button>
        <br />
        <button onClick={async () => {
          setClickDescription("> Real Time Quotes");
        }
        }>
          Get Real Time Quotes
        </button>
        <br />
        <h3>Create Movie and Quote</h3>
        <div>
          <input
            type="text"
            value={quote}
            onChange={(e) => setQuote(e.target.value)}
            placeholder="Enter Quote"
          />
          <input
            type="number"
            value={quoteMovieId}
            onChange={(e) => setQuoteMovieId(Number(e.target.value))}
            placeholder="Enter Movie ID"
          />
          <button
            onClick={async () => {
              quote !== undefined &&
              quoteMovieId !== undefined &&
              setResult(await postQuote(quote, quoteMovieId));
              setClickDescription(`> Quote: ${quote} for Movie ID: ${quoteMovieId}`);
            }}
          >
            Create Quote
          </button>
        </div>
        <div>
          <input
            type="text"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            placeholder="Enter Title"
          />
          <input
            type="number"
            value={year}
            onChange={(e) => setYear(Number(e.target.value))}
            placeholder="Enter Year"
          />
          <button
              onClick={async () =>{
                title !== undefined &&
                year !== undefined &&
                setResult(await postMovie(title, year));
                setClickDescription(`> Movie: ${title} (${year})`);
              }}
          >
            Create Movie
          </button>
        </div>
        <div>
          <h3>{clickDescription}</h3>
          <pre>
            {JSON.stringify(isRealTime ? realTimeQuotes : result, null, 2)}
            </pre>
        </div>
      </div>
    </>
  );
}

export default App;
