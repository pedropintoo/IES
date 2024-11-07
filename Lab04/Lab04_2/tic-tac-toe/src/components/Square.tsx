interface SquareProps {
  value: string;
  onSquareClick: () => void;
}

function Square({ value, onSquareClick } : SquareProps) {
  return (
    <button 
    className="square" 
    onClick={onSquareClick}
    style={{backgroundColor: value === 'X' ? 'blue' : value === 'O' ? 'red' : 'white'}}
    >
      {value}
    </button>
  );
}

export default Square;