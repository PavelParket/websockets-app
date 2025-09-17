import { Box, Card, Container, Typography } from "../ui";

export default function Rooms() {
   return (
      <Box style={{ padding: "56px 0" }}>
         <Container>
            <Box style={{ display: "grid", placeItems: "center" }}>
               <Card style={{ width: "min(500px, 100%)", textAlign: "center" }}>
                  <Typography variant="h2">Rooms</Typography>
                  <Typography variant="body">Here will be the list of user rooms.</Typography>
               </Card>
            </Box>
         </Container>
      </Box>
   );
}
