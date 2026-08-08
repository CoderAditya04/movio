import { Link } from 'react-router-dom';

export default function MovieCard({ movie }) {
  return (
    <Link to={`/movies/${movie.id}`} className="movie-card">
      <img
        src={movie.posterUrl || 'https://placehold.co/180x270?text=No+Poster'}
        alt={movie.title}
      />
      <p className="title">{movie.title}</p>
      <p className="genre">{movie.genre}</p>
    </Link>
  );
}