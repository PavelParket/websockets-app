import { useEffect } from "react";
import { Box, Button, Card, Container, Typography } from "../ui";
import { useSelector, useDispatch } from "react-redux";
import type { RootState, AppDispatch } from "../store/store";
import { fetchRooms } from "../store/feature/roomsSlice";

export default function Rooms() {
   const dispatch = useDispatch<AppDispatch>();
   const { rooms, loading, error } = useSelector((state: RootState) => state.rooms);

   useEffect(() => {
      dispatch(fetchRooms());
   }, [dispatch]);

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
                     <Button variant="solid" onClick={() => console.log(`Join room ${room}`)}>Join</Button>
                     <Button variant="outline" onClick={() => console.log(`Info room ${room}`)}>Info</Button>
                  </Card>
               ))}
            </Box>
         </Container>
      </Box>
   );
}
