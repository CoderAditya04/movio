import { useEffect, useState, useCallback } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axiosClient from '../api/axiosClient.js';
import SeatMap from '../components/SeatMap.jsx';
import Navbar from '../components/Navbar.jsx';

export default function SeatSelectionPage() {
  const { showId } = useParams();
  const [seats, setSeats] = useState([]);
  const [selectedSeatIds, setSelectedSeatIds] = useState([]);
  const [locking, setLocking] = useState(false);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const fetchSeats = useCallback(() => {
    axiosClient.get(`/shows/${showId}/seats`).then((res) => setSeats(res.data));
  }, [showId]);

  useEffect(() => {
    fetchSeats();
    const interval = setInterval(fetchSeats, 5000);
    return () => clearInterval(interval);
  }, [fetchSeats]);

  const toggleSeat = (seat) => {
    setSelectedSeatIds((prev) =>
      prev.includes(seat.id) ? prev.filter((id) => id !== seat.id) : [...prev, seat.id]
    );
  };

  const handleProceed = async () => {
    setError('');
    setLocking(true);
    try {
      await axiosClient.post('/seats/lock', { showId: Number(showId), seatIds: selectedSeatIds });
      const bookingRes = await axiosClient.post('/bookings', {
        showId: Number(showId),
        showSeatIds: selectedSeatIds,
      });
      navigate(`/bookings/${bookingRes.data.id}/confirm`);
    } catch (err) {
      setError(err.response?.data || 'Could not lock seats — someone may have taken one. Refreshing...');
      fetchSeats();
      setSelectedSeatIds([]);
    } finally {
      setLocking(false);
    }
  };

  const totalPrice = seats
    .filter((s) => selectedSeatIds.includes(s.id))
    .reduce((sum, s) => sum + s.price, 0);

  return (
    <>
      <Navbar />
      <div className="page">
        <h2>Select Your Seats</h2>
        <SeatMap seats={seats} selectedSeatIds={selectedSeatIds} onToggleSeat={toggleSeat} />
        <div className="booking-bar">
          <p>Selected: {selectedSeatIds.length} seat(s) — <strong>₹{totalPrice}</strong></p>
          {error && <p className="error-text">{error}</p>}
          <button
            disabled={selectedSeatIds.length === 0 || locking}
            onClick={handleProceed}
          >
            {locking ? 'Locking seats...' : 'Proceed to Payment'}
          </button>
        </div>
      </div>
    </>
  );
}