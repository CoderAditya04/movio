import { Routes, Route, Navigate } from 'react-router-dom';
import LoginPage from './pages/LoginPage.jsx';
import RegisterPage from './pages/RegisterPage.jsx';
import MoviesPage from './pages/MoviesPage.jsx';
import MovieDetailPage from './pages/MovieDetailPage.jsx';
import SeatSelectionPage from './pages/SeatSelectionPage.jsx';
import BookingConfirmPage from './pages/BookingConfirmPage.jsx';
import TicketPage from './pages/TicketPage.jsx';
import BookingHistoryPage from './pages/BookingHistoryPage.jsx';
import { useAuth } from './context/AuthContext.jsx';

function ProtectedRoute({ children }) {
  const { user } = useAuth();
  if (!user) return <Navigate to="/login" replace />;
  return children;
}

function App() {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />
      <Route path="/movies" element={<MoviesPage />} />
      <Route path="/movies/:movieId" element={<MovieDetailPage />} />
      <Route
        path="/shows/:showId/seats"
        element={<ProtectedRoute><SeatSelectionPage /></ProtectedRoute>}
      />
      <Route
        path="/bookings/:bookingId/confirm"
        element={<ProtectedRoute><BookingConfirmPage /></ProtectedRoute>}
      />
      <Route
        path="/bookings/:bookingId/ticket"
        element={<ProtectedRoute><TicketPage /></ProtectedRoute>}
      />
      <Route
        path="/history"
        element={<ProtectedRoute><BookingHistoryPage /></ProtectedRoute>}
      />
      <Route path="*" element={<Navigate to="/movies" replace />} />
    </Routes>
  );
}

export default App;