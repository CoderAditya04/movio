import { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import axiosClient from '../api/axiosClient.js';
import Navbar from '../components/Navbar.jsx';

export default function TicketPage() {
  const { bookingId } = useParams();
  const [qrUrl, setQrUrl] = useState(null);
  const [error, setError] = useState('');

  useEffect(() => {
    let objectUrl;
    axiosClient.get(`/bookings/${bookingId}/qr`, { responseType: 'blob' })
      .then((res) => {
        objectUrl = URL.createObjectURL(res.data);
        setQrUrl(objectUrl);
      })
      .catch(() => setError('Could not load ticket QR code.'));

    return () => {
      if (objectUrl) URL.revokeObjectURL(objectUrl);
    };
  }, [bookingId]);

  return (
    <>
      <Navbar />
      <div className="ticket-box">
        <h2>🎬 Booking Confirmed!</h2>
        <p className="muted">Booking #{bookingId}</p>
        {error && <p className="error-text">{error}</p>}
        {qrUrl && <img src={qrUrl} alt="Ticket QR Code" width={250} height={250} />}
        <p className="muted">Show this QR code at the theatre entrance.</p>
      </div>
    </>
  );
}