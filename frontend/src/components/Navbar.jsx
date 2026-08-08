import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext.jsx';

export default function Navbar() {
  const { user, logout } = useAuth();
  return (
    <div className="navbar">
      <Link to="/movies" className="logo">MOVIO</Link>
      <div className="nav-links">
        {user ? (
          <>
            <Link to="/history">My Bookings</Link>
            <span className="muted">Hi, {user.name}</span>
            <button className="secondary" onClick={logout}>Logout</button>
          </>
        ) : (
          <Link to="/login">Login</Link>
        )}
      </div>
    </div>
  );
}