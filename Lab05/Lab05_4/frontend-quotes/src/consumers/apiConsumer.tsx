import axios from "axios";

interface Quote {
    id: number;
    quote: string;
    movieId: number;
    movieTitle: string;
    movieYear: number;
}
interface Movie {
    id: number;
    title: string;
    year: number;
}

const client = axios.create({
    baseURL: "http://localhost:8080/api",
    withCredentials: true,
});

// GET with Axios
const getAxiosClient = async function (endpoint: string) {
    try {
        const response = await client.get(endpoint);
        return response.data;
    } catch (error: any) {
        console.error("Error fetching data from", endpoint, error);
        if (error.response) {
            // Server responded with a status other than 2xx
            console.error("Error response data:", error.response.data);
            alert(`Error: ${error.response.status} - ${error.response.data.message || "An error occurred"}`);
        } else {
            // Network or other error
            alert("Network error or server unavailable");
        }
        return []; // Return in case of error
    }
};

// GET quotes
const getAllQuotes = async function () {
    const data = await getAxiosClient('/quotes');
    if (data) {
        return data as Quote[];
    }
    return [];
};

// GET movies
const getAllMovies = async function () {
    const data = await getAxiosClient('/movies');
    if (data) {
        return data as Movie[];
    }
    return [];
};

// GET quote by movieId
const getQuoteByMovieId = async function (movieId: number) {
    const data = await getAxiosClient(`/quotes?movie=${movieId}`);
    if (data) {
        return data as Quote[];
    }
    return [];
};

// GET random quote
const getRandomQuote = async function () {
    const data = await getAxiosClient('/random/quote');
    if (data) {
        return data as Quote[];
    }
    return [];
};



// POST with Axios
const postAxiosClient = async function (endpoint: string, data: any) {
    try {
        const response = await client.post(endpoint, data);
        return response.data;
    } catch (error: any) {
        console.error("Error posting data to", endpoint, error);
        if (error.response) {
            // Server responded with a status other than 2xx
            console.error("Error response data:", error.response.data);
            alert(`Error: ${error.response.status} - ${error.response.data.message || "An error occurred"}`);
        } else {
            // Network or other error
            alert("Network error or server unavailable");
        }
        return []; // Return in case of error
    }
};


// POST movie
const postMovie = async function (title: string, year: number) {
    const data = await postAxiosClient('/movies', { title, year }); 
    if (data) {
        return data as Movie[];
    }
    return [];
};

// POST quote
const postQuote = async function (quote: string, movieId: number) {
    const data = await postAxiosClient('/quotes', { quote, movieId });
    if (data) {
        return data as Quote[];
    }
    return [];
};

export { getAllQuotes, getAllMovies, getQuoteByMovieId, getRandomQuote, postMovie, postQuote };
export type { Quote, Movie };
