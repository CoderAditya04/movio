export default function SeatMap({ seats, selectedSeatIds, onToggleSeat }) {
  const rows = {};
  seats.forEach((s) => {
    const row = s.seat.rowLabel;
    if (!rows[row]) rows[row] = [];
    rows[row].push(s);
  });

  const getClass = (seat) => {
    if (selectedSeatIds.includes(seat.id)) return 'seat selected';
    if (seat.status === 'BOOKED') return 'seat booked';
    if (seat.status === 'LOCKED') return 'seat locked';
    return 'seat available';
  };

  const isClickable = (seat) => seat.status === 'AVAILABLE' || selectedSeatIds.includes(seat.id);

  return (
    <div>
      <div className="legend">
        <span><span className="dot" style={{ background: '#3a3f52' }} /> Available</span>
        <span><span className="dot" style={{ background: 'var(--success)' }} /> Selected</span>
        <span><span className="dot" style={{ background: 'var(--danger)' }} /> Locked</span>
        <span><span className="dot" style={{ background: '#4a4d59' }} /> Booked</span>
      </div>
      {Object.entries(rows).map(([rowLabel, rowSeats]) => (
        <div key={rowLabel} className="seat-row">
          <span className="row-label">{rowLabel}</span>
          {rowSeats
            .sort((a, b) => a.seat.seatNumber - b.seat.seatNumber)
            .map((seat) => (
              <button
                key={seat.id}
                disabled={!isClickable(seat)}
                onClick={() => onToggleSeat(seat)}
                className={getClass(seat)}
                title={`${rowLabel}${seat.seat.seatNumber} — ${seat.status}`}
              />
            ))}
        </div>
      ))}
    </div>
  );
}