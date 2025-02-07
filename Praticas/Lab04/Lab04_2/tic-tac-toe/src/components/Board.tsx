import Square from "./Square";

interface BoardProps {
    boardWidth: number;
    xIsNext: boolean;
    squares: string[];
    onPlay: (squares: string[]) => void;
}

function calculatePossibleLines(boardWidth: number) {
  const lines = [];

  // Rows
  for (let row = 0; row < boardWidth; row++) {
    for (let col = 0; col <= boardWidth - 3; col++) {
      lines.push([row * boardWidth + col, row * boardWidth + col + 1, row * boardWidth + col + 2]);
    }
  }

  // Columns
  for (let col = 0; col < boardWidth; col++) {
    for (let row = 0; row <= boardWidth - 3; row++) {
      lines.push([row * boardWidth + col, (row + 1) * boardWidth + col, (row + 2) * boardWidth + col]);
    }
  }

  // Diagonals (top-left to bottom-right)
  for (let row = 0; row <= boardWidth - 3; row++) {
    for (let col = 0; col <= boardWidth - 3; col++) {
      lines.push([row * boardWidth + col, (row + 1) * boardWidth + col + 1, (row + 2) * boardWidth + col + 2]);
    }
  }

  // Diagonals (top-right to bottom-left)
  for (let row = 0; row <= boardWidth - 3; row++) {
    for (let col = 2; col < boardWidth; col++) {
      lines.push([row * boardWidth + col, (row + 1) * boardWidth + col - 1, (row + 2) * boardWidth + col - 2]);
    }
  }

  return lines;
}

function calculateWinner(squares: string[], boardWidth: number) {
  const lines = calculatePossibleLines(boardWidth);

  for (let i = 0; i < lines.length; i++) {
    const [a, b, c] = lines[i];
    if (squares[a] && squares[a] === squares[b] && squares[a] === squares[c]) {
      return squares[a];
    }
  }
  return null;
}

function Board({ boardWidth, xIsNext, squares, onPlay } : BoardProps) {
  function handleClick(clickedPos : number) {
    if (calculateWinner(squares, boardWidth) || squares[clickedPos]) {
      return;
    }
    const nextSquares = squares.slice();
    if (xIsNext) {
      nextSquares[clickedPos] = 'X';
    } else {
      nextSquares[clickedPos] = 'O';
    }
    onPlay(nextSquares); // update next allowed player
  }

  const winner = calculateWinner(squares, boardWidth);
  let status;
  if (winner) {
    status = 'Winner: ' + winner;
  } else {
    status = 'Next player: ' + (xIsNext ? 'X' : 'O');
  }

  return (
    <>
      <div style={{ fontSize:20 }}><b>Jogo do Galo</b></div>
      <div className="status">{status}</div>
      {[...Array(boardWidth)].map((_, row) => (
        <div className="board-row" key={row}>
          {[...Array(boardWidth)].map((_, col) => {
            const index = row * boardWidth + col;
            return (
        <Square
          key={index}
          value={squares[index]}
          onSquareClick={() => handleClick(index)}
        />
            );
          })}
        </div>
      ))}
    </>
  );
}


export default Board;