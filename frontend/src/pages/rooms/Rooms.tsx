import { useEffect, useState } from "react";
import { Box, Button, Card, Container, Modal, Typography } from "../ui";
import { useSelector, useDispatch } from "react-redux";
import type { RootState, AppDispatch } from "../store/store";
import { fetchRooms } from "../store/feature/roomsSlice";
import { useNavigate } from "react-router-dom";

export default function Rooms() {
   const dispatch = useDispatch<AppDispatch>();
   const navigate = useNavigate();
   const { rooms, loading, error } = useSelector((state: RootState) => state.rooms);
   const { accessToken } = useSelector((state: RootState) => state.auth);

   const [modalOpen, setModalOpen] = useState(false);
   const [modalContent, setModalContent] = useState<string>("");

   useEffect(() => {
      dispatch(fetchRooms());
   }, [dispatch]);

   const handleJoinRoom = (roomId: string) => {
      if (!accessToken) {
         return;
      }

      navigate(`/room/${roomId}`);
   };

   const handleInfo = (room: string) => {
      setModalContent(`Information about room: ${room}`);
      setModalOpen(true);
   };

   if (loading) return <Typography>Loading rooms...</Typography>;
   if (error) return <Typography color="red">{error}</Typography>;

   return (
      <Box style={{ padding: "56px 0" }}>
         <Container>
            <Typography variant="h2" style={{ marginBottom: "20px", textAlign: "center" }}>
               Rooms
            </Typography>
            <Box style={{
               display: "grid",
               gridTemplateColumns: "repeat(auto-fill, minmax(180px, 1fr))",
               gap: "16px",
               justifyItems: "center"
            }}>
               {rooms.map((room) => (
                  <Card key={room}
                     style={{
                        width: "180px",
                        textAlign: "center",
                        padding: "20px",
                        display: "flex",
                        flexDirection: "column",
                        gap: "10px",
                     }}>
                     <Typography variant="body">{room}</Typography>
                     <Button variant="solid" onClick={() => handleJoinRoom(room)}>Join</Button>
                     <Button variant="outline" onClick={() => handleInfo(room)}>Info</Button>
                  </Card>
               ))}
            </Box>
         </Container>

         <Modal isOpen={modalOpen} onClose={() => setModalOpen(false)} title="Room Info">
            <Typography>{modalContent}</Typography>
         </Modal>
      </Box>
   );
}
