import { useCallback, useEffect, useState } from "react";
import { Box, Button, Card, Container, Typography } from "../../ui";
import { useParams } from "react-router-dom";
import { useSelector } from "react-redux";
import type { RootState } from "../../store/store";
import { useWebSocket } from "../../services/hooks/useWebSocket";

type Player = "X" | "O";
type Cell = Player | null;

interface GameMessageRequest {
   type: "join" | "leave" | "ready" | "move";
   player?: Player;
   cell?: number;
}

interface GameMessageResponse {
   type: "join" | "leave" | "ready" | "start" | "move" | "system";
   fromUserId?: string;
   roomId?: string;
   player?: Player;
   cell?: number;
   board?: (Player | null)[];
   currentPlayer?: Player;
   winner?: Player | "Draw" | null;
   message?: string;
}

export default function TicTaeToeRoom() {
   const { roomId } = useParams<{ roomId: string }>();
   const { accessToken } = useSelector((state: RootState) => state.auth);

   const [board, setBoard] = useState<Cell[]>(Array(9).fill(null));
   const [currentPlayer, setCurrentPlayer] = useState<Player>("X");
   const [winner, setWinner] = useState<Player | "Draw" | null>(null);
   const [mySymbol, setMySymbol] = useState<Player | null>(null);
   const [ready, setReady] = useState(false);

   const handleWSMessage = useCallback((message: GameMessageResponse) => {
      console.log("TicTacToe WS message:", message);

      switch (message.type) {
         case "start":
            if (message.board) setBoard(message.board);
            if (message.currentPlayer) setCurrentPlayer(message.currentPlayer);
            if (message.player) {
               setMySymbol(message.player);
            }
            break;

         case "move":
            if (message.board) setBoard(message.board);
            if (message.currentPlayer) setCurrentPlayer(message.currentPlayer);
            if (message.winner !== undefined) setWinner(message.winner);
            break;

         case "system":
            if (message.message) {
               alert(message.message);
            }
            break;

         case "ready":
            console.log("Player ready:", message.fromUserId);
            break;

         default:
            break;
      }
   }, []);

   const { sendMessage, connected } = useWebSocket<GameMessageResponse, GameMessageRequest>({
      roomId: roomId!,
      token: accessToken!,
      onMessage: handleWSMessage,
   });

   useEffect(() => {
      if (connected) {
         sendMessage({ type: "join" });
      }
      return () => {
         if (connected) {
            sendMessage({ type: "leave" });
         }
      };
   }, [connected, sendMessage]);

   const handleClick = (index: number) => {
      if (!connected || board[index] || winner || currentPlayer !== mySymbol) {
         return;
      }

      sendMessage({ type: "move", player: mySymbol!, cell: index });
   };

   const handleReady = () => {
      if (!connected || ready) {
         return;
      }

      sendMessage({ type: "ready" });
      setReady(true);
   };

   return (
      <Box style={{ padding: "56px 0" }}>
         <Container>
            <Typography variant="h2" style={{ textAlign: "center", marginBottom: "20px" }}>
               Tic-Tae-Toe
            </Typography>

            <Card style={{ padding: "16px", display: "flex", flexDirection: "column", alignItems: "center" }}>
               <Typography variant="h3" style={{ marginBottom: "16px" }}>
                  {winner
                     ? winner === "Draw"
                        ? "Draw!"
                        : `Winner: ${winner}`
                     : `Turn: ${currentPlayer}`
                  }
               </Typography>

               <Box style={{
                  display: "grid",
                  gridTemplateColumns: "repeat(3, 80px)",
                  gridTemplateRows: "repeat(3,80px)",
                  gap: "4px"
               }}>
                  {board.map((cell, index) => (
                     <Button
                        key={index}
                        variant="outline"
                        style={{
                           width: "80px",
                           height: "80px",
                           fontSize: "32px",
                           fontWeight: "bold",
                        }}
                        onClick={() => handleClick(index)}
                        disabled={!!cell || !!winner || currentPlayer !== mySymbol}
                     >
                        {cell}
                     </Button>
                  ))}
               </Box>

               <Button
                  onClick={handleReady}
                  disabled={!connected || ready}
               >
                  {ready ? "Ready ✔" : "Ready"}
               </Button>
            </Card>
         </Container>
      </Box>
   );
}
