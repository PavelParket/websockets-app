import { useCallback, useEffect, useState } from "react";
import { useSelector } from "react-redux";
import { useParams } from "react-router-dom";
import { useWebSocket } from "../../services/hooks/useWebSocket";
import type { RootState } from "../../store/store";
import { Box, Button, Card, Container, Textfield, Typography } from "../../ui";
import { getUsersInRoom } from "../../services/mediatorService";

interface MessageResponse {
   type: string;
   fromUserId: string;
   toUserId?: string | null;
   roomId: string;
   content: string;
}

interface MessageRequest {
   type: string;
   toUserId?: string | null;
   content: string;
}

export default function ChatRoom() {
   const { roomId } = useParams<{ roomId: string }>();
   const { accessToken } = useSelector((state: RootState) => state.auth);
   const [input, setInput] = useState("");
   const [participants, setParticipants] = useState<string[]>([]);

   const fetchParticipants = useCallback(async () => {
      if (!roomId) {
         return;
      }

      try {
         const users = await getUsersInRoom(roomId);
         setParticipants(users);
      } catch (e) {
         console.log("Error fetching users", e);
      }
   }, [roomId]);

   const handleWSMessage = useCallback((message: MessageResponse) => {
      console.log("WS message", message);

      if (message.type === "system") {
         fetchParticipants();
      }
   }, [fetchParticipants]);

   const { messages, sendMessage, connected } = useWebSocket<MessageResponse, MessageRequest>({
      roomId: roomId!,
      token: accessToken!,
      onMessage: handleWSMessage,
   });

   useEffect(() => {
      fetchParticipants();
   }, [fetchParticipants]);

   const handleSend = () => {
      if (!input.trim()) {
         return;
      }

      const message: MessageRequest = {
         type: "message",
         toUserId: null,
         content: input.trim(),
      };

      sendMessage(message);
      setInput("");
   };

   return (
      <Box style={{ padding: "56px 0" }}>
         <Container>
            <Typography variant="h2" style={{ marginBottom: "20px", textAlign: "center" }}>
               Room: {roomId}
            </Typography>
            <Typography variant="body" style={{ marginBottom: "10px" }}>
               Connection: {connected ? "🟢 Connected" : "🔴 Disconnected"}
            </Typography>

            <Box style={{ display: "flex", gap: "20px" }}>
               <Card style={{ width: "200px", padding: "16px" }}>
                  <Typography variant="h3">Participants</Typography>
                  {participants.map((user) => (
                     <Typography key={user}>{user}</Typography>
                  ))}
               </Card>

               <Card style={{ flex: 1, padding: "16px", display: "flex", flexDirection: "column", gap: "10px" }}>
                  <Typography variant="h3">Chat</Typography>
                  <Box style={{ flex: 1, overflowY: "auto", border: "1px solid var(--color-border)", padding: "8px" }}>
                     {messages.map((msg, index) => (
                        <Typography key={index}>
                           <b>{msg.fromUserId}:</b> {msg.content}
                        </Typography>
                     ))}
                  </Box>
                  <Box style={{ display: "flex", gap: "8px" }}>
                     <Textfield
                        placeholder="Type your message..."
                        value={input}
                        onChange={setInput}
                        rounded
                        disabled={!connected}
                     />
                     <Button
                        variant="solid"
                        onClick={handleSend}
                        disabled={!connected || !input.trim()}
                     >
                        Send
                     </Button>
                  </Box>
               </Card>
            </Box>
         </Container>
      </Box>
   );
}
