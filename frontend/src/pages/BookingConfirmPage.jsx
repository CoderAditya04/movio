import { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import axiosClient from '../api/axiosClient.js';
import Navbar from '../components/Navbar.jsx';

export default function BookingConfirmPage() {
  const { bookingId } = useParams();
  const [booking, setBooking] = useState(null);
  const [paying, setPaying] = useState(false);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handlePay = async () => {
    setError('');
    setPaying(true);
    try {
      const res = await axiosClient.post(`/bookings/${bookingId}/confirm`);
      setBooking(res.data);
      if (res.data.status === 'CONFIRMED') {
        navigate(`/bookings/${bookingId}/ticket`);
      }
    } catch (err) {
      setError(err.response?.data || 'Payment failed.');
    } finally {
      setPaying(false);
    }
  };

  return (
    <>
      <Navbar />
      <div className="page-narrow">
        <div className="form-card">
          <h2>Confirm & Pay</h2>
          <p className="muted">Booking #{bookingId}</p>
          <p className="muted">
            Simulated payment — this will take ~1.5s and has a small chance of failing, just like a real gateway.
          </p>

          {error && <p className="error-text">{error}</p>}
          {booking?.status === 'CANCELLED' && (
            <p className="error-text">
              Payment failed. Your seats have been released — please select again.
            </p>
          )}

          <button onClick={handlePay} disabled={paying} style={{ width: '100%' }}>
            {paying ? 'Processing payment...' : 'Pay Now'}
          </button>
        </div>
      </div>
    </>
  );
}