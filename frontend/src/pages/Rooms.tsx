import { Box, Card, Layout, Navbar } from "../ui";

export default function Rooms() {
   return (
      <Layout navbar={<Navbar brand="Websockets App" />}>
         <Box justifyContent="center" alignItems="center" style={{ width: "100%", height: "100%" }}>
            <Card style={{ width: "500px", textAlign: "center" }}>
               <h2>Rooms</h2>
               <p>Here will be the list of user rooms.</p>
            </Card>
         </Box>
      </Layout>
   );
}