import { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext.jsx';

export default function RegisterPage() {
  const [name, setName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const { register } = useAuth();
  const navigate = useNavigate();

  const isValidGmail = (email) => /^[a-zA-Z0-9._%+-]+@gmail\.com$/.test(email);

const handleSubmit = async (e) => {
  e.preventDefault();
  setError('');

  if (!isValidGmail(email)) {
    setError('Please use a valid @gmail.com email address.');
    return;
  }

  setLoading(true);
  try {
    await register(name, email, password);
    navigate('/movies');
  } catch (err) {
    setError(err.response?.data?.email || err.response?.data || 'Registration failed.');
  } finally {
    setLoading(false);
  }
};

  return (
    <div className="page-narrow">
      <div className="form-card">
        <h2>Create your Movio account</h2>
        <form onSubmit={handleSubmit}>
          <input
            type="text"
            placeholder="Name"
            value={name}
            onChange={(e) => setName(e.target.value)}
            required
          />
          <input
            type="email"
            placeholder="Email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />
          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />
          {error && <p className="error-text">{String(error)}</p>}
          <button type="submit" disabled={loading} style={{ width: '100%' }}>
            {loading ? 'Creating account...' : 'Register'}
          </button>
        </form>
        <p className="muted" style={{ marginTop: 16 }}>
          Already have an account? <Link to="/login" style={{ color: 'var(--primary)' }}>Login here</Link>
        </p>
      </div>
    </div>
  );
}