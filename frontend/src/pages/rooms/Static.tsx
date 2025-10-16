import { useState } from "react";
import { Box, Button, Card, Container, Icon, Typography, useThemedIcon } from "../../ui";

type Player = "X" | "O";
type Cell = Player | null;

interface User {
   name: string;
   symbol: string;
}

export default function Static() {

   const [board, setBoard] = useState<Cell[]>(Array(9).fill(null));
   const [currentPlayer, setCurrentPlayer] = useState<Player>("X");
   const [winner, setWinner] = useState<Player | "Draw" | null>(null);
   const [ready, setReady] = useState(false);
   const { getInverseIcon } = useThemedIcon();

   const players: User[] = [{ name: "Pasha", symbol: "X" }, { name: "Timur", symbol: "O" }];

   const handleClick = (index: number) => {
      if (board[index] || winner) return;

      const newBoard = [...board];
      newBoard[index] = currentPlayer;
      setBoard(newBoard);

      const lines = [
         [0, 1, 2], [3, 4, 5], [6, 7, 8],
         [0, 3, 6], [1, 4, 7], [2, 5, 8],
         [0, 4, 8], [2, 4, 6],
      ];

      for (const [a, b, c] of lines) {
         if (newBoard[a] && newBoard[a] === newBoard[b] && newBoard[a] === newBoard[c]) {
            setWinner(newBoard[a]);
            return;
         }
      }

      if (newBoard.every(cell => cell)) {
         setWinner("Draw");
         return;
      }

      setCurrentPlayer(currentPlayer === "X" ? "O" : "X");
   };

   const handleReady = () => {
      setReady(!ready);
   };

   return (
      <Box style={{
         minHeight: "calc(100vh - 60px - 50px)",
         margin: "0 10rem",
         padding: "0 1rem",
         background: "var(--color-bg-glass)",
         backdropFilter: "blur(2px)",
         borderRadius: "var(--radius-md)",
         boxShadow: "var(--shadow-lg)"
      }}>
         <Container>
            <Box style={{ padding: "2rem 0" }}>
               <Typography variant="h2" style={{ textAlign: "center" }}>
                  Tic-Tae-Toe
               </Typography>
            </Box>

            <Card style={{
               padding: "0",
               display: "flex",
               flexDirection: "column",
               alignItems: "center"
            }}>
               <Typography variant="h3" style={{ margin: "2rem 0" }}>
                  {winner
                     ? winner === "Draw"
                        ? "Draw!"
                        : `Winner: ${winner}`
                     : `Turn: ${currentPlayer}`
                  }
               </Typography>

               <Box style={{
                  display: "grid",
                  gridTemplateColumns: "repeat(3, 1fr)",
                  alignItems: "center",
                  justifyContent: "center",
               }}>
                  <Box style={{
                     marginRight: "5rem",
                     display: "flex",
                     flexDirection: "column",
                     alignItems: "center",
                     justifyContent: "center",
                     rowGap: "1.5rem",
                  }}>
                     {players.map((User, index) => (
                        <Typography
                           key={index}
                           variant="h2"
                        >
                           {User.name}: {User.symbol}
                        </Typography>
                     ))}
                  </Box>

                  <Box style={{
                     display: "grid",
                     gridTemplateColumns: "repeat(3, 80px)",
                     gridTemplateRows: "repeat(3, 80px)",
                     borderRadius: "var(--radius-lg)",
                     overflow: "hidden",
                     boxShadow: "var(--shadow-lg)",
                  }}>
                     {board.map((cell, index) => {
                        const style = {
                           width: "80px",
                           height: "80px",
                           fontSize: "32px",
                           fontWeight: "bold",
                           borderRadius: "0",
                           borderRight: "none",
                           borderBottom: "none",
                        };

                        if (index % 3 !== 2) {
                           style.borderRight = "2px solid var(--color-text)";
                        }

                        if (index < 6) {
                           style.borderBottom = "2px solid var(--color-text)";
                        }

                        return (
                           <Button
                              key={index}
                              variant="ghost"
                              style={style}
                              onClick={() => handleClick(index)}
                              disabled={!!cell || !!winner}
                           >
                              {cell}
                           </Button>
                        );
                     })}
                  </Box>
               </Box>

               <Box style={{
                  width: "100%",
                  display: "grid",
                  gridTemplateColumns: "repeat(3, 1fr)",
                  alignItems: "center",
                  justifyItems: "center",
               }}>
                  <Button variant="outline">Leave</Button>

                  <Button
                     onClick={handleReady}
                     disabled={ready}
                     style={{
                        margin: "2rem",
                        display: "flex",
                        alignItems: "center",
                        gap: "8px"
                     }}
                  >
                     {ready ? (
                        <>
                           <Typography variant="body" inverse style={{ fontSize: "20px", fontWeight: 500 }}>Ready</Typography>
                           <Icon src={getInverseIcon("check")} alt="check" size={20} />
                        </>
                     ) : (
                        <Typography variant="body" inverse style={{ fontSize: "20px", fontWeight: 500 }}>Get Ready</Typography>
                     )}
                  </Button>
               </Box>
            </Card>
         </Container>
      </Box >
   );
}
