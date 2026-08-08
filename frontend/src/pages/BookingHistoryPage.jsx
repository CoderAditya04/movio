import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import axiosClient from '../api/axiosClient.js';
import Navbar from '../components/Navbar.jsx';

export default function BookingHistoryPage() {
  const [bookings, setBookings] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    axiosClient.get('/bookings/history')
      .then((res) => setBookings(res.data))
      .finally(() => setLoading(false));
  }, []);

  return (
    <>
      <Navbar />
      <div className="page">
        <h2>My Bookings</h2>
        {loading && <p className="muted">Loading...</p>}
        {!loading && bookings.length === 0 && <p className="muted">No bookings yet.</p>}
        {bookings.map((b) => (
          <div key={b.bookingId} className="card" style={{ marginBottom: 12 }}>
            <div className="flex-between">
              <strong>{b.movieTitle}</strong>
              <span className={`badge ${b.status}`}>{b.status}</span>
            </div>
            <p className="muted">{b.theatreName} — {b.screenName}</p>
            <p className="muted">{new Date(b.showTime).toLocaleString()}</p>
            <div className="flex-between">
              <p>Seats: {b.seatLabels.join(', ')} — ₹{b.totalAmount}</p>
              {b.status === 'CONFIRMED' && (
                <Link to={`/bookings/${b.bookingId}/ticket`}>
                  <button className="secondary">View Ticket</button>
                </Link>
              )}
            </div>
          </div>
        ))}
      </div>
    </>
  );
}