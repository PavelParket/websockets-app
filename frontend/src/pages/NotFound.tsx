import { Box, Button, Card, Layout, Navbar } from "../ui";

export default function NotFound() {
   return (
      <Layout navbar={<Navbar brand="Websockets App" />}>
         <Box justifyContent="center" alignItems="center" style={{ width: "100%", height: "100%" }}>
            <Card style={{ width: "400px", textAlign: "center" }}>
               <h2>404 - Page Not Found</h2>
               <p>The page you are looking for does not exist.</p>
               <Button to="/" variant="primary" size="md" style={{ marginTop: "16px" }}>
                  Back to Home
               </Button>
            </Card>
         </Box>
      </Layout>
   );
}