import { Box, Button, Card, Layout, Navbar } from "../ui";

export default function Home() {
   return (
      <Layout
         navbar={
            <Navbar
               brand="Websockets App"
               rightContent={
                  <Button to="/register" variant="secondary" size="md">
                     Sign up
                  </Button>
               }
            />
         }
      >
         <Box display="flex" justifyContent="center" margin="5rem 0 0 0">
            <Card padding="lg" style={{ width: "400px", textAlign: "center" }}>
               <h2>Welcome 👋</h2>
               <p>Start exploring by visiting the rooms page.</p>
               <Button to="/rooms" variant="primary" size="md" style={{ marginTop: "16px" }}>
                  Go to Rooms
               </Button>
            </Card>
         </Box>
      </Layout>
   );
}