import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axiosClient from '../api/axiosClient.js';
import Navbar from '../components/Navbar.jsx';

export default function MovieDetailPage() {
  const { movieId } = useParams();
  const [shows, setShows] = useState([]);
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  useEffect(() => {
    axiosClient.get(`/shows/movie/${movieId}`)
      .then((res) => setShows(res.data))
      .finally(() => setLoading(false));
  }, [movieId]);

  return (
    <>
      <Navbar />
      <div className="page">
        <h2>Available Shows</h2>
        {loading && <p className="muted">Loading shows...</p>}
        {!loading && shows.length === 0 && <p className="muted">No shows scheduled yet.</p>}
        {shows.map((show) => (
          <div key={show.id} className="show-card">
            <div className="details">
              <p><strong>{show.screen.theatre.name}</strong> — {show.screen.name}</p>
              <p className="muted">{new Date(show.startTime).toLocaleString()}</p>
              <p className="price">₹{show.basePrice} onwards</p>
            </div>
            <button onClick={() => navigate(`/shows/${show.id}/seats`)}>Select Seats</button>
          </div>
        ))}
      </div>
    </>
  );
}