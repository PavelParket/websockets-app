import { useCallback, useEffect, useState } from "react";
import { Box, Button, Card, Container, Icon, Typography, useThemedIcon } from "../../ui";
import { useNavigate, useParams } from "react-router-dom";
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
   playersSymbols?: Record<string, Player>;
   currentPlayer?: Player;
   winner?: Player | "Draw" | null;
   message?: string;
}

export default function TicTaeToeRoom() {
   const { roomId } = useParams<{ roomId: string }>();
   const { accessToken } = useSelector((state: RootState) => state.auth);

   const navigate = useNavigate();

   const [board, setBoard] = useState<Cell[]>(Array(9).fill(null));
   const [currentPlayer, setCurrentPlayer] = useState<Player>("X");
   const [winner, setWinner] = useState<Player | "Draw" | null>(null);
   const [mySymbol, setMySymbol] = useState<Player | null>(null);
   const [ready, setReady] = useState(false);
   const [players, setPlayers] = useState<Record<string, Player>>({});

   const { getInverseIcon } = useThemedIcon();

   const handleWSMessage = useCallback((message: GameMessageResponse) => {
      console.log("TicTacToe WS message:", message);

      switch (message.type) {
         case "start":
            if (message.board)
               setBoard(message.board);

            if (message.currentPlayer)
               setCurrentPlayer(message.currentPlayer);

            if (message.player)
               setMySymbol(message.player);

            if (message.playersSymbols)
               setPlayers(message.playersSymbols);

            break;

         case "move":
            if (message.board)
               setBoard(message.board);

            if (message.currentPlayer)
               setCurrentPlayer(message.currentPlayer);

            if (message.winner !== undefined)
               setWinner(message.winner);

            break;

         case "system":
            if (message.message)
               alert(message.message);

            break;

         case "ready":
            console.log("Player ready:", message.fromUserId);
            break;

         case "leave":
            if (message.playersSymbols)
               setPlayers(message.playersSymbols);

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

   const handleLeave = () => {
      if (connected) {
         sendMessage({ type: "leave" });
      }

      navigate("/rooms");
   };

   return (
      <Box style={{
         minHeight: "calc(100vh - 60px - 50px)",
         margin: "0 10rem",
         padding: "0 1rem",
         background: "var(--color-bg-glass)",
         backdropFilter: "blur(2px)",
         borderRadius: "var(--radius-md)",
         boxShadow: "var(--shadow-lg)",
      }}
      >
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
               alignItems: "center",
            }}
            >
               <Typography variant="h3" style={{ margin: "2rem 0" }}>
                  {winner
                     ? winner === "Draw"
                        ? "Draw!"
                        : `Winner: ${winner}`
                     : `Turn: ${currentPlayer}`}
               </Typography>

               <Box
                  style={{
                     display: "grid",
                     gridTemplateColumns: "repeat(3, 1fr)",
                     alignItems: "center",
                     justifyContent: "center",
                  }}
               >
                  {/* Список игроков */}
                  <Box
                     style={{
                        marginRight: "5rem",
                        display: "flex",
                        flexDirection: "column",
                        alignItems: "center",
                        justifyContent: "center",
                        rowGap: "1.5rem",
                     }}
                  >
                     {Object.entries(players).map(([name, symbol]) => (
                        <Typography key={name} variant="h2">
                           {name}: {symbol}
                        </Typography>
                     ))}
                  </Box>

                  {/* Игровое поле */}
                  <Box
                     style={{
                        display: "grid",
                        gridTemplateColumns: "repeat(3, 80px)",
                        gridTemplateRows: "repeat(3, 80px)",
                        borderRadius: "var(--radius-lg)",
                        overflow: "hidden",
                        boxShadow: "var(--shadow-lg)",
                     }}
                  >
                     {board.map((cell, index) => {
                        const style: React.CSSProperties = {
                           width: "80px",
                           height: "80px",
                           fontSize: "32px",
                           fontWeight: "bold",
                           borderRadius: "0",
                           borderRight: "none",
                           borderBottom: "none",
                        };

                        if (index % 3 !== 2) style.borderRight = "2px solid var(--color-text)";
                        if (index < 6) style.borderBottom = "2px solid var(--color-text)";

                        return (
                           <Button
                              key={index}
                              variant="ghost"
                              style={style}
                              onClick={() => handleClick(index)}
                              disabled={!!cell || !!winner || currentPlayer !== mySymbol}
                           >
                              {cell}
                           </Button>
                        );
                     })}
                  </Box>
               </Box>

               {/* Кнопки управления */}
               <Box
                  style={{
                     width: "100%",
                     display: "grid",
                     gridTemplateColumns: "repeat(3, 1fr)",
                     alignItems: "center",
                     justifyItems: "center",
                  }}
               >
                  <Button variant="outline" onClick={handleLeave}>
                     Leave
                  </Button>

                  <Button
                     onClick={handleReady}
                     disabled={!connected || ready}
                     style={{
                        margin: "2rem",
                        display: "flex",
                        alignItems: "center",
                        gap: "8px",
                     }}
                  >
                     {ready ? (
                        <>
                           <Typography
                              variant="body"
                              inverse
                              style={{ fontSize: "20px", fontWeight: 500 }}
                           >
                              Ready
                           </Typography>
                           <Icon src={getInverseIcon("check")} alt="check" size={20} />
                        </>
                     ) : (
                        <Typography
                           variant="body"
                           inverse
                           style={{ fontSize: "20px", fontWeight: 500 }}
                        >
                           Get Ready
                        </Typography>
                     )}
                  </Button>
               </Box>
            </Card>
         </Container>
      </Box>
   );
}
