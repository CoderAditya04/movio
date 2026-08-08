import { useEffect, useState } from 'react';
import axiosClient from '../api/axiosClient.js';
import MovieCard from '../components/MovieCard.jsx';
import Navbar from '../components/Navbar.jsx';

export default function MoviesPage() {
  const [movies, setMovies] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    axiosClient.get('/movies')
      .then((res) => setMovies(res.data))
      .catch(() => setError('Failed to load movies.'))
      .finally(() => setLoading(false));
  }, []);

  return (
    <>
      <Navbar />
      <div className="page">
        <h1>Now Showing</h1>
        {loading && <p className="muted">Loading movies...</p>}
        {error && <p className="error-text">{error}</p>}
        <div className="grid">
          {movies.map((movie) => (
            <MovieCard key={movie.id} movie={movie} />
          ))}
        </div>
      </div>
    </>
  );
}