import { useEffect, useState } from "react";
import { Box, Button, Card, Container, Icon, Modal, Textfield, Typography, useThemedIcon } from "../../ui";
import { useSelector, useDispatch } from "react-redux";
import type { RootState, AppDispatch } from "../../store/store";
import { fetchRooms } from "../../store/feature/roomsSlice";
import { useNavigate } from "react-router-dom";

export default function Rooms() {
   const dispatch = useDispatch<AppDispatch>();
   const navigate = useNavigate();
   const { rooms, loading, error } = useSelector((state: RootState) => state.rooms);
   const { accessToken } = useSelector((state: RootState) => state.auth);

   const [modalOpen, setModalOpen] = useState(false);
   const [modalContent, setModalContent] = useState<string>("");
   const [createModalOpen, setCreateModalOpen] = useState(false);
   const [newRoomName, setNewRoomName] = useState("");
   const { getIcon, getInverseIcon } = useThemedIcon();

   useEffect(() => {
      dispatch(fetchRooms());
   }, [dispatch]);

   const handleJoinRoom = (roomId: string) => {
      if (!accessToken) {
         return;
      }

      navigate(`/room/game/${roomId}`);
   };

   const handleInfo = (room: string) => {
      setModalContent(`Information about room: ${room}`);
      setModalOpen(true);
   };

   const handleCreateRoom = () => {
      if (!newRoomName.trim()) {
         return;
      }

      setCreateModalOpen(false);
      navigate(`/room/game/${newRoomName}`);
   };

   if (loading) return <Typography>Loading rooms...</Typography>;
   if (error) return <Typography color="red">{error}</Typography>;

   const allRooms: string[] = [
      ...(rooms.chatRooms ?? []),
      ...(rooms.gameRooms ?? []),
   ];

   return (
      <>
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
               <Box style={{
                  padding: "2rem 1rem 0 1rem",
                  marginBottom: "2rem",
                  display: "flex",
                  flexDirection: "row",
                  alignItems: "center",
                  justifyContent: "space-between"
               }}>
                  <Typography variant="h2" style={{ textAlign: "center" }}>
                     Rooms
                  </Typography>

                  <Button
                     variant="solid"
                     onClick={() => setCreateModalOpen(true)}
                     style={{
                        display: "flex",
                        alignItems: "center",
                        gap: "8px"
                     }}
                  >
                     <Icon src={getInverseIcon("add")} alt="add" size={17} />
                     <Box style={{ textAlign: "center" }}>
                        <Typography variant="body" inverse style={{ fontSize: "16px", fontWeight: 500 }}>
                           Create Room
                        </Typography>
                     </Box>
                  </Button>
               </Box>

               <Box style={{ textAlign: "center" }}>
                  {allRooms.length === 0 && (
                     <Typography>No rooms available. Try to create something!</Typography>
                  )}
               </Box>

               <Box style={{
                  paddingBottom: "1rem",
                  display: "grid",
                  gridTemplateColumns: "repeat(4, 1fr)",
                  columnGap: "16px",
                  rowGap: "3rem",
                  justifyItems: "center",
               }}>
                  {allRooms.map((room) => (
                     <>
                        <Card
                           key={room}
                           style={{
                              width: "180px",
                              height: "180px",
                              textAlign: "center",
                              padding: "20px",
                              display: "flex",
                              flexDirection: "column",
                              gap: "10px",
                           }}
                        >
                           <Typography variant="body">{room}</Typography>
                           <Button variant="outline" onClick={() => handleJoinRoom(room)}>Join</Button>
                           <Button variant="ghost" onClick={() => handleInfo(room)}>Info</Button>
                        </Card>

                        {allRooms.length > 0 && (
                           <Card
                              onClick={() => setCreateModalOpen(true)}
                              style={{
                                 width: "180px",
                                 height: "180px",
                                 textAlign: "center",
                                 padding: "20px",
                                 display: "flex",
                                 alignItems: "center",
                                 justifyContent: "center",
                                 cursor: "pointer",
                              }}
                           >
                              <Icon src={getIcon("add")} alt="add" size={50} />
                           </Card>
                        )}
                     </>
                  ))}
               </Box>
            </Container>
         </Box>

         <Modal isOpen={modalOpen} onClose={() => setModalOpen(false)} title="Room Info">
            <Typography>{modalContent}</Typography>
         </Modal>

         <Modal isOpen={createModalOpen} onClose={() => setCreateModalOpen(false)} title="Create Room">
            <Box style={{ display: "flex", flexDirection: "column", gap: "16px" }}>
               <Textfield value={newRoomName} onChange={setNewRoomName} />
               <Button variant="solid" onClick={handleCreateRoom}>Create</Button>
            </Box>
         </Modal>
      </>
   );
}
