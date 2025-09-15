import { Box, Button, Card, Layout, Navbar } from "../ui";

export default function NotFound() {
   return (
      <Layout navbar={<Navbar brand="Websockets App" />}>
         <Box display="flex" justifyContent="center" margin="5rem 0 0 0">
            <Card padding="lg" style={{ width: "400px", textAlign: "center" }}>
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